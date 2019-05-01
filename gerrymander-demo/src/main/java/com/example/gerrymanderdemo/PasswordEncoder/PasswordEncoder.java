package com.example.gerrymanderdemo.PasswordEncoder;

import org.springframework.context.annotation.Bean;

public class PasswordEncoder {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private class BCryptPasswordEncoder extends PasswordEncoder {
    }
}
