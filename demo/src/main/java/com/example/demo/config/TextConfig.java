package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "text")
public record TextConfig(
        Map<String, String> languages
) {
}
