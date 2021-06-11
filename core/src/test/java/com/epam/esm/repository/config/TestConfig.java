package com.epam.esm.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Profile("test")
@Configuration
@ComponentScan("com.epam.esm")
@PropertySource({"classpath:/datasource.properties"})
@EnableTransactionManagement
public class TestConfig {

    @Value("${dataSource.user}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.driver}")
    private String driverName;

    @Bean
    public DataSource postgresqlDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName(driverName);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}