package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.databinding.ActivityDetalhesProdutoBinding;

public class DetalhesProdutoActivity extends AppCompatActivity {


    final public static String PRODUTO = "PRODUTO";
    private ActivityDetalhesProdutoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetalhesProdutoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(PRODUTO)) {
            Produto produto = intent.getParcelableExtra(PRODUTO);

            binding.nomeTxt.setText(produto.getNome());
            binding.precoTxt.setText(produto.getPreco() + "€");
            binding.tvDescricaoProduto.setText(produto.getDescricao());
            String imageUrl = "http://172.22.21.211:80/images/" + produto.getImagem();

            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .dontTransform()
                    .fitCenter()
                    .into(binding.pic);
        }
    }
}
