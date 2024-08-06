package com.example.demo.service;

import com.example.demo.dto.TextRequest;
import com.example.demo.dto.TextResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

public interface TextService {
    TextResponse translate(TextRequest textRequest, HttpServletRequest request) throws InterruptedException, ExecutionException;
}
