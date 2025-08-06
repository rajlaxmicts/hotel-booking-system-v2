package com.ihg.hotelbooking.service;

import com.ihg.hotelbooking.entity.User;
import com.ihg.hotelbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        return findByUsername(username)
            .map(user -> org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().toArray(new String[0]))
                .build())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User registerUser(User user) {
        System.out.println("Registering user: " + user.getUsername() + " with email: " + user.getEmail());
        System.out.println("User roles before save: " + user.getRoles());
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        System.out.println("User saved with ID: " + savedUser.getId() + " and roles: " + savedUser.getRoles());
        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
