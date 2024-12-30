package com.example.groovetech.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groovetech.PaginaInicialFragment;
import com.example.groovetech.R;
import com.example.groovetech.listeners.HomeProdutosListener;
import com.example.groovetech.listeners.HomeProdutosSearchListener;
import com.example.groovetech.listeners.LoginListener;
import com.example.groovetech.listeners.SignupListener;
import com.example.groovetech.utils.LoginJsonParser;
import com.example.groovetech.utils.ProdutoJsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Singleton {

    private static volatile Singleton instance = null;
    private static RequestQueue volleyQueue = null;

    private Utilizador utilizador;

    private ArrayList<Produto> listaProdutos = new ArrayList<>();


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

    public void loginAPI(String username, String password, Context context, final LoginListener loginListener) {
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
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPILogin(context), json, new Response.Listener<JSONObject>() {

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

    public void signupAPI(String username, String password, String email, Context context, final SignupListener signupListener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
                json.put("email", email);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPISignup(context), json, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("LoginAPI", "Raw Response: " + response.toString());
                    utilizador = LoginJsonParser.parserJsonLogin(response);

                    // Guardar o id do utilizador, token, e username em SharedPreferences
                    saveUserPreferences(context, utilizador.getId(), utilizador.getAuth_key(), utilizador.getUsername());

                    if (signupListener != null) {
                        signupListener.onUpdateSignup(utilizador);
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
                }
            });
            volleyQueue.add(req);
        }

    }

    public void getAllProdutosAPI(Context context, final HomeProdutosListener produtosListener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();

            if (produtosListener != null) {
                produtosListener.onRefreshHomeProdutos(listaProdutos);
            }
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIProdutos(context), null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.d("ProdutosAPI", "Raw Response: " + response.toString());
                    listaProdutos = ProdutoJsonParser.parserJsonProdutos(response);

                    // Notificar o listener que a lista de produtos foi atualizada
                    if (produtosListener != null) {
                        produtosListener.onRefreshHomeProdutos(listaProdutos);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Dados Incorretos", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void getSearchProdutosAPI(final Context context, final String query, final HomeProdutosSearchListener searchedProdutosListener) {
        if (context == null) {
            return;
        }

        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepara a URL para a API
        String url = UrlAPISearchProdutos(context) + query;


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Produtos pesquisados:", "Raw Response: " + response.toString());
                listaProdutos = ProdutoJsonParser.parserJsonProdutos(response);

                // Notificar o listener com a lista de produtos
                if (searchedProdutosListener != null) {
                    searchedProdutosListener.onSearchResults(listaProdutos);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Resultados para \"" + query + "\" não encontrados.";

                /**
                 * Atualizar o UI usando o contexto
                 */
                if (context instanceof AppCompatActivity) {

                    /**
                     * O 'context' pode ser de vários tipos (por exemplo, Activity, fragment, etc...),
                     * que é uma classe usada em atividades que oferecem suporte a fragmentos e outros componentes da UI.
                     */
                    AppCompatActivity activity = (AppCompatActivity) context;

                    /**
                     * Usar 'getSupportFragmentManager()' se for uma AppCompatActivity.
                     * Uma 'AppCompatActivity' possui o método 'getSupportFragmentManager()',
                     * que é responsável por manipular os fragmentos dentro da atividade.
                     * Neste caso, usámos a tag associada à classe 'PaginaInicialFragment'.
                     */
                    Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_pagina_inicial);

                    if (fragment instanceof PaginaInicialFragment) {

                        // Atualiza a UI no Fragment chamando o método 'updateTituloTxt()' no fragmento.
                        // como o título da tela, com a mensagem de erro recebida (errorMessage).
                        ((PaginaInicialFragment) fragment).updateTituloTxt(errorMessage);
                    }
                }
            }
        });

        volleyQueue.add(req);
    }


    public ArrayList<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private String UrlAPILogin(Context context) {

        return "http://" + getApiIP(context) + "/api/auth/login";

    }

    private String UrlAPIProdutos(Context context) {
        return "http://" + getApiIP(context) + "/api/produto/all";
    }

    private String UrlAPISignup(Context context) {
        return "http://" + getApiIP(context) + "/api/auth/signup";
    }

    private String UrlAPISearchProdutos(Context context) {
        return "http://" + getApiIP(context) + "/api/produto/search?query=";
    }


    public void saveUserPreferences(Context context, int userId, String token, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", userId);
        editor.putString("user_token", token);
        editor.putString("username", username);
        editor.apply();
    }

    public String getApiIP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("api_url", Context.MODE_PRIVATE);
        return preferences.getString("API", null);
    }
}