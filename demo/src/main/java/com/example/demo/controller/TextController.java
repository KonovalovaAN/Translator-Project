package com.example.demo.controller;

import com.example.demo.config.TextConfig;
import com.example.demo.dto.TextRequest;
import com.example.demo.dto.TextResponse;
import com.example.demo.service.TextService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class TextController {
    private final TextService textService;
    private final TextConfig textConfig;

    @PostMapping("/submit")
    public TextResponse submitText(@RequestBody TextRequest textRequest, HttpServletRequest request) throws InterruptedException, ExecutionException {
        return textService.translate(textRequest, request);
    }

    @GetMapping("/languages")
    public Map<String, String> getLanguages() {
        return textConfig.languages();
    }
}
