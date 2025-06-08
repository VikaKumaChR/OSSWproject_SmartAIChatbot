package com.xingyang.chat.security.service;

import com.xingyang.chat.model.entity.User;
import com.xingyang.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService Implementation
 *
 * @author XingYang
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get user from database
        User user = userService.getUserByUsername(username);
        
        if (user == null) {
            log.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        // Check if user is active
        if (user.getStatus() != null && user.getStatus() != 1) {
            log.error("User is disabled: {}", username);
            throw new UsernameNotFoundException("User is disabled: " + username);
        }
        
        // Get user roles and convert to authorities
        List<GrantedAuthority> authorities;
        if (user.getRoles() != null && user.getRoles().length > 0) {
            authorities = Arrays.stream(user.getRoles())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        } else {
            // 添加默认用户权限
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        // Create and return UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
} 