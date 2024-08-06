package com.example.demo.dto;

public class TextRequest {
    private String text;
    private String from;
    private String to;

    // Конструктор с параметрами
    public TextRequest(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    // Конструктор по умолчанию
    public TextRequest() {
    }

    // Getters and setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
