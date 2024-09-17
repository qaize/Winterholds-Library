//package com.example.winterhold.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class RESTSecurityConfiguration {
//
//
//    public final JWTSecurityConfiguration jwtSecurityConfiguration;
//
//    @Bean("user1")
//    public AppUserDetailsService userDetailsService() {
//        return new AppUserDetailsService();
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/api/**").permitAll()
//                        .anyRequest().authenticated())
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtSecurityConfiguration, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
////    Akses secuity HTTP
//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        var configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:7082"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowedMethods(List.of("POST","GET","PATCH","PUT","DELETE"));
//
//        var source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",configuration);
//        return source;
//
//    }
//
//    // Password Encoding
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//}
