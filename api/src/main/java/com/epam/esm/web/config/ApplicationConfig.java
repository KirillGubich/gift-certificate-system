package com.epam.esm.web.config;

import com.epam.esm.service.converter.GiftCertificateConverter;
import com.epam.esm.service.converter.GiftCertificateDtoConverter;
import com.epam.esm.service.converter.OrderConverter;
import com.epam.esm.service.converter.OrderDtoConverter;
import com.epam.esm.service.converter.RoleConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.converter.TagDtoConverter;
import com.epam.esm.service.converter.UserConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

/**
 * Configuration class for web initializing web application context.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        TagConverter tagConverter = new TagConverter();
        RoleConverter roleConverter = new RoleConverter();
        UserConverter userConverter = new UserConverter(roleConverter);
        GiftCertificateConverter giftCertificateConverter = new GiftCertificateConverter(tagConverter);
        OrderConverter orderConverter = new OrderConverter(userConverter, giftCertificateConverter);
        registry.addConverter(tagConverter);
        registry.addConverter(new TagDtoConverter());
        registry.addConverter(orderConverter);
        registry.addConverter(new OrderDtoConverter());
        registry.addConverter(userConverter);
        registry.addConverter(giftCertificateConverter);
        registry.addConverter(new GiftCertificateDtoConverter());
    }

    @Bean
    public ViewResolver viewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        return resolver;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
