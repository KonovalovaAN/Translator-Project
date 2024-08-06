package com.example.demo.service.impl;

import com.example.demo.config.GoogleConfig;
import com.example.demo.config.TextConfig;
import com.example.demo.dto.TextRequest;
import com.example.demo.dto.TextResponse;
import com.example.demo.exception.LanguageNotFoundException;
import com.example.demo.exception.TranslationResourceAccessException;
import com.example.demo.repository.TranslationRepository;
import com.example.demo.service.TextService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final GoogleConfig googleConfig;
    private final TranslationRepository translationRepository;
    private final TextConfig textConfig;  // Inject TextConfig to check available languages
    private static final int MAX_THREADS = 10;

    @Override
    public TextResponse translate(TextRequest textRequest, HttpServletRequest request) throws InterruptedException, ExecutionException {
        String langFrom = textRequest.getFrom();
        String langTo = textRequest.getTo();
        String text = textRequest.getText();

        // Check if the provided languages are supported
        Map<String, String> languages = textConfig.languages();
        if (!languages.containsKey(langFrom)) {
            throw new LanguageNotFoundException("Не найден язык исходного сообщения");
        }
        if (!languages.containsKey(langTo)) {
            throw new LanguageNotFoundException("Не найден язык целевого сообщения");
        }

        String resultText;

        if (text == null || text.trim().isEmpty()) {
            resultText = "";
        } else if (langFrom.equals(langTo)) {
            resultText = text;
        } else {
            List<String> words = Arrays.asList(text.split("\\s+"));
            ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
            List<Future<String>> futures = new ArrayList<>();

            try {
                for (String word : words) {
                    Future<String> future = executorService.submit(() -> {
                        try {
                            String translatedWord = translate(langFrom, langTo, word);
                            translatedWord = StringEscapeUtils.unescapeHtml4(translatedWord);
                            return applyOriginalCase(word, translatedWord);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return word;
                        }
                    });
                    futures.add(future);
                }

                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.HOURS);

                StringBuilder translatedString = new StringBuilder();
                for (Future<String> future : futures) {
                    try {
                        String translatedWord = future.get();
                        translatedString.append(translatedWord).append(" ");
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        translatedString.append(words.get(futures.indexOf(future))).append(" ");
                    }
                }

                resultText = translatedString.toString().trim();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                resultText = "Translation interrupted";
            } finally {
                if (!executorService.isTerminated()) {
                    executorService.shutdownNow();
                }
            }
        }

        String ipAddress = request.getRemoteAddr();
        translationRepository.saveTranslationRequest(ipAddress, text, resultText);

        return new TextResponse(resultText);
    }

    private String translate(String langFrom, String langTo, String text) {
        String url =
                googleConfig.translatorUri() +
                        "?q=" + text +
                        "&target=" + langTo +
                        "&source=" + langFrom;

        try {
            return new RestTemplate().getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            throw new TranslationResourceAccessException("Ошибка доступа к ресурсу перевода");
        }
    }

    private String applyOriginalCase(String original, String translated) {
        if (original.equals(original.toUpperCase())) {
            return translated.toUpperCase();
        } else if (original.equals(original.toLowerCase())) {
            return translated.toLowerCase();
        } else if (Character.isUpperCase(original.charAt(0))) {
            return Character.toUpperCase(translated.charAt(0)) + translated.substring(1).toLowerCase();
        } else {
            return translated;
        }
    }
}
