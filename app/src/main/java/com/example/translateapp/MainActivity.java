package com.example.translateapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText inputWord;
    private Button translateButton;
    private TextView translationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputWord = findViewById(R.id.et_translation);
        translateButton = findViewById(R.id.translateButton);
        translationResult = findViewById(R.id.translationResult);

        translateButton.setOnClickListener(v -> {
            String query = inputWord.getText().toString().trim();
            if (query.isEmpty()) {
                translationResult.setText("Введите слово для перевода");
                return;
            }
            translateWord(query);
        });
    }

    private void translateWord(String query) {
        translateButton.setEnabled(false);
        translationResult.setText("Переводится...");

        LingueeApi api = ApiClient.getLingueeApi();
        Call<List<TranslationResponse>> call = api.getTranslation(query, "ru", "en");

        call.enqueue(new Callback<List<TranslationResponse>>() {
            @Override
            public void onResponse(Call<List<TranslationResponse>> call, Response<List<TranslationResponse>> response) {
                translateButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    TranslationResponse translation = response.body().get(0);
                    StringBuilder examples = new StringBuilder();

                    for (Translation t : translation.getTranslations()) {
                        Log.d("Translation", "Text: " + t.getText());
                        examples.append("Перевод: ").append(t.getText()).append("\n");

                        if (t.getExamples() != null) {
                            for (Example ex : t.getExamples()) {
                                examples.append(ex.getSource()).append(" - ").append(ex.getTarget()).append("\n");
                            }
                        }
                    }
                    translationResult.setText(examples.toString());
                } else {
                    translationResult.setText("Ошибка: перевод не найден.");
                }
            }

            @Override
            public void onFailure(Call<List<TranslationResponse>> call, Throwable t) {
                translateButton.setEnabled(true);
                translationResult.setText("Ошибка сети: " + t.getMessage());
            }
        });
    }
}