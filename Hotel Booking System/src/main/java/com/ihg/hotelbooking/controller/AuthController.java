package com.ihg.hotelbooking.controller;

import com.ihg.hotelbooking.entity.User;
import com.ihg.hotelbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String username = authentication.getName();
        return userService.findByUsername(username)
            .map(user -> ResponseEntity.ok(new ProfileResponse(user.getUsername(), user.getEmail())))
            .orElseGet(() -> ResponseEntity.status(404).body(new ProfileResponse("", "")));
    }

    public static class ProfileResponse {
        public String username;
        public String email;
        public ProfileResponse(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        
        // Ensure roles are set if not provided
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new java.util.HashSet<>());
            user.getRoles().add("USER");
        }
        
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @Autowired
    private com.ihg.hotelbooking.config.JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            System.out.println("Login attempt for username: " + user.getUsername());
            
            // Validate input
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                System.out.println("Username is empty");
                return ResponseEntity.badRequest().body(new ErrorResponse("Username is required"));
            }
            
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                System.out.println("Password is empty");
                return ResponseEntity.badRequest().body(new ErrorResponse("Password is required"));
            }
            
            Optional<User> foundUser = userService.findByUsername(user.getUsername().trim());
            if (foundUser.isEmpty()) {
                System.out.println("User not found: " + user.getUsername());
                return ResponseEntity.status(401).body(new ErrorResponse("Invalid username or password"));
            }
            
            User dbUser = foundUser.get();
            System.out.println("User found: " + dbUser.getUsername() + ", checking password...");
            
            boolean passwordMatches = userService.getPasswordEncoder().matches(user.getPassword(), dbUser.getPassword());
            System.out.println("Password matches: " + passwordMatches);
            
            if (passwordMatches) {
                String token = jwtUtil.generateToken(dbUser.getUsername());
                System.out.println("Login successful for: " + dbUser.getUsername());
                return ResponseEntity.ok(new AuthResponse(token, dbUser.getUsername(), dbUser.getEmail()));
            } else {
                System.out.println("Password mismatch for: " + dbUser.getUsername());
                return ResponseEntity.status(401).body(new ErrorResponse("Invalid username or password"));
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred during login. Please try again."));
        }
    }

    public static class AuthResponse {
        public String token;
        public String username;
        public String email;
        public AuthResponse(String token, String username, String email) {
            this.token = token;
            this.username = username;
            this.email = email;
        }
    }
    
    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new ErrorResponse("Unauthorized"));
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }
        
        User user = userOpt.get();
        
        try {
            // Update email if provided and different
            if (request.email != null && !request.email.trim().isEmpty() && !request.email.equals(user.getEmail())) {
                // Check if email is already taken by another user
                Optional<User> existingUser = userService.findByEmail(request.email);
                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("Email already exists"));
                }
                user.setEmail(request.email.trim());
            }
            
            // Update password if provided
            if (request.newPassword != null && !request.newPassword.trim().isEmpty()) {
                if (request.currentPassword == null || request.currentPassword.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("Current password is required to change password"));
                }
                
                // Verify current password
                if (!userService.getPasswordEncoder().matches(request.currentPassword, user.getPassword())) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("Current password is incorrect"));
                }
                
                // Validate new password
                if (request.newPassword.length() < 6) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("New password must be at least 6 characters long"));
                }
                
                user.setPassword(userService.getPasswordEncoder().encode(request.newPassword));
            }
            
            //User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(new ProfileResponse(user.getUsername(), user.getEmail()));
            
        } catch (Exception e) {
            System.err.println("Profile update error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred while updating profile"));
        }
    }

    public static class ProfileUpdateRequest {
        public String email;
        public String currentPassword;
        public String newPassword;
        
        // Default constructor
        public ProfileUpdateRequest() {}
    }
}
