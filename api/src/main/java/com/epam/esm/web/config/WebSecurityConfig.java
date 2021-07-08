package com.epam.esm.web.config;

import com.epam.esm.service.maintenance.UserService;
import com.epam.esm.web.filter.JwtCsrfFilter;
import com.epam.esm.web.security.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService service;
    private final JwtTokenRepository jwtTokenRepository;
    private final HandlerExceptionResolver resolver;

    @Autowired
    public WebSecurityConfig(UserService service, JwtTokenRepository jwtTokenRepository,
                             @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.service = service;
        this.jwtTokenRepository = jwtTokenRepository;
        this.resolver = resolver;
    }

    @Bean
    public PasswordEncoder devPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                    .addFilterAt(new JwtCsrfFilter(jwtTokenRepository, resolver), CsrfFilter.class)
//                    .csrf().ignoringAntMatchers("/**")
//                .and()
//                    .authorizeRequests()
//                    .antMatchers("/login")
//                    .authenticated()
//                .and()
//                    .httpBasic()
//                    .authenticationEntryPoint(((request, response, e) -> resolver
//                            .resolveException(request, response, null, e)));
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() //todo why?
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(HttpMethod.GET, "/tags/**")
                .antMatchers(HttpMethod.GET, "/certificates/**");
    }
}
