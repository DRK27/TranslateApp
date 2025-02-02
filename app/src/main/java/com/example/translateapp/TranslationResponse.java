package com.example.translateapp;

import java.util.List;

public class TranslationResponse {
    private boolean featured;
    private String text;
    private String pos;
    private List<Translation> translations;

    public List<Translation> getTranslations() {
        return translations;
    }

    public String getText() {
        return text;
    }
}

