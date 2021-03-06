package com.leverx.employeestat.rest.security.config;

import com.leverx.employeestat.rest.security.employeedetails.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
public class MultipleSecurityConfig {

    @Order(1)
    @Configuration
    public static class OAuth2Config extends WebSecurityConfigurerAdapter {

        private final ClientRegistrationRepository clientRegistrationRepository;
        private final OAuth2AuthorizedClientService authorizedClientService;

        @Autowired
        public OAuth2Config(ClientRegistrationRepository clientRegistrationRepository,
                            OAuth2AuthorizedClientService authorizedClientService) {
            this.clientRegistrationRepository = clientRegistrationRepository;
            this.authorizedClientService = authorizedClientService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .requestMatchers()
                    .antMatchers(
                            "/oauth2/authorization/google",
                            "/api/employees/export",
                            "/api/employees/export/last",
                            "/login/oauth2/code/google/**")
                    .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login()
                    .clientRegistrationRepository(clientRegistrationRepository)
                    .authorizedClientService(authorizedClientService);
        }
    }

    @Order(2)
    @Configuration
    public static class BasicAuthorizationConfig extends WebSecurityConfigurerAdapter {

        private final EmployeeDetailsService employeeDetailsService;
        private final CustomBasicAuthenticationEntryPoint authenticationEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;

        @Autowired
        public BasicAuthorizationConfig(EmployeeDetailsService employeeDetailsService,
                                        CustomBasicAuthenticationEntryPoint authenticationEntryPoint,
                                        CustomAccessDeniedHandler accessDeniedHandler) {
            this.employeeDetailsService = employeeDetailsService;
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.accessDeniedHandler = accessDeniedHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/reset-password").permitAll()
                    .antMatchers("/api/doc").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/**/**/").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/*").hasRole("MANAGER")
                    .antMatchers(HttpMethod.PUT, "/api/*").hasRole("MANAGER")
                    .antMatchers(HttpMethod.DELETE, "/api/**/**").hasRole("MANAGER")
                    .anyRequest().authenticated()
                    .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                    .and().sessionManagement().disable()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        }

        @Override
        public void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder.userDetailsService(employeeDetailsService);
        }
    }
}
