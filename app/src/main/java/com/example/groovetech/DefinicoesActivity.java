package com.example.groovetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.groovetech.databinding.ActivityDefinicoesBinding;

public class DefinicoesActivity extends AppCompatActivity {

    private ActivityDefinicoesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDefinicoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> finish());

        binding.confirmarBtn.setOnClickListener(v -> {
            String ipURL = binding.apiTxt.getText().toString();

            if (!ipURL.isEmpty()) {
                saveIpUrlToSharedPreferences(ipURL);
                Toast.makeText(DefinicoesActivity.this, "IP Confirmado: " + ipURL, Toast.LENGTH_SHORT).show();
                navigateToLoginActivity();
            }
        });

    }

    private void saveIpUrlToSharedPreferences(String apiText) {
        // Use SharedPreferences to save the text
        SharedPreferences preferences = getSharedPreferences("api_url", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("API", apiText);
        editor.apply();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}