package com.example.groovetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        // Verificar se o utilizador já tem token guardado
        if (HasUserToken(this)) {
            redirectToMainActivity();
        } else {
            // Inicialização do View Binding
            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
        }


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

        Singleton.getInstance(this).loginAPI(username, pass, getApplicationContext(), this);
    }

    @Override
    public void onUpdateLogin(Utilizador utilizador) {

        if (utilizador.getAuth_key() != null) {
            Toast.makeText(this, "Login efetuado com sucesso " + utilizador.getUsername(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TOKEN, utilizador.getAuth_key());
            intent.putExtra(USERNAME, utilizador.getUsername());

            startActivity(intent);
            //finish();
        } else {
            Toast.makeText(this, "Token incorreto", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean HasUserToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getString("user_token", null) != null;
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // não permite que o utilizador volte para a activity anterior
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Limpa os campos de texto quando a activity é retomada
        if (binding.etUsername != null) {
            binding.etUsername.setText(""); // Clear the username field
        }
        if (binding.etPassword != null) {
            binding.etPassword.setText(""); // Clear the password field
        }
    }
}
