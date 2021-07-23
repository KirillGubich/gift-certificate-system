package com.epam.esm.repository.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EntityScan("com.epam.esm")
@PropertySource("classpath:database.properties")
@PropertySource("classpath:dev_database.properties")
public class RepositoryConfig {

    @Profile("prod")
    @Bean
    public DataSource productionDataSource(@Value("${spring.database.driverClassName}") String driverName,
                                           @Value("${spring.database.url}") String url,
                                           @Value("${spring.database.username}") String username,
                                           @Value("${spring.database.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Profile("dev")
    @Bean
    public DataSource developmentDataSource(@Value("${spring.dev_database.driverClassName}") String driverName,
                                            @Value("${spring.dev_database.url}") String url,
                                            @Value("${spring.dev_database.username}") String username,
                                            @Value("${spring.dev_database.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
