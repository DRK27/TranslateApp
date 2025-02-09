package com.example.translateapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        String url = "https://linguee-api.fly.dev/api/v2/translations?query=" + query + "&src=ru&dst=en";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        StringBuilder translationText = new StringBuilder();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject lemma = response.getJSONObject(i);
                            JSONArray translations = lemma.getJSONArray("translations");

                            for (int j = 0; j < translations.length(); j++) {
                                JSONObject translation = translations.getJSONObject(j);
                                String translatedWord = translation.getString("text");
                                translationText.append("Перевод: ").append(translatedWord).append("\n");
                            }
                        }
                        translationResult.setText(translationText.toString());

                    } catch (JSONException e) {
                        translationResult.setText("Ошибка обработки JSON: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    translationResult.setText("Ошибка запроса: " + error.getMessage());
                    Log.e("Volley", "Ошибка запроса: " + error.getMessage());
                    error.printStackTrace();
                });

        queue.add(jsonArrayRequest);
    }
}