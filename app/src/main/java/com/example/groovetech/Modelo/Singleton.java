package com.example.groovetech.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groovetech.PaginaInicialFragment;
import com.example.groovetech.R;
import com.example.groovetech.listeners.CarrinhoListener;
import com.example.groovetech.listeners.ExpedicoesListener;
import com.example.groovetech.listeners.FaturasListener;
import com.example.groovetech.listeners.HomeProdutosListener;
import com.example.groovetech.listeners.HomeProdutosSearchListener;
import com.example.groovetech.listeners.LinhasCarrinhoListener;
import com.example.groovetech.listeners.LinhasFaturasListener;
import com.example.groovetech.listeners.LoginListener;
import com.example.groovetech.listeners.PagamentosListener;
import com.example.groovetech.listeners.PerfilEditListener;
import com.example.groovetech.listeners.PerfilListener;
import com.example.groovetech.listeners.SignupListener;
import com.example.groovetech.utils.CarrinhoJsonParser;
import com.example.groovetech.utils.ExpedicoesJsonParser;
import com.example.groovetech.utils.FaturasJsonParser;
import com.example.groovetech.utils.LinhaCarrinhoJsonParser;
import com.example.groovetech.utils.LinhasFaturasJsonParser;
import com.example.groovetech.utils.LinhasFaturasTotaisJsonParser;
import com.example.groovetech.utils.LoginAndSignupJsonParser;
import com.example.groovetech.utils.PagamentosJsonParser;
import com.example.groovetech.utils.ProdutoJsonParser;
import com.example.groovetech.utils.PerfilJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Singleton {

    private static volatile Singleton instance = null;
    private static RequestQueue volleyQueue = null;

    private Utilizador utilizador;

    private Perfil perfil;

    private Carrinho carrinho;

    private UtilizadorAndPerfil utilizadorAndPerfil;

    private ArrayList<Produto> listaProdutos = new ArrayList<>();

    private ArrayList<Produto> searchedProdutos = new ArrayList<>();

    private ArrayList<Pagamento> listaPagamentos = new ArrayList<>();

    private ArrayList<Expedicao> listaExpedicoes = new ArrayList<>();

    private ArrayList<Fatura> listaFaturas = new ArrayList<>();

    private ArrayList<LinhasFaturas> listaLinhasFaturas = new ArrayList<>();

    private ArrayList<LinhaCarrinho> listaLinhasCarrinho = new ArrayList<>();


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
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPILogin(context), json,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("LoginAPI", "Raw Response: " + response.toString());
                            utilizador = LoginAndSignupJsonParser.parserJsonLogin(response);

                            // Guardar o id do utilizador, token, e username em SharedPreferences
                            saveUserPreferences(context, utilizador.getId(), utilizador.getAuth_key(), utilizador.getUsername());
                            Log.d("LoginAPI", "Utilizador Auth key: " + utilizador.getAuth_key());

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
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPISignup(context), json,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("LoginAPI", "Raw Response: " + response.toString());
                            utilizador = LoginAndSignupJsonParser.parserJsonLogin(response);

                            // Guardar o id do utilizador, token, e username em SharedPreferences
                            saveUserPreferences(context, utilizador.getId(), utilizador.getAuth_key(), utilizador.getUsername());

                            if (signupListener != null) {
                                signupListener.onUpdateSignup(utilizador);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);

                        Log.e("UtilizadorProfile:", "Response Body" + responseBody);

                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseBody);
                            JSONObject errorObj = jsonResponse.optJSONObject("error");

                            if (errorObj != null) {
                                String errorMessage = errorObj.optString("message");
                                Log.e("UtilizadorProfile:", "Error Message: " + errorMessage);
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

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
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIProdutos(context), null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("ProdutosAPI", "Raw Response: " + response.toString());
                            listaProdutos = ProdutoJsonParser.parserJsonProdutos(response);

                            // Guardar a lista de produtos na instância do Singleton
                            setListaProdutos(listaProdutos);


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


        String url = UrlAPISearchProdutos(context) + query;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("Produtos pesquisados:", "Raw Response: " + response.toString());
                searchedProdutos = ProdutoJsonParser.parserJsonProdutos(response);

                // Notificar o listener com a lista de produtos
                if (searchedProdutosListener != null) {
                    searchedProdutosListener.onSearchResults(searchedProdutos);
                    Toast.makeText(context, "Resultados para a pesquisa \"" + query + "\"", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Não foi possível encontrar resultados para a pesquisa \"" + query + "\"";

                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 404) {
                        Toast.makeText(context, "Sem resultados para a pesquisa \"" + query + "\"", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        volleyQueue.add(req);
    }

    public void getUserProfileAPI(Context context, final PerfilListener perfilListener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, UrlAPIProfile(context), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("UtilizadorProfile:", "Raw Response: " + response.toString());
                        utilizadorAndPerfil = PerfilJsonParser.parserJsonUtilizadorProfile(response);

                        //guardar o utilizador e perfil na instância do Singleton
                        setUtilizadorAndPerfil(utilizadorAndPerfil);


                        if (perfilListener != null) {
                            perfilListener.onProfileDataLoaded(utilizadorAndPerfil);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro ao carregar perfil do Utilizador" + error.toString(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                Log.d("UtilizadorProfile:", "Headers: " + headers);
                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void updateUserProfileAPI(Context context, final PerfilEditListener perfilEditListener, JSONObject profileData) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, UrlAPIProfileEdit(context), profileData,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("UtilizadorProfile:", "Raw Response: " + response.toString());


                        if (perfilEditListener != null) {
                            perfilEditListener.onUpdateProfile();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String responseBody = new String(error.networkResponse.data);

                    Log.e("UtilizadorProfile:", "Response Body" + responseBody);

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(responseBody);
                        JSONObject errorObj = jsonResponse.optJSONObject("error");

                        if (errorObj != null) {
                            String errorMessage = errorObj.optString("message");
                            Log.e("UtilizadorProfile:", "Error Message: " + errorMessage);
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));
                return headers;
            }
        };
        volleyQueue.add(req);
    }


    public void getCarrinhoAPI(Context context, final CarrinhoListener carrinhoListener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, UrlAPIgetCarrinho(context), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        carrinho = CarrinhoJsonParser.parserJsonCarrinho(response);

                        //guardar o carrinho na instância do Singleton
                        setCarrinho(carrinho);

                        if (carrinhoListener != null) {
                            carrinhoListener.onCarrinhoDataLoaded(carrinho);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("GET CARRINHO", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void createCarrinhoAPI(Context context) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("user_id", getUserId(context));
        } catch (Exception e) {
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPIcreateCarrinho(context), json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CREATE CARRINHO", "Erro ao criar carrinho: " + error.toString());
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("ERROR CREATE CARRINHO", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void getLinhasCarrinhoAPI(Context context, final LinhasCarrinhoListener linhasCarrinhoListener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIgetLinhasCarrinho(context), null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        listaLinhasCarrinho = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response);

                        Log.d("GET LINHAS CARRINHO", "Raw Response: " + listaLinhasCarrinho);
                        // Guardar a lista de linhas do carrinho na instância do Singleton
                        setListaLinhasCarrinho(listaLinhasCarrinho);

                        if (linhasCarrinhoListener != null) {
                            linhasCarrinhoListener.onListaLinhasCarrinhoLoaded(listaLinhasCarrinho);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("GET LINHAS CARRINHO", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void createlinhasCarrinhoAPI(Context context, final Carrinho carrinho, final Produto produto) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("produtos_id", produto.getId());
        } catch (Exception e) {
            Log.e("CREATE CARRINHO", "Error creating JSON: " + e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPIcreateLinhaCarrinho(context), jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("ERROR CREATE LINHA CARRINHO", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void deleteLinhasCarrinhoAPI(Context context, final LinhaCarrinho linhaCarrinho) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }

        String url = UrlAPIdeleteLinhaCarrinho(context) + linhaCarrinho.getIdLinha();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("ERROR CREATE LINHA CARRINHO", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void aumentarQuantidadeAPI(Context context, final LinhaCarrinho linhaCarrinho) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }

        String url = UrlAPIAumentarQuantidade(context) + linhaCarrinho.getIdLinha();
        Log.d("AUMENTAR QUANTIDADE", "URL: " + url);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        if (linhasCarrinhosListener != null) {
//                            linhasCarrinhosListener.onRefreshListaLinhasCarrinhos(listaLinhasCarrinho);
//                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String responseBody = new String(error.networkResponse.data);


                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(responseBody);
                        JSONObject errorObj = jsonResponse.optJSONObject("error");

                        if (errorObj != null) {
                            String errorMessage = errorObj.optString("message");
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void diminuirQuantidadeAPI(Context context, final LinhaCarrinho linhaCarrinho) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }


        String url = UrlAPIDiminuirQuantidade(context) + linhaCarrinho.getIdLinha();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DIMINUIR QUANTIDADE", "Raw Response: " + response.toString());

//                        if (linhasCarrinhosListener != null) {
//                            linhasCarrinhosListener.onRefreshListaLinhasCarrinhos(listaLinhasCarrinho);
//                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String responseBody = new String(error.networkResponse.data);

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(responseBody);
                        JSONObject errorObj = jsonResponse.optJSONObject("error");

                        if (errorObj != null) {
                            String errorMessage = errorObj.optString("message");
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void getAllPagamentosAPI(Context context, final PagamentosListener Pagamentoslistener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();

        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIgetPagamentos(context), null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("PAGAMENTOS", "Raw Response: " + response.toString());
                            listaPagamentos = PagamentosJsonParser.parserJsonPagamentos(response);

                            // Guardar a lista de produtos na instância do Singleton
                            setListaPagamentos(listaPagamentos);


                            // Notificar o listener que a lista de produtos foi atualizada
                            if (Pagamentoslistener != null) {
                                Pagamentoslistener.onPagamentosDataLoaded(listaPagamentos);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);

                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseBody);
                            JSONObject errorObj = jsonResponse.optJSONObject("error");

                            if (errorObj != null) {
                                String errorMessage = errorObj.optString("message");
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + getToken(context));

                    return headers;
                }
            };

            volleyQueue.add(req);
        }
    }

    public void getAllExpedicoesAPI(Context context, final ExpedicoesListener Expedicoeslistener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();

        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIgetExpedicoes(context), null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("EXPEDICOES", "Raw Response: " + response.toString());
                            listaExpedicoes = ExpedicoesJsonParser.parserJsonExpedicoes(response);

                            setListaExpedicoes(listaExpedicoes);

                            if (Expedicoeslistener != null) {
                                Expedicoeslistener.onExpedicoesDataLoaded(listaExpedicoes);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);

                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseBody);
                            JSONObject errorObj = jsonResponse.optJSONObject("error");

                            if (errorObj != null) {
                                String errorMessage = errorObj.optString("message");
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + getToken(context));

                    return headers;
                }
            };

            volleyQueue.add(req);
        }
    }

    public void getAllFaturasByUserIdAPI(Context context, final FaturasListener Faturaslistener) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();

        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, UrlAPIgetFaturasByUserId(context), null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("EXPEDICOES", "Raw Response: " + response.toString());
                            listaFaturas = FaturasJsonParser.parserJsonFaturas(response);

                            setListaFaturas(listaFaturas);

                            if (Faturaslistener != null) {
                                Faturaslistener.onFaturasDataLoaded(listaFaturas);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if (error.networkResponse != null) {
//                        String responseBody = new String(error.networkResponse.data);
//
//                        JSONObject jsonResponse = null;
//                        try {
//                            jsonResponse = new JSONObject(responseBody);
//                            JSONObject errorObj = jsonResponse.optJSONObject("error");
//
//                            if (errorObj != null) {
//                                String errorMessage = errorObj.optString("message");
//                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + getToken(context));

                    return headers;
                }
            };

            volleyQueue.add(req);
        }
    }

    public void getLinhasFaturasByFaturaIdAPI(Context context, final LinhasFaturasListener linhasfaturasListener, Fatura fatura) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();

        } else {

            String url = UrlAPIgetLinhasFaturasByFaturaId(context) + fatura.getId();
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("LINHAS FATURAS", "Raw Response: " + response.toString());
                            listaLinhasFaturas = LinhasFaturasJsonParser.parserJsonLinhasFaturas(response);

                            // Usar o LinhasFaturasTotaisJsonParser para obter o total das linhas das faturas
                            // Única forma Que encontrei para obter o total das linhas das faturas
                            LinhasFaturasTotais totals = LinhasFaturasTotaisJsonParser.parserJsonTotals(response);
                            float totalIvaLinhas = LinhasFaturasTotais.getTotalIva();
                            float subTotalLinhas = LinhasFaturasTotais.getSubTotalLinhas();


                            setListaLinhasFaturas(listaLinhasFaturas);

                            if (linhasfaturasListener != null) {
                                linhasfaturasListener.onListaLinhasFaturasLoaded(listaLinhasFaturas, totalIvaLinhas, subTotalLinhas);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null) {
                        String responseBody = new String(error.networkResponse.data);

                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(responseBody);
                            JSONObject errorObj = jsonResponse.optJSONObject("error");

                            if (errorObj != null) {
                                String errorMessage = errorObj.optString("message");
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + getToken(context));

                    return headers;
                }
            };

            volleyQueue.add(req);
        }
    }


    public void createFaturaAndLinhasFaturaAPI(Context context, int pagamento_id, int expedicao_id) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pagamentos_id", pagamento_id);
            jsonObject.put("expedicoes_id", expedicao_id);
        } catch (Exception e) {
            Log.e("CREATE CARRINHO", "Error creating JSON: " + e.toString());
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPIcreateFaturaAndLinhaFatura(context), jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, "Fatura criada com Suceso", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //display the http response
                if (error.networkResponse != null) {
                    Log.e("ERROR CREATE FATURA", "Response Body" + new String(error.networkResponse.data));
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getToken(context));

                return headers;
            }
        };
        volleyQueue.add(req);
    }

    public void getLatestTokenAPI(Context context, String username) {
        if (!isConnectionInternet(context)) {
            Toast.makeText(context, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, UrlAPIRefreshToken(context), json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String newToken = null;
                            int userId = 0;
                            String username = null;
                            try {
                                newToken = response.getString("latest_token");

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            saveUserToken(context, newToken);

//                                if (refreshTokenListener != null) {
//                                    refreshTokenListener.onRefreshSuccess(newToken);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                if (refreshTokenListener != null) {
//                                    refreshTokenListener.onRefreshFailed();
//                                }
//                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Erro ao atualizar o token", Toast.LENGTH_SHORT).show();
                    //display the http response
                    if (error.networkResponse != null) {
                        Log.e("ERROR REFRESH", "Response Body" + new String(error.networkResponse.data));
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

    private String UrlAPIProfile(Context context) {
        return "http://" + getApiIP(context) + "/api/user-profile/profile/" + getUserId(context);
    }

    private String UrlAPIProfileEdit(Context context) {
        return "http://" + getApiIP(context) + "/api/user-profile/update/" + getUserId(context);
    }

    private String UrlAPIgetCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/api/carrinho/cart/" + getUserId(context);
    }

    private String UrlAPIcreateCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/api/carrinho/create";
    }

    private String UrlAPIcreateLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/api/produtos-carrinho/create";
    }

    private String UrlAPIgetLinhasCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/api/produtos-carrinho/cartline/" + getUserId(context);
    }

    private String UrlAPIAumentarQuantidade(Context context) {
        return "http://" + getApiIP(context) + "/api/produtos-carrinho/increase-quantity/";
    }

    private String UrlAPIDiminuirQuantidade(Context context) {
        return "http://" + getApiIP(context) + "/api/produtos-carrinho/decrease-quantity/";
    }

    private String UrlAPIdeleteLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/api/produtos-carrinho/cartline/";
    }

    private String UrlAPIgetPagamentos(Context context) {
        return "http://" + getApiIP(context) + "/api/pagamento/all";
    }

    private String UrlAPIgetExpedicoes(Context context) {
        return "http://" + getApiIP(context) + "/api/expedicao/all";
    }

    private String UrlAPIcreateFaturaAndLinhaFatura(Context context) {
        return "http://" + getApiIP(context) + "/api/fatura/create";
    }

    private String UrlAPIgetFaturasByUserId(Context context) {
        return "http://" + getApiIP(context) + "/api/fatura/all/" + getUserId(context);
    }

    private String UrlAPIgetLinhasFaturasByFaturaId(Context context) {
        return "http://" + getApiIP(context) + "/api/linhas-fatura/linhafatura/";
    }

    private String UrlAPIRefreshToken(Context context) {
        return "http://" + getApiIP(context) + "/api/auth/refresh/token";
    }

    public void saveUserPreferences(Context context, int userId, String token, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", userId);
        editor.putString("user_token", token);
        editor.putString("username", username);
        editor.apply();
    }

    public void saveUserToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_token", token);
        editor.apply();
    }

    public Utilizador getUtilizador() {
        return this.utilizador;
    }

    public UtilizadorAndPerfil getUtilizadorAndPerfil() {
        return this.utilizadorAndPerfil;
    }

    public Carrinho getCarrinho() {
        return this.carrinho;
    }

    public ArrayList<Fatura> getListaFaturas() {
        return listaFaturas;
    }

    public ArrayList<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public ArrayList<Pagamento> getListaPagamentos() {
        return listaPagamentos;
    }

    public ArrayList<Expedicao> getListaExpedicoes() {
        return listaExpedicoes;
    }

    public ArrayList<LinhasFaturas> getListaLinhasFaturas() {
        return listaLinhasFaturas;
    }

    public ArrayList<Produto> getSearchedProdutos() {
        return searchedProdutos;
    }

    public ArrayList<LinhaCarrinho> getListaLinhasCarrinho() {
        return listaLinhasCarrinho;
    }

    // buscar o produto pelo id para adicionar na linha do carrinho
    public Produto searchProdutoByID(int id) {
        for (Produto produto : listaProdutos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    // buscar o id da fatura nas linhas faturas
    public Fatura searchFaturaToLinhasFaturas(int id) {
        for (Fatura fatura : listaFaturas) {
            if (fatura.getId() == id) {
                return fatura;
            }
        }
        return null;
    }

    public void setListaFaturas(ArrayList<Fatura> listaFaturas) {
        this.listaFaturas = listaFaturas;
    }

    public void setListaLinhasFaturas(ArrayList<LinhasFaturas> listaLinhasFaturas) {
        this.listaLinhasFaturas = listaLinhasFaturas;
    }

    public void setListaProdutos(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public void setListaPagamentos(ArrayList<Pagamento> listaPagamentos) {
        this.listaPagamentos = listaPagamentos;
    }

    public void setListaExpedicoes(ArrayList<Expedicao> listaExpedicoes) {
        this.listaExpedicoes = listaExpedicoes;
    }

    public void setSearchedProdutos(ArrayList<Produto> searchedProdutos) {
        this.searchedProdutos = searchedProdutos;
    }

    public void setListaLinhasCarrinho(ArrayList<LinhaCarrinho> listaLinhasCarrinho) {
        this.listaLinhasCarrinho = listaLinhasCarrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public void setUtilizadorAndPerfil(UtilizadorAndPerfil utilizadorAndPerfil) {
        this.utilizadorAndPerfil = utilizadorAndPerfil;
    }

    public int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getInt("user_id", 0);
    }

    public String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getString("user_token", null);
    }

    public String getApiIP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("api_url", Context.MODE_PRIVATE);
        return preferences.getString("API", null);
    }


}