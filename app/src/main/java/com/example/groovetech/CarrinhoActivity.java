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
    public void onRefreshListaLinhasCarrinhos(ArrayList<LinhaCarrinho> linhasCarrinho) {
        // Configura o RecyclerView para mostrar as linhas do carrinho
        binding.rvListaLinhaCarrinho.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        // Define o adaptador para a lista de linhas do carrinho
        LinhasCarrinhoAdaptador adapter = new LinhasCarrinhoAdaptador(getApplicationContext(), linhasCarrinho);
        binding.rvListaLinhaCarrinho.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);

        updateCartValues(linhasCarrinho);

    }

    private void updateCartValues(ArrayList<LinhaCarrinho> linhasCarrinho) {
        float subTotal = 0;
        float totalIva = 0;

        // Loop through the list to calculate subTotal and totalIva
        for (LinhaCarrinho linhaCarrinho : linhasCarrinho) {
            subTotal += linhaCarrinho.getPrecoVenda();  // Add each item's price to subTotal
            totalIva += linhaCarrinho.getValorIva();    // Add each item's IVA to totalIva
        }

        // Calculate the total
        float total = subTotal + totalIva;

        // Update the UI with the new values
        binding.tvValorIVA.setText(totalIva + "€");
        binding.tvValorTotal.setText(total + "€");
        binding.tvValorSubtotal.setText(subTotal + "€");
    }

    @Override
    public void onCarrinhoDataLoaded() {

        // Converter o valor total do carrinho que é string para float
        String valorTotalText = binding.tvValorTotal.getText().toString(); // Get the text as a String
        float valorTotal = 0.0f;

        try {
            valorTotal = Float.parseFloat(valorTotalText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // Esconder o botão de finalizar encomenda se o valor total for 0
        if (valorTotal == 0.0f) {
            binding.btnFinalizarEncomenda.setVisibility(View.INVISIBLE);
        } else {
            binding.btnFinalizarEncomenda.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemUpdate() {

    }

    @Override
    public void onItemDelete(ArrayList<LinhaCarrinho> linhasCarrinho, LinhaCarrinho linhaCarrinho) {

    }


}
