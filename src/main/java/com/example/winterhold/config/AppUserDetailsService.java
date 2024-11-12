package com.example.winterhold.config;

import com.example.winterhold.repository.AccountRepository;
import com.example.winterhold.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account userLogin = accountRepository.findById(username)
                .orElseThrow(()->new UsernameNotFoundException("Username : "+username+" not found"));

        return User.builder()
                .username(userLogin.getUsername())
                .password(userLogin.getPassword())
                .roles(userLogin.getRole())
                .build();
    }
}
