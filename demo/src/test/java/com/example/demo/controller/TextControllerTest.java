package com.example.demo.controller;

import com.example.demo.config.TextConfig;
import com.example.demo.dto.TextRequest;
import com.example.demo.dto.TextResponse;
import com.example.demo.exception.LanguageNotFoundException;
import com.example.demo.exception.TranslationResourceAccessException;
import com.example.demo.service.TextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextController.class)
public class TextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextService textService;

    @MockBean
    private TextConfig textConfig;

    private Map<String, String> supportedLanguages;

    @BeforeEach
    public void setup() {
        supportedLanguages = new HashMap<>();
        supportedLanguages.put("en", "English");
        supportedLanguages.put("ru", "Russian");
        Mockito.when(textConfig.languages()).thenReturn(supportedLanguages);
    }

    @Test
    public void testSubmitTextSuccess() throws Exception {
        TextRequest request = new TextRequest();
        request.setText("Hello");
        request.setFrom("en");
        request.setTo("ru");

        Mockito.when(textService.translate(any(TextRequest.class), any())).thenReturn(new TextResponse("Привет"));

        mockMvc.perform(post("/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Hello\", \"from\": \"en\", \"to\": \"ru\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translatedText").value("Привет"));
    }

    @Test
    public void testSubmitTextUnsupportedSourceLanguage() throws Exception {
        TextRequest request = new TextRequest();
        request.setText("Hello");
        request.setFrom("fr");
        request.setTo("ru");

        Mockito.when(textService.translate(any(TextRequest.class), any())).thenThrow(new LanguageNotFoundException("Не найден язык исходного сообщения"));

        mockMvc.perform(post("/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Hello\", \"from\": \"fr\", \"to\": \"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.translatedText").value("Не найден язык исходного сообщения"));
    }

    @Test
    public void testSubmitTextUnsupportedTargetLanguage() throws Exception {
        TextRequest request = new TextRequest();
        request.setText("Hello");
        request.setFrom("en");
        request.setTo("de");

        Mockito.when(textService.translate(any(TextRequest.class), any())).thenThrow(new LanguageNotFoundException("Не найден язык целевого сообщения"));

        mockMvc.perform(post("/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Hello\", \"from\": \"en\", \"to\": \"de\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.translatedText").value("Не найден язык целевого сообщения"));
    }

    @Test
    public void testSubmitTextTranslationResourceError() throws Exception {
        TextRequest request = new TextRequest();
        request.setText("Hello");
        request.setFrom("en");
        request.setTo("ru");

        Mockito.when(textService.translate(any(TextRequest.class), any())).thenThrow(new TranslationResourceAccessException("Ошибка доступа к ресурсу перевода"));

        mockMvc.perform(post("/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Hello\", \"from\": \"en\", \"to\": \"ru\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.translatedText").value("Ошибка доступа к ресурсу перевода"));
    }

    @Test
    public void testGetLanguages() throws Exception {
        mockMvc.perform(get("/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.en").value("English"))
                .andExpect(jsonPath("$.ru").value("Russian"));
    }
}
