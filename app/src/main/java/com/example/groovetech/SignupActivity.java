package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.Modelo.Utilizador;
import com.example.groovetech.databinding.ActivitySignupBinding;
import com.example.groovetech.listeners.SignupListener;

public class SignupActivity extends AppCompatActivity implements SignupListener {

    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    private static final int MIN_PASS = 6;

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialização do View Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private boolean isUsernameValido(String username) {
        return !TextUtils.isEmpty(username) && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isEmailValido(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String pass) {
        return !TextUtils.isEmpty(pass) && pass.length() >= MIN_PASS;
    }

    public void onClickSignup(View view) {
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (!isUsernameValido(username)) {
            binding.etUsername.setError("Username inválido");
            return;
        }
        if (!isEmailValido(email)) {
            binding.etEmail.setError("Email inválido");
            return;
        }
        if (!isPasswordValida(password)) {
            binding.etPassword.setError("Password inválida deve conter pelo menos " + MIN_PASS + " caracteres");
            return;
        }
        Singleton.getInstance(this).signupAPI(username, password, email, getApplicationContext(), this);
    }

    public void goToLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Método que é chamado quando o registo é feito com a API
    public void onUpdateSignup(Utilizador user) {
        if (user != null) {
            Toast.makeText(this, "Registo com sucesso " + user.getUsername(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TOKEN, user.getAuth_key());
            String username = binding.etUsername.getText().toString();
            intent.putExtra(USERNAME, username);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Signup sem sucesso", Toast.LENGTH_SHORT).show();
        }

    }
}