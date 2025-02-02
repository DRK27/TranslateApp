package com.example.translateapp;

import java.util.List;

public class Translation {
    private boolean featured;
    private String text;
    private String pos;
    private List<Example> examples;

    public String getText() {
        return text;
    }

    public List<Example> getExamples() {
        return examples;
    }
}
