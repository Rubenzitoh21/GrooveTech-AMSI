package com.example.groovetech.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groovetech.listeners.LoginListener;
import com.example.groovetech.utils.LoginJsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

public class Singleton {

    private static volatile Singleton instance = null;
    private static RequestQueue volleyQueue = null;

    private Utilizador utilizador;

    private LoginListener loginListener;

    public Singleton(Context context) {

    }

    public static synchronized Singleton getInstance(Context context) {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton(context);
                    volleyQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return instance;
    }

    public void loginAPI(String username, String password, Context context) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPILogin(), json, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LoginAPI", "Raw Response: " + response.toString());
                    utilizador = LoginJsonParser.parserJsonLogin(response);


                    // Guardar o id do utilizador, token, e username em SharedPreferences
                    saveUserPreferences(context, utilizador.getId(), utilizador.getAuth_key(), utilizador.getUsername());

                    // Adiciona o utilizador à database apenas se não existir
                    if (utilizador.getId() != 0 || utilizador.getAuth_key() != null) {
                        //TODO:getUserDataAPI(context);
                    }

                    if (loginListener != null) {
                        loginListener.onUpdateLogin(utilizador);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Dados Incorretos", Toast.LENGTH_SHORT).show();
                    if (utilizador != null) {
                        Log.e("LoginAPI", "Utilizador Existe: " + utilizador.getUsername());
                    } else {
                        Log.e("LoginAPI", "Utilizador Não Existe");
                    }
                    // verficar se o login listener não é null antes de chamar o método
                    if (loginListener != null) {
                        loginListener.onUpdateLogin(null);
                    }
                }
            });
            volleyQueue.add(req);
        }

    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private String UrlAPILogin() {
        return "http://172.22.21.211:8080/api/auth/login";
    }


    public void saveUserPreferences(Context context, int userId, String token, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", userId);
        editor.putString("user_token", token);
        editor.putString("username", username);
        editor.apply();
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}