package com.example.groovetech;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ActivityDefesaBinding;
import com.example.groovetech.databinding.ActivityMainBinding;
import com.example.groovetech.listeners.HomeProdutosListener;

import java.util.ArrayList;

public class DefesaActivity extends AppCompatActivity implements HomeProdutosListener {

    private ActivityDefesaBinding binding;
    private ArrayList<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDefesaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Singleton.getInstance(getApplicationContext()).getAllProdutosAPI(this, this);


    }

    @Override
    public void onRefreshHomeProdutos(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = Singleton.getInstance(getApplicationContext()).getListaProdutos();

        double maxPrice = 0;
        String productName = "";
        for (Produto produto : listaProdutos) {
            if (produto.getPreco() > maxPrice) {
                maxPrice = produto.getPreco();
                productName = produto.getNome();
            }
            binding.textView2.setText("O produto mais caro é: " + productName + " com o preço de: " + maxPrice);
        }

    }
}