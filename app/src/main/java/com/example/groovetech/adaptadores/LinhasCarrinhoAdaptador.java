package com.example.groovetech.adaptadores;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groovetech.Modelo.Carrinho;
import com.example.groovetech.Modelo.LinhaCarrinho;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ItemCarrinhoBinding;
import com.example.groovetech.listeners.OnLinhaCarrinhoDeletedListener;
import com.example.groovetech.listeners.OnQuantidadeUpdate;

import java.util.ArrayList;


public class LinhasCarrinhoAdaptador extends RecyclerView.Adapter<LinhasCarrinhoAdaptador.ViewHolder> {

    private Context context;
    private ArrayList<LinhaCarrinho> linhasCarrinho;
    private OnLinhaCarrinhoDeletedListener listenerDelete;
    private OnQuantidadeUpdate listenerUpdate;

    private Carrinho carrinho;

    public LinhasCarrinhoAdaptador(Context context, ArrayList<LinhaCarrinho> linhasCarrinho,
                                   OnLinhaCarrinhoDeletedListener listenerDelete, OnQuantidadeUpdate listenerUpdate) {
        this.context = context;
        this.linhasCarrinho = linhasCarrinho;
        this.listenerDelete = listenerDelete;
        this.listenerUpdate = listenerUpdate;
    }

    public void updateLinhasCarrinho(ArrayList<LinhaCarrinho> newListaLinhasCarrinho) {
        this.linhasCarrinho = newListaLinhasCarrinho;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarrinhoBinding binding = ItemCarrinhoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinhaCarrinho linhaCarrinho = linhasCarrinho.get(position);

        int ProdutoID = linhaCarrinho.getProdutoID();
        Produto produto = Singleton.getInstance(context).searchProdutoToLinhaCarrinho(ProdutoID);
        if (produto == null) {
            return;
        }
        String nomeProduto = produto.getNome();
        holder.binding.tvNomeProdutoCarrinho.setText(nomeProduto);
        holder.binding.tvPrecoProdutoCarrinho.setText(linhaCarrinho.getPrecoVenda() + "€");
        holder.binding.tvQuantidadeProdutoCarrinho.setText(linhaCarrinho.getQuantidade() + "");
        String imageUrl = "http://172.22.21.211:80/images/" + produto.getImagem();
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .dontTransform()
                .fitCenter()
                .into(holder.binding.imgProdutoCarrinho);

        holder.binding.btnAumentaQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    LinhaCarrinho linhaCarrinho = linhasCarrinho.get(currentPosition);


                    Singleton.getInstance(context).aumentarQuantidadeAPI(context, linhaCarrinho);


                    notifyItemChanged(currentPosition);

                    Singleton.getInstance(context).getLinhasCarrinhoAPI(context, null);
                    Singleton.getInstance(context).getCarrinhoAPI(context, null);

                    // Notify the listener (if it's not null)
                    if (listenerUpdate != null) {
                        listenerUpdate.onItemUpdate();
                    }
                }
            }
        });


        holder.binding.btnDiminuiQtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    LinhaCarrinho linhaCarrinho = linhasCarrinho.get(currentPosition);


                    Singleton.getInstance(context).diminuirQuantidadeAPI(context, linhaCarrinho);

                    notifyItemChanged(currentPosition);

                    Singleton.getInstance(context).getLinhasCarrinhoAPI(context, null);
                    Singleton.getInstance(context).getCarrinhoAPI(context, null);

                    // Notify the listener (if it's not null)
                    if (listenerUpdate != null) {
                        listenerUpdate.onItemUpdate();
                    }
                }
            }
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            LinhaCarrinho currentLinhaCarrinho = linhasCarrinho.get(currentPosition);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Remover produto");
            builder.setMessage("Tem a certeza que deseja remover este produto do carrinho?");
            builder.setPositiveButton("Sim", (dialog, which) -> {
                Singleton.getInstance(context).deleteLinhasCarrinhoAPI(context, currentLinhaCarrinho);
                linhasCarrinho.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                Singleton.getInstance(context).getCarrinhoAPI(context, null);
//                Singleton.getInstance(context).getLinhasCarrinhoAPI(context, null);

                if (listenerDelete != null) {
                    listenerDelete.onDeletedLinhaCarrinho();
                }
            });
            builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return linhasCarrinho.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCarrinhoBinding binding;

        public ViewHolder(@NonNull ItemCarrinhoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
