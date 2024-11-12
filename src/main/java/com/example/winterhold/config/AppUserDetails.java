package com.example.winterhold.config;


import com.example.winterhold.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities = new ArrayList<>();
    private Boolean islock;

    public AppUserDetails(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.authorities.add(new SimpleGrantedAuthority(account.getRole()));
        this.islock = account.getIslocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !islock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

