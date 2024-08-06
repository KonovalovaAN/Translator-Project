package com.example.demo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TranslationRepository {
    private final JdbcTemplate jdbcTemplate;

    public void saveTranslationRequest(String ipAddress, String inputText, String translatedText) {
        String sql = "INSERT INTO translation_requests (ip_address, input_text, translated_text) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, ipAddress, inputText, translatedText);
    }
}
