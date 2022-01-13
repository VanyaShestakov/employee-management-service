package com.leverx.employeestat.rest.integration.config;

import com.leverx.employeestat.rest.configuration.Config;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class TestConfig extends Config {

    private final Environment env;

    @Autowired
    public TestConfig(Environment env) {
        super(env);
        this.env = env;
    }

    @Bean
    @Override
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(env.getRequiredProperty("driverClass"));
            dataSource.setJdbcUrl(env.getRequiredProperty("testUrl"));
            dataSource.setUser(env.getRequiredProperty("dbuser"));
            dataSource.setPassword(env.getRequiredProperty("password"));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    @Override
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/liquibase-changeLog-test.xml");
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


}
