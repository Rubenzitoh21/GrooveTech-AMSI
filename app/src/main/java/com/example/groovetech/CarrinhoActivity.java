package com.example.groovetech;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.groovetech.Modelo.Carrinho;
import com.example.groovetech.Modelo.LinhaCarrinho;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.adaptadores.LinhasCarrinhoAdaptador;
import com.example.groovetech.databinding.ActivityCarrinhoBinding;
import com.example.groovetech.listeners.CarrinhoListener;
import com.example.groovetech.listeners.LinhasCarrinhoListener;

import java.util.ArrayList;

public class CarrinhoActivity extends AppCompatActivity implements LinhasCarrinhoListener, CarrinhoListener {
    private ActivityCarrinhoBinding binding;
    private Carrinho carrinho;
    private ArrayList<LinhaCarrinho> linhasCarrinho;
    private LinhasCarrinhoAdaptador adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCarrinhoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> onBackPressed());
        Singleton.getInstance(getApplicationContext()).getLinhasCarrinhoAPI(getApplicationContext(), this);
        Singleton.getInstance(getApplicationContext()).getCarrinhoAPI(this, this);
        carrinho = Singleton.getInstance(getApplicationContext()).getCarrinho();

        adapter = new LinhasCarrinhoAdaptador(getApplicationContext(), null);
        adapter.setOnItemClickListener(this);

    }


    @Override
    public void onListaLinhasCarrinhoLoaded(ArrayList<LinhaCarrinho> linhasCarrinho) {
        // Configura o RecyclerView para mostrar as linhas do carrinho
        binding.rvListaLinhaCarrinho.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        // Define o adaptador para a lista de linhas do carrinho
        LinhasCarrinhoAdaptador adapter = new LinhasCarrinhoAdaptador(getApplicationContext(), linhasCarrinho);
        binding.rvListaLinhaCarrinho.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onCarrinhoDataLoaded(Carrinho carrinho) {

        binding.tvValorTotal.setText(String.format("%.2f€", carrinho.getValorTotal()));
        binding.tvValorSubtotal.setText(String.format("%.2f€", carrinho.getValorTotal()));
        binding.tvValorIVA.setText(String.format("%.2f€", carrinho.getValorTotalIVA()));

        if (carrinho.getValorTotal() == 0) {
            binding.btnFinalizarEncomenda.setVisibility(View.GONE);
        } else {
            binding.btnFinalizarEncomenda.setVisibility(View.VISIBLE);
        }

    }

}
