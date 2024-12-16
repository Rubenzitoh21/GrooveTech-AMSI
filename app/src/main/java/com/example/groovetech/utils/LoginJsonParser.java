package com.example.groovetech.utils;

import android.util.Log;

import com.example.groovetech.Modelo.Utilizador;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJsonParser {
    public static Utilizador parserJsonLogin(JSONObject response) {
        Utilizador utilizador = null;
        try {
            JSONObject userJSON = response.getJSONObject("user");

            int id = userJSON.getInt("id");
            String username = userJSON.getString("username");
            String auth_key = userJSON.getString("auth_key");
            String password_hash = userJSON.getString("password_hash");
            String password_reset_token = userJSON.isNull("password_reset_token") ? "" : userJSON.getString("password_reset_token");
            String email = userJSON.getString("email");
            String status = userJSON.getString("status");

            String created_at = String.valueOf(userJSON.getLong("created_at"));
            String updated_at = String.valueOf(userJSON.getLong("updated_at"));

            String verification_token = "";

            utilizador = new Utilizador(id, username, email,
                    auth_key, password_hash, password_reset_token,
                    status, created_at, updated_at, verification_token);



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return utilizador;

    }
}
