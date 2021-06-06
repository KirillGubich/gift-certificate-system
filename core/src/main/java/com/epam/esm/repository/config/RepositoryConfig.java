package com.epam.esm.repository.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
//@PropertySource("/database.properties")
public class RepositoryConfig {

    @Profile("prod")
    @Bean
    public DataSource productionDataSource(@Value("com.mysql.cj.jdbc.Driver") String driverName,
                                           @Value("jdbc:mysql://localhost:3306/gift_certificates_system?serverTimezone=UTC") String url,
                                           @Value("root") String username,
                                           @Value("root") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Profile("dev")
    @Bean
    public DataSource developmentDataSource(@Value("com.mysql.cj.jdbc.Driver") String driverName,
                                            @Value("jdbc:mysql://localhost:3306/develop_gift_certificates_system?serverTimezone=UTC") String url,
                                            @Value("root") String username,
                                            @Value("root") String password) {
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
