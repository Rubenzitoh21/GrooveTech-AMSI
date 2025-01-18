package com.example.groovetech.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groovetech.Modelo.Fatura;
import com.example.groovetech.databinding.ItemFaturaBinding;
import com.example.groovetech.listeners.FaturaListener;

import java.util.ArrayList;


public class ListaFaturasAdaptador extends RecyclerView.Adapter<ListaFaturasAdaptador.ViewHolder> {

    public Context context;
    private ArrayList<Fatura> faturas;
    private FaturaListener faturaListener;

    //setup me the adapter
    public ListaFaturasAdaptador(Context context, ArrayList<Fatura> faturas, FaturaListener faturalistener) {
        this.context = context;
        this.faturas = faturas;
        this.faturaListener = faturalistener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaturaBinding binding = ItemFaturaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fatura fatura = faturas.get(position);
        holder.binding.tvNomeFatura.setText("Fatura: " + fatura.getId());
        holder.binding.tvDataFatura.setText(fatura.getData());
        holder.binding.tvValorFatura.setText(String.format("%.2f €", fatura.getValorTotal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faturaListener != null) {
                    faturaListener.onFaturaClick(position, fatura);


                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return faturas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFaturaBinding binding;


        public ViewHolder(@NonNull View itemView, ItemFaturaBinding binding) {
            super(itemView);
            this.binding = binding;
        }


    }
}
