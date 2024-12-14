package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity  {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    private EditText etUsername, etPassword;
    private static final int MIN_PASS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicialização
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        //SingletonProdutos.getInstance(this).setLoginListener(this);
    }

    private boolean isUsernameValido(String username) {
        return !TextUtils.isEmpty(username) && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isPasswordValida(String pass) {
        return !TextUtils.isEmpty(pass) && pass.length() >= MIN_PASS;
    }

//    public void onSignupButtonClick(View view) {
//        startActivity(new Intent(this, SignupActivity.class));
//    }

    public void onClickLogin(View view) {
        String username = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        // Username and password validation with early returns to simplify flow
        if (!isUsernameValido(username)) {
            etUsername.setError("Username Inválido!");
            return;
        }
        if (!isPasswordValida(pass)) {
            etPassword.setError("Password Inválida!");
            return;
        }

        //SingletonProdutos.getInstance(this).loginAPI(username, pass, getApplicationContext());
    }

//    @Override
//    public void onUpdateLogin(Utilizador utilizador) {
//        if (utilizador.getAuth_key() != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra(TOKEN, utilizador.getAuth_key());
//            intent.putExtra(USERNAME, utilizador.getUsername());
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Token incorreto", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onUpdateSignup(Utilizador newUser) {
//        if (newUser.getAuth_key() != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra(TOKEN, newUser.getAuth_key());
//            intent.putExtra(USERNAME, newUser.getUsername());
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Token incorreto", Toast.LENGTH_SHORT).show();
//        }
//    }
}
