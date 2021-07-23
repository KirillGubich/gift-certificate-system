package com.epam.esm.web.security;

import com.epam.esm.web.filter.ExceptionHandlerFilter;
import com.epam.esm.web.filter.JwtTokenFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenFilter jwtTokenFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    public JwtConfigurer(JwtTokenFilter jwtTokenFilter, ExceptionHandlerFilter exceptionHandlerFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class);
    }
}
