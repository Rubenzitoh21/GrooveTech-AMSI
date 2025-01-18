package com.example.groovetech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.groovetech.Modelo.Expedicao;
import com.example.groovetech.Modelo.Fatura;
import com.example.groovetech.Modelo.LinhasFaturas;
import com.example.groovetech.Modelo.Pagamento;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.adaptadores.LinhasFaturasAdaptador;
import com.example.groovetech.databinding.ActivityFaturaBinding;
import com.example.groovetech.listeners.ExpedicoesListener;
import com.example.groovetech.listeners.LinhasFaturasListener;
import com.example.groovetech.listeners.PagamentosListener;

import java.util.ArrayList;

public class FaturaActivity extends AppCompatActivity implements LinhasFaturasListener, ExpedicoesListener, PagamentosListener {

    private ActivityFaturaBinding binding;
    private LinhasFaturasAdaptador adapter;
    private Fatura fatura;
    private String metodoExpedicao, metodoPagamento;
    private boolean isExpedicaoLoaded = false, isPagamentoLoaded = false;
    private float totalIvaLinhas, subTotalLinhas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaturaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura  o botão de voltar
        binding.backButton.setOnClickListener(v -> onBackPressed());

        // Inicializa as chamadas à API para obter expedicoes e pagamentos
        Singleton.getInstance(getApplicationContext()).getAllExpedicoesAPI(this, this);
        Singleton.getInstance(getApplicationContext()).getAllPagamentosAPI(this, this);

        // Obtém o ID da fatura a partir da Intent e pesquisa a fatura correspondente
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("faturaID")) {
            int idFatura = intent.getIntExtra("faturaID", 0);
            this.fatura = Singleton.getInstance(getApplicationContext()).searchFaturaToLinhasFaturas(idFatura);
        }

        // Carrega os dados das LinhasFaturas da fatura
        Singleton.getInstance(getApplicationContext()).getLinhasFaturasByFaturaIdAPI(getApplicationContext(), this, fatura);
    }

    private void fillLinhaFaturaUI() {
        // Define os valores da fatura
        binding.FaturaidTxt.setText(String.format("Fatura# %d", fatura.getId()));
        binding.DataFaturacaoValueTxt.setText(fatura.getData());
        binding.TotalValueTxt.setText(String.format("%.2f €", fatura.getValorTotal()));
        binding.SubTotalValueTxt.setText(String.format("%.2f €", subTotalLinhas));
        binding.IvaTotalValueTxt.setText(String.format("%.2f €", totalIvaLinhas));

        // Define os valores dos métodos de expedição e pagamento
        binding.MetodoExpedicaoValueTxt.setText(metodoExpedicao != null ? metodoExpedicao : "Não Definido");
        binding.MetodoPagamentoValueTxt.setText(metodoPagamento != null ? metodoPagamento : "Não Definido");
    }

    @Override
    public void onListaLinhasFaturasLoaded(ArrayList<LinhasFaturas> linhasFaturas, float totalIva, float subTotalLinhas) {
        this.totalIvaLinhas = totalIva;
        this.subTotalLinhas = subTotalLinhas;

        binding.recyclerViewLinhasFatura.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new LinhasFaturasAdaptador(this, linhasFaturas);
        binding.recyclerViewLinhasFatura.setAdapter(adapter);

        // Verifica e atualiza a UI quando os dados estão prontos
        updateUI();
    }

    @Override
    public void onExpedicoesDataLoaded(ArrayList<Expedicao> listaExpedicoes) {
        // Encontra e define o método de expedição com base no ID
        for (Expedicao expedicao : listaExpedicoes) {
            if (expedicao.getId() == fatura.getExpedicoesID()) {
                metodoExpedicao = expedicao.getMetodoExpedicao();
                break;
            }
        }
        isExpedicaoLoaded = true;
        updateUI();
    }

    @Override
    public void onPagamentosDataLoaded(ArrayList<Pagamento> listaPagamentos) {
        // Encontra e define o método de pagamento com base no ID
        for (Pagamento pagamento : listaPagamentos) {
            if (pagamento.getId() == fatura.getPagamentosID()) {
                metodoPagamento = pagamento.getMetodoPagamento();
                break;
            }
        }
        isPagamentoLoaded = true;
        updateUI();
    }

    private void updateUI() {
        // Atualiza a UI apenas quando os dados de expedição e pagamento estão carregados
        if (isExpedicaoLoaded && isPagamentoLoaded) {
            fillLinhaFaturaUI();
        }
    }
}
