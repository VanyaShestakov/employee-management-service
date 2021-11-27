package com.leverx.employeestat.rest.integration.config;

import com.leverx.employeestat.rest.configuration.Config;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

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
            dataSource.setUser(env.getRequiredProperty("user"));
            dataSource.setPassword(env.getRequiredProperty("password"));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
