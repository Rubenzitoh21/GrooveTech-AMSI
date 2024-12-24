//package com.example.groovetech.adaptadores;
//
//import static androidx.core.content.ContextCompat.startActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//
//import java.util.ArrayList;
//
//import pt.ipleiria.estg.dei.books.DetalhesProdutoActivity;
//import pt.ipleiria.estg.dei.books.Modelo.FavoritosBDHelper;
//import pt.ipleiria.estg.dei.books.Modelo.Produto;
//import pt.ipleiria.estg.dei.books.Modelo.SingletonProdutos;
//import pt.ipleiria.estg.dei.books.databinding.ItemBestProdutosBinding;
//import pt.ipleiria.estg.dei.books.listeners.ProdutoListener;
//
//public class BestProdutosAdaptador extends RecyclerView.Adapter<BestProdutosAdaptador.ViewHolder> {
//
//    private ArrayList<Produto> produtos;
//
//    private ProdutoListener produtoListener;
//    private Context context;
//
//    public BestProdutosAdaptador(Context context, ArrayList<Produto> produtos) {
//        this.produtos = produtos;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ItemBestProdutosBinding binding = ItemBestProdutosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//        return new ViewHolder(binding, produtoListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Produto produto = produtos.get(position);
//        String imageUrl = "http://"+ SingletonProdutos.getInstance(context).getApiIP(context) +"/AMAI-plataformas/frontend/web/public/imagens/produtos/" + produto.getImagem();
//
//        holder.binding.nomeTxt.setText(produto.getNome());
//        holder.binding.precoTxt.setText(produto.getPreco() + "€");
//
//
//        Glide.with(holder.itemView.getContext())
//                .load(imageUrl)
//                .transform(new CenterCrop(), new RoundedCorners(30))
//                .into(holder.binding.pic);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Notify the listener about the item click and pass the position
//                Intent intent = new Intent(context, DetalhesProdutoActivity.class);
//                intent.putExtra(DetalhesProdutoActivity.PRODUTO, produto);
//
//                // Check if the product is in the Favoritos table for the current user
//                int userID = SingletonProdutos.getInstance(context).getUserId(context);
//                FavoritosBDHelper dbHelper = new FavoritosBDHelper(context);
//
//                boolean isProdutoInFavorites = dbHelper.isProdutoInFavorites(userID, produto.getId());
//                dbHelper.close();
//
//                // Pass the information to the details activity
//                intent.putExtra(DetalhesProdutoActivity.IS_FAVORITE, isProdutoInFavorites);
//
//                startActivity(context,intent,null);
//            }
//        });
//    }
//    public void setProdutos(ArrayList<Produto> produtos) {
//        this.produtos = produtos;
//    }
//
//    @Override
//    public int getItemCount() {
//        return produtos.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private final ItemBestProdutosBinding binding;
//
//        public ViewHolder(@NonNull ItemBestProdutosBinding binding,ProdutoListener produtoListener) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
//}
