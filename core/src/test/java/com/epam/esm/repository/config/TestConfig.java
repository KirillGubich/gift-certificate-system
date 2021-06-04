package com.epam.esm.repository.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Profile("test")
@PropertySource({"classpath:/datasource.properties"})
@Configuration
@EnableTransactionManagement
public class TestConfig {
    @Value("${dataSource.user}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${dataSource.dbUrl}")
    private String url;
    @Value("${dataSource.driverName}")
    private String driverName;


    @Profile("test")
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

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}