package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.Modelo.Utilizador;
import com.example.groovetech.databinding.ActivityLoginBinding;
import com.example.groovetech.listeners.LoginListener;
import com.example.groovetech.listeners.SignupListener;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    private static final int MIN_PASS = 4;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialização do View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Singleton.getInstance(this).setLoginListener(this);


    }

    private boolean isUsernameValido(String username) {
        return !TextUtils.isEmpty(username) && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isPasswordValida(String pass) {
        return !TextUtils.isEmpty(pass) && pass.length() >= MIN_PASS;
    }

    public void onSignupButtonClick(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void onClickLogin(View view) {
        String username = binding.etUsername.getText().toString();
        String pass = binding.etPassword.getText().toString();

        // Verificar se os campos estão preenchidos
        if (!isUsernameValido(username)) {
            binding.etUsername.setError("Username Inválido!");
            return;
        }
        if (!isPasswordValida(pass)) {
            binding.etPassword.setError("Password Inválida!");
            return;
        }

        Singleton.getInstance(this).loginAPI(username, pass, getApplicationContext());
    }

    @Override
    public void onUpdateLogin(Utilizador utilizador) {
        if(utilizador.getAuth_key() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TOKEN, utilizador.getAuth_key());
            intent.putExtra(USERNAME, utilizador.getUsername());

            startActivity(intent);
            //finish();
        }
        else {
            Toast.makeText(this, "Token incorreto", Toast.LENGTH_SHORT).show();
        }
    }

}
