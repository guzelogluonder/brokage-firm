package com.brokerage.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generate hashes
        String adminHash = encoder.encode("admin123");
        String customerHash = encoder.encode("password123");

        System.out.println("admin123: " + adminHash);
        System.out.println("123456: " + customerHash);

        // Verify hashes
        System.out.println("Verify admin123: " + verify("admin123", adminHash));
        System.out.println("Verify wrong password: " + verify("wrongpassword", adminHash));
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainPassword, hashedPassword);
    }
}
