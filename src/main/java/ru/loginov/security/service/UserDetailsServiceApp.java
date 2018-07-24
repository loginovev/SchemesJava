package ru.loginov.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsServiceApp extends org.springframework.security.core.userdetails.UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
