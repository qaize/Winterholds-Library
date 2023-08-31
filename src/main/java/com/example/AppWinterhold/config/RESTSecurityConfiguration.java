package com.example.AppWinterhold.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class RESTSecurityConfiguration {


    @Autowired
    public JWTSecurityConfiguration jwtSecurityConfiguration;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{

        http.antMatcher("/api/**").csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated().and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().addFilterBefore(jwtSecurityConfiguration, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    Akses secuity HTTP
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("POST","GET","PATCH","PUT","DELETE"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;

    }

}
