package com.example.groovetech;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ActivityProvaoraliplBinding;
import com.example.groovetech.listeners.HomeProdutosListener;

import java.util.ArrayList;

public class ProvaOralIPL extends AppCompatActivity implements HomeProdutosListener {

    private ActivityProvaoraliplBinding binding;
    private ArrayList<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProvaoraliplBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Android title to 2220848 Amaral
        setTitle("2220848 Amaral");
        Singleton.getInstance(getApplicationContext()).getAllProdutosAPI(this, this);


    }

    @Override
    public void onRefreshHomeProdutos(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = Singleton.getInstance(getApplicationContext()).getListaProdutos();

        binding.buttonipl.setOnClickListener(v -> {
            // get the lowest price produto from lista produtos
            float minPrice = Float.MAX_VALUE;
            String productName = "";
            for (Produto produto : listaProdutos) {
                if (produto.getPreco() < minPrice) {
                    minPrice = produto.getPreco();
                    productName = produto.getNome();
                }
            }
            binding.textView2.setText("Existem " + listaProdutos.size() + " produtos. O produto mais barato custa " + minPrice + "€");


        });
    }


}
