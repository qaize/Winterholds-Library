package com.example.AppWinterhold;

import com.example.AppWinterhold.Dao.AccountRepository;
import com.example.AppWinterhold.Entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
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

        return new AppUserDetails(userLogin);
    }
}
