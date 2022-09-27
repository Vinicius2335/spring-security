package com.api.parkingcontrol.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UuidGenerator {
    public static void main(String[] args) {
        System.out.println("UUID: " + UUID.randomUUID());
        System.out.println("Password: " + new BCryptPasswordEncoder().encode("devdojo"));
    }
}
