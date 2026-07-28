package com.example.demo.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class InMemoryUserConfig {


    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {


        UserDetails user =
                User.builder()
                        .username("john")
                        .password(
                            passwordEncoder.encode("password123")
                        )
                        .roles("USER")
                        .build();



        UserDetails admin =
                User.builder()
                        .username("sam")
                        .password(
                            passwordEncoder.encode("password123")
                        )
                        .roles("ADMIN")
                        .build();



        return new InMemoryUserDetailsManager(
                user,
                admin
        );
    }
}