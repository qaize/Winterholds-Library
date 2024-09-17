package com.example.winterhold.config;

import com.example.winterhold.security.CustomAuthenticationSuccessHandler;
import com.example.winterhold.security.CustomAuthenticationFailureHandler;
import com.example.winterhold.service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/resources/**", "/login/**", "/register/**", "/home/**", "/book/**", "/category/**", "/author/**").permitAll()
                        .requestMatchers(
                                "/customer/index",
                                "/customer/insert",
                                "/customer/delete",
                                "/customer/extend",
                                "/customer/ban",
                                "/customer/unban",
                                "/customer/banned-customer",
                                "/loan/history",
                                "/loan/insert",
                                "/loan/return",
                                "/loan/detail",
                                "/loan/denda",
                                "/loan/payment",
                                "/loan/extend",
                                "/loan/paymentHistory",
                                "/notification/show-all-notification"
                        ).hasRole("administrator")
                        .requestMatchers(
                                "/notification/show-notification-by-id",
                                "/customer/detail",
                                "/customer/update",
                                "/loan/request-loan-list",
                                "/loan/request-loan"
                        ).hasAnyRole("customer", "administrator")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login/loginForm")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/loginProcess")
                        .failureHandler(authenticationFailureHandler())
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/loginForm?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling()
                .accessDeniedPage("/login/accessDenied");

            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }




}
