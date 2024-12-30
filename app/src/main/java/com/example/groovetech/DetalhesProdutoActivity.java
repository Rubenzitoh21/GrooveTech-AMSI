package com.example.groovetech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.groovetech.Modelo.Favoritos;
import com.example.groovetech.Modelo.FavoritosBDHelper;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.databinding.ActivityDetalhesProdutoBinding;

public class DetalhesProdutoActivity extends AppCompatActivity {


    final public static String PRODUTO = "PRODUTO";
    private ActivityDetalhesProdutoBinding binding;
    private boolean isFavorite = false;
    private FavoritosBDHelper dbHelper;

    private int produtoID;
    private int userID;


    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicialização do View Binding
        binding = ActivityDetalhesProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        dbHelper = new FavoritosBDHelper(this);

        loadProdutoDetails();
        setupUI();
        checkAndUpdateFavoriteState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close(); // Fecha a ligação à base de dados
        }
    }

    private void setupUI() {
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.favBtn.setOnClickListener(v -> {
            toggleFavoriteState();
            updateFavoriteButton();
        });

    }

    private void loadProdutoDetails() {
        Produto produto = getProdutoFromIntent();
        if (produto != null) {
            binding.nomeTxt.setText(produto.getNome());
            binding.precoTxt.setText(produto.getPreco() + "€");
            binding.tvDescricaoProduto.setText(produto.getDescricao());

            String imageUrl = "http://172.22.21.211:80/images/" + produto.getImagem();
            Glide.with(this)
                    .load(imageUrl)
                    .dontTransform()
                    .fitCenter()
                    .into(binding.pic);

            produtoID = produto.getId();
        }
    }


    private Produto getProdutoFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PRODUTO)) {
            return intent.getParcelableExtra(PRODUTO);
        }
        return null;
    }

    private void addProdutoFavorites(Produto produto) {
        dbHelper.adicionarFavoritosBD(new Favoritos(userID, produto.getId(), produto.getNome(), produto.getCategoria(),
                produto.getImagem(), produto.getPreco()));
        Toast.makeText(this, "Produto adicionado aos favoritos", Toast.LENGTH_SHORT).show();
    }

    private void removeProdutoFromFavorites() {
        dbHelper.removerEstadoFavoritosBD(userID, produtoID);
        Toast.makeText(this, "Produto removido dos favoritos", Toast.LENGTH_SHORT).show();
    }

    private void toggleFavoriteState() {
        Produto produto = getProdutoFromIntent();
        if (produto != null) {
            isFavorite = !isFavorite;
            if (isFavorite) {
                addProdutoFavorites(produto);
            } else {
                removeProdutoFromFavorites();
            }
        }
    }

    private void updateFavoriteButton() {
        int imageResource = isFavorite ? R.drawable.favorite_fill : R.drawable.favorite_unfilled;
        binding.favBtn.setImageResource(imageResource);
    }

    private void checkAndUpdateFavoriteState() {
        // Recebe o id do produto
        Produto produto = getProdutoFromIntent();
        produtoID = (produto != null) ? produto.getId() : 0;

        // Recebe o id do user
        userID = getUserId();

        // Verfica se o produto está nos favoritos para o utilizador atual
        isFavorite = dbHelper.isProdutoInFavorites(userID, produtoID);

        // Atualiza o botão de favoritos
        updateFavoriteButton();
    }

    private int getUserId() {
        return preferences.getInt("user_id", 0);
    }

}
