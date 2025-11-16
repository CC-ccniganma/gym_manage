package com.milotnt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;

@Component
public class PasswordEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return encoder;
    }

    public static String encode(String password) {
        if (password == null) {
            return null;
        }
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encodedPassword);
    }
} 