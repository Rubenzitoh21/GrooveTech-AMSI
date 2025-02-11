package com.example.groovetech.adaptadores;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.groovetech.DetalhesProdutoActivity;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.databinding.ItemHomeProdutosBinding;
import com.example.groovetech.listeners.HomeProdutosListener;

import java.util.ArrayList;


public class HomeProdutosAdaptador extends RecyclerView.Adapter<HomeProdutosAdaptador.ViewHolder> {

    private ArrayList<Produto> produtos;

    private HomeProdutosListener produtosListener;
    private Context context;

    public HomeProdutosAdaptador(Context context, ArrayList<Produto> produtos) {
        this.produtos = produtos;
        this.context = context;
    }
    // Método que atualiza a lista de produtos
    public void updateProdutos(ArrayList<Produto> newListaProdutos) {
        this.produtos = newListaProdutos;
        notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeProdutosBinding binding = ItemHomeProdutosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, produtosListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto produto = produtos.get(position);
        String imageUrl = "http://172.22.21.211:80/images/" + produto.getImagem();

        holder.binding.nomeTxt.setText(produto.getNome());
        holder.binding.precoTxt.setText(String.format("%.2f€", produto.getPreco()));

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .dontTransform()
                .fitCenter()
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetalhesProdutoActivity.class);
                intent.putExtra(DetalhesProdutoActivity.PRODUTO, produto);
                startActivity(context, intent, null);
            }
        });
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemHomeProdutosBinding binding;

        public ViewHolder(@NonNull ItemHomeProdutosBinding binding, HomeProdutosListener produtosListener) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
