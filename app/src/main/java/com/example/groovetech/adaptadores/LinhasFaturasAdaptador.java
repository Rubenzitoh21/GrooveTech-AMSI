package com.example.groovetech.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.groovetech.Modelo.LinhasFaturas;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ItemLinhasFaturasBinding;

import java.util.List;


public class LinhasFaturasAdaptador extends RecyclerView.Adapter<LinhasFaturasAdaptador.ViewHolder> {

    private List<LinhasFaturas> linhasFaturas;
    private Context context;

    public LinhasFaturasAdaptador(Context context, List<LinhasFaturas> linhasFaturas) {
        this.context = context;
        this.linhasFaturas = linhasFaturas;
    }

    public void setLinhasFaturas(List<LinhasFaturas> linhasFaturas) {
        this.linhasFaturas = linhasFaturas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLinhasFaturasBinding binding = ItemLinhasFaturasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinhasFaturas linha = linhasFaturas.get(position);
        Produto produto = Singleton.getInstance(context).searchProdutoByID(linha.getProdutos_id());

        holder.binding.tvNome.setText(produto.getNome());
        holder.binding.tvPreco.setText(linha.getPreco_venda() + " €");
        holder.binding.tvQuantidade.setText(String.valueOf(linha.getQuantidade()));
        holder.binding.tvSubTotal.setText(linha.getSubtotal() + " €");
    }

    @Override
    public int getItemCount() {
        return linhasFaturas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemLinhasFaturasBinding binding;

        public ViewHolder(View itemView, ItemLinhasFaturasBinding binding) {
            super(itemView);
            this.binding = binding;

        }
    }
}
