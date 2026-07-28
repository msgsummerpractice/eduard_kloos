package com.example.demo.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            Role userRole = new Role();
            userRole.setName("USER");

            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ADMIN");

            roleRepository.save(adminRole);

            User john = new User();

            john.setName("John");
            john.setEmail("john@test.com");
            john.setPassword(
                    passwordEncoder.encode("password123")
            );

            john.setRoles(
                    Set.of(userRole)
            );

            userRepository.save(john);

            User sam = new User();

            sam.setName("Sam");
            sam.setEmail("sam@test.com");
            sam.setPassword(
                    passwordEncoder.encode("password123")
            );

            sam.setRoles(
                    Set.of(adminRole)
            );

            userRepository.save(sam);

        };

    }

}