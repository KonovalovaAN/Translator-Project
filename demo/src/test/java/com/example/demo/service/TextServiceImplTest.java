package com.example.demo.service;

import com.example.demo.config.GoogleConfig;
import com.example.demo.config.TextConfig;
import com.example.demo.dto.TextRequest;
import com.example.demo.dto.TextResponse;
import com.example.demo.exception.LanguageNotFoundException;
import com.example.demo.repository.TranslationRepository;
import com.example.demo.service.impl.TextServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TextServiceImplTest {

    @InjectMocks
    private TextServiceImpl textService;

    @Mock
    private GoogleConfig googleConfig;

    @Mock
    private TextConfig textConfig;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TranslationRepository translationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        textService = new TextServiceImpl(googleConfig, translationRepository, textConfig); // Ensure the injected dependencies are properly initialized
    }

    @Test
    public void testTranslate_SuccessfulTranslation_ReturnsTranslatedText() throws Exception {
        String langFrom = "en";
        String langTo = "fr";
        String text = "Hello world";
        String translatedWord1 = "Bonjour";
        String translatedWord2 = "monde";

        when(textConfig.languages()).thenReturn(Map.of("en", "English", "fr", "French"));
        when(googleConfig.translatorUri()).thenReturn("https://script.google.com/macros/s/AKfycbye8HmoCku3NPWV6VqL3Vd1BOfkgb5wVRJaGJoIApS_Mg4wHJSvC-09b95-zaWq-arK4Q/exec");

        when(restTemplate.getForObject(
                buildUri(langFrom, langTo, "Hello"),
                String.class))
                .thenReturn(translatedWord1);
        when(restTemplate.getForObject(
                buildUri(langFrom, langTo, "world"),
                String.class))
                .thenReturn(translatedWord2);

        TextRequest textRequest = new TextRequest(langFrom, langTo, text);
        TextResponse response = textService.translate(textRequest, request);

        assertEquals("Bonjour monde", response.translatedText());
    }

    @Test
    void translate_UnsupportedSourceLanguage_ThrowsLanguageNotFoundException() {
        TextRequest textRequest = new TextRequest("zz", "fr", "Hello");
        when(textConfig.languages()).thenReturn(Map.of("en", "English", "fr", "French"));

        LanguageNotFoundException thrown = assertThrows(LanguageNotFoundException.class, () ->
                textService.translate(textRequest, request));
        assertEquals("Не найден язык исходного сообщения", thrown.getMessage());
    }

    @Test
    void translate_UnsupportedTargetLanguage_ThrowsLanguageNotFoundException() {
        TextRequest textRequest = new TextRequest("en", "zz", "Hello");
        when(textConfig.languages()).thenReturn(Map.of("en", "English", "fr", "French"));

        LanguageNotFoundException thrown = assertThrows(LanguageNotFoundException.class, () ->
                textService.translate(textRequest, request));
        assertEquals("Не найден язык целевого сообщения", thrown.getMessage());
    }

    @Test
    void translate_EmptyText_ReturnsEmptyText() throws Exception {
        TextRequest textRequest = new TextRequest("en", "fr", "");
        when(textConfig.languages()).thenReturn(Map.of("en", "English", "fr", "French"));

        TextResponse response = textService.translate(textRequest, request);

        assertEquals("", response.translatedText());
    }

    @Test
    void translate_SameSourceAndTargetLanguage_ReturnsOriginalText() throws Exception {
        TextRequest textRequest = new TextRequest("en", "en", "Hello");
        when(textConfig.languages()).thenReturn(Map.of("en", "English"));

        TextResponse response = textService.translate(textRequest, request);

        assertEquals("Hello", response.translatedText());
    }

    private String buildUri(String langFrom, String langTo, String text) {
        try {
            String baseUri = "https://script.google.com/macros/s/AKfycbye8HmoCku3NPWV6VqL3Vd1BOfkgb5wVRJaGJoIApS_Mg4wHJSvC-09b95-zaWq-arK4Q/exec";
            return baseUri + "?q=" + URLEncoder.encode(text, "UTF-8") + "&target=" + langTo + "&source=" + langFrom;
        } catch (Exception e) {
            throw new RuntimeException("Error building URI", e);
        }
    }
}
