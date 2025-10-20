package com.example.forum_app.service;

import com.example.forum_app.model.User;
import com.example.forum_app.model.CustomUserDetails;
import com.example.forum_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;  // Added import for List

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setUsername(user.getUsername().trim().toLowerCase());
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        user.setRole("USER");

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        try {
            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully: " + savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String trimmedUsername = username.trim().toLowerCase();
        System.out.println("Attempting to load user: " + trimmedUsername);

        User user = userRepository.findByUsername(trimmedUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + trimmedUsername));

        System.out.println("User found: " + user.getUsername() + ", Role: " + user.getRole());
        return new CustomUserDetails(user);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
