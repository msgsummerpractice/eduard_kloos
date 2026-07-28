package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findTop10ByNameContainingIgnoreCaseOrderByNameAsc(String name);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
}
