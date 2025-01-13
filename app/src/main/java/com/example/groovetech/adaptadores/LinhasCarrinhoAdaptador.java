package com.example.groovetech.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groovetech.Modelo.Carrinho;
import com.example.groovetech.Modelo.LinhaCarrinho;
import com.example.groovetech.Modelo.Produto;
import com.example.groovetech.Modelo.Singleton;
import com.example.groovetech.databinding.ItemCarrinhoBinding;
import com.example.groovetech.listeners.LinhasCarrinhoListener;

import java.util.ArrayList;


public class LinhasCarrinhoAdaptador extends RecyclerView.Adapter<LinhasCarrinhoAdaptador.ViewHolder> {

    private Context context;
    private ArrayList<LinhaCarrinho> linhasCarrinho;
    private LinhasCarrinhoListener listener;

    private Carrinho carrinho;

    public LinhasCarrinhoAdaptador(Context context, ArrayList<LinhaCarrinho> linhasCarrinho) {
        this.context = context;
        this.linhasCarrinho = linhasCarrinho;
        this.carrinho = carrinho;
    }

    public void setOnItemClickListener(LinhasCarrinhoListener listener) {
        this.listener = listener;
    }

    // Método que atualiza a lista de produtos
    public void updateLinhasCarrinho(ArrayList<LinhaCarrinho> newLinhasCarrinho) {
        this.linhasCarrinho = newLinhasCarrinho;
        notifyDataSetChanged(); // Notificar o adaptador para atualizar o RecyclerView
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
        Log.d("Produto", produto.toString());
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

        holder.binding.btnAumentaQtd.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition(); // Ter a posição do item clicado
            if (currentPosition != RecyclerView.NO_POSITION) {
                LinhaCarrinho currentLinhaCarrinho = linhasCarrinho.get(currentPosition);

                Singleton.getInstance(context).aumentarQuantidadeAPI(context, currentLinhaCarrinho);
                notifyItemChanged(currentPosition); // Notificar que este item foi alterado
            }
        });

        holder.binding.btnDiminuiQtd.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition(); // Ter a posição do item clicado
            if (currentPosition != RecyclerView.NO_POSITION) {
                LinhaCarrinho currentLinhaCarrinho = linhasCarrinho.get(currentPosition);

                Singleton.getInstance(context).diminuirQuantidadeAPI(context, currentLinhaCarrinho);
                notifyItemChanged(currentPosition); // Notificar que este item foi alterado
            }
        });
        holder.binding.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition(); // Ter a posição do item clicado
            LinhaCarrinho currentLinhaCarrinho = linhasCarrinho.get(currentPosition);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Remover produto");
            builder.setMessage("Tem a certeza que deseja remover este produto do carrinho?");
            builder.setPositiveButton("Sim", (dialog, which) -> {
                if (listener != null) {
                    listener.onRefreshListaLinhasCarrinhos(linhasCarrinho);
                }
                Singleton.getInstance(context).deleteLinhasCarrinhoAPI(context, currentLinhaCarrinho, null);
                linhasCarrinho.remove(currentLinhaCarrinho);
                notifyItemRemoved(currentPosition); // Notificar que este item foi removido
                Singleton.getInstance(context).getLinhasCarrinhoAPI(context, null);
            });
            builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
            builder.create().show();


        });
    }

    public void setProdutos(ArrayList<LinhaCarrinho> linhasCarrinho) {
        this.linhasCarrinho = linhasCarrinho;
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
