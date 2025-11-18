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

    /**
     * 检查密码是否已经是加密格式（BCrypt加密后的密码通常以$2a$、$2b$或$2y$开头，长度为60）
     * @param password 密码字符串
     * @return 如果已经是加密格式返回true，否则返回false
     */
    public static boolean isEncoded(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
} 