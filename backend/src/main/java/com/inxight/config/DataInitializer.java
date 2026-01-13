package com.inxight.config;

import com.inxight.model.Admin;
import com.inxight.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeDefaultAdmin() {
        return args -> {
            // Check if default admin already exists
            if (!adminRepository.existsByUsername("admin")) {
                Admin defaultAdmin = new Admin();
                defaultAdmin.setUsername("admin");
                defaultAdmin.setEmail("admin@inxight.com");
                defaultAdmin.setPassword(passwordEncoder.encode("welcome01"));
                
                adminRepository.save(defaultAdmin);
                System.out.println("Default admin user created: username=admin, password=welcome01");
            }
        };
    }
}
