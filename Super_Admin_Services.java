package com.example.Project_1.Services;

import com.example.Project_1.Model.Employee2;
import com.example.Project_1.Model.Role;
import com.example.Project_1.Repository.Employee2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Super_Admin_Services implements CommandLineRunner {

    @Autowired
    private Employee2Repository employee2Repository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public void run(String... args) throws Exception {
        boolean superAdmin= employee2Repository.existsByRole(Role.SUPER_ADMIN);
        boolean Admin= employee2Repository.existsByRole(Role.ADMIN);
            System.out.println("Admin count in database: " + Admin);
            System.out.println("Super-Admin count in database: " + superAdmin);

        if (!superAdmin) {
            Employee2 employee2 = new Employee2();
            employee2.setUsername("Super-Admin");
            employee2.setPassword(encoder.encode("super@123"));
            employee2.setEmail("SA@gmail.com");
            employee2.setRole(Role.SUPER_ADMIN);
            employee2Repository.save(employee2);
            System.out.println("Super-Admin user created successfully!");
        } else {
            System.out.println("Super-Admin user already exists. Skipping creation.");
        }
    }
}
