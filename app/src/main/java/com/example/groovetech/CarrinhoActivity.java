package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.groovetech.Modelo.Carrinho;
import com.example.groovetech.Modelo.LinhaCarrinho;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.adaptadores.LinhasCarrinhoAdaptador;
import com.example.groovetech.databinding.ActivityCarrinhoBinding;
import com.example.groovetech.listeners.CarrinhoListener;
import com.example.groovetech.listeners.OnLinhaCarrinhoDeletedListener;
import com.example.groovetech.listeners.LinhasCarrinhoListener;
import com.example.groovetech.listeners.OnQuantidadeUpdate;

import java.util.ArrayList;

public class CarrinhoActivity extends AppCompatActivity implements LinhasCarrinhoListener, CarrinhoListener,
        OnLinhaCarrinhoDeletedListener, OnQuantidadeUpdate {
    private ActivityCarrinhoBinding binding;
    private ArrayList<LinhaCarrinho> linhasCarrinho;
    private LinhasCarrinhoAdaptador adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarrinhoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.backButton.setOnClickListener(v -> onBackPressed());
        Singleton.getInstance(getApplicationContext()).getLinhasCarrinhoAPI(getApplicationContext(), this);
        Singleton.getInstance(getApplicationContext()).getCarrinhoAPI(this, this);


        binding.btnFinalizarEncomenda.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onListaLinhasCarrinhoLoaded(ArrayList<LinhaCarrinho> linhasCarrinho) {
        this.linhasCarrinho = linhasCarrinho;

        if(linhasCarrinho.size() != 0){
            binding.btnFinalizarEncomenda.setEnabled(true);


        }
        // Configura o RecyclerView para mostrar as linhas do carrinho
        binding.rvListaLinhaCarrinho.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        // Define o adaptador para a lista de linhas do carrinho
        adapter = new LinhasCarrinhoAdaptador(getApplicationContext(), linhasCarrinho, this, this);
        binding.rvListaLinhaCarrinho.setAdapter(adapter);


        binding.progressBar.setVisibility(View.GONE);
    }


    public void carrinhoUpdateUI(Carrinho carrinho) {
        binding.tvValorTotal.setText(String.format("%.2f€", carrinho.getValorTotal()));
        binding.tvValorSubtotal.setText(String.format("%.2f€", carrinho.getValorSubtotal()));
        binding.tvValorIVA.setText(String.format("%.2f€", carrinho.getValorTotalIVA()));
    }

    @Override
    public void onCarrinhoDataLoaded(Carrinho carrinho) {

        carrinhoUpdateUI(carrinho);
        if (carrinho.getValorTotal() == 0) {
            binding.btnFinalizarEncomenda.setVisibility(View.GONE);
            Toast.makeText(this, "O carrinho está vazio", Toast.LENGTH_SHORT).show();
        } else {
            binding.btnFinalizarEncomenda.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onDeletedLinhaCarrinho() {
        Singleton.getInstance(getApplicationContext()).getCarrinhoAPI(this, this);
        Carrinho carrinho = Singleton.getInstance(getApplicationContext()).getCarrinho();
        carrinhoUpdateUI(carrinho);
    }


    @Override
    public void onItemUpdate() {
        Singleton.getInstance(getApplicationContext()).getLinhasCarrinhoAPI(this, this);
        Singleton.getInstance(getApplicationContext()).getCarrinhoAPI(this, this);
        linhasCarrinho = Singleton.getInstance(getApplicationContext()).getListaLinhasCarrinho();
        adapter.updateLinhasCarrinho(linhasCarrinho);
        Carrinho carrinho = Singleton.getInstance(getApplicationContext()).getCarrinho();
        carrinhoUpdateUI(carrinho);

    }
}


