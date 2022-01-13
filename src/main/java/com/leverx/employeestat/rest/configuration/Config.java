package com.leverx.employeestat.rest.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.leverx.employeestat.rest*")
@EnableWebMvc
@EnableTransactionManagement
@EnableSwagger2
@EnableJpaRepositories(basePackages = "com.leverx.employeestat.rest.repository*")
@PropertySource({
        "classpath:properties/db.properties",
        "classpath:properties/session-factory.properties",
        "classpath:security/google-security.properties",
        "classpath:application.properties"})
@EnableScheduling
@Slf4j
public class Config implements WebMvcConfigurer {

    private final static List<String> clients = Arrays.asList("google");
    private final static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    @Value("${driverClass}")
    private String driverClass;

    @Value("${dbuser}")
    private String user;

    @Value("${password}")
    private String password;

    @Value("${jdbcUrl}")
    private String jdbcUrl;

    private Environment env;

    @Autowired
    public Config(Environment env) {
        this.env = env;
    }
    
    public Config() {

    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/liquibase-changeLog.xml");
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            log.info(String.format("Connecting to the datasource (driver=%s, url=%s, user=%s)", driverClass, jdbcUrl, user));

            dataSource.setDriverClass(driverClass);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(user);
            dataSource.setPassword(password);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        factory.setPackagesToScan(env.getRequiredProperty("packagesToScan"));
        Properties props = new Properties();
        props.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        props.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        props.put("hibernate.ddl-auto", env.getRequiredProperty("hibernate.ddl-auto"));
        factory.setJpaProperties(props);
        return factory;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10000000L);
        return resolver;
    }

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());

        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Employees Service",
                "API for keeping track of employees and their employment",
                "0.9 BETA", "",
                new Contact(
                        "Ivan Shestakov",
                        "https://github.com/VanyaShestakov",
                        "ivan.shestakov.official@gmail.com"),
                "", "", Collections.emptyList());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ClientRegistration getRegistration(String client) {
        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE
                    .getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        return null;
    }
}