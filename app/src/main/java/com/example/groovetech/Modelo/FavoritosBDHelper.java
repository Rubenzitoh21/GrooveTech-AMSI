package com.example.groovetech.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FavoritosBDHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDFavoritos";
    public static final String TABLE_NAME = "Favoritos";

    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    private static final String UTILIZADOR_ID = "utilizador_id", ID = "ID", PRODUTO_ID = "produto_id", ESTADO =
            "estado", NOME_PRODUTO =
            "nome_produto", PRECO_PRODUTO = "preco_produto", CATEGORIA_PRODUTO = "categoria_produto", IMAGEM_PRODUTO
            = "imagem_produto";


    public FavoritosBDHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTableFavoritos =
                "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRODUTO_ID + " " +
                        "INTEGER," + NOME_PRODUTO + " TEXT,"
                        + PRECO_PRODUTO + " FLOAT," + CATEGORIA_PRODUTO + " TEXT," + IMAGEM_PRODUTO + " TEXT," + UTILIZADOR_ID + " INTEGER" + "); ";
        db.execSQL(sqlTableFavoritos);
    }

    public Favoritos adicionarFavoritosBD(Favoritos favoritos) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //produtos
        values.put(PRODUTO_ID, favoritos.getIdProduto());
        values.put(NOME_PRODUTO, favoritos.getNomeProduto());
        values.put(PRECO_PRODUTO, favoritos.getPrecoProduto());
        values.put(CATEGORIA_PRODUTO, favoritos.getCategoriaProduto());
        values.put(IMAGEM_PRODUTO, favoritos.getImagemProduto());

        //favoritos
        values.put(UTILIZADOR_ID, favoritos.getId_user());


        db.insert(TABLE_NAME, null, values);
        db.close();

        return favoritos;
    }

    // recebe a lista de favoritos da base de dados
    public List<Favoritos> getAllFavoritos(int utilizador_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Favoritos> favoritosList = new ArrayList<>();

        String selection = UTILIZADOR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(utilizador_id)};
        Cursor cursor = db.query("Favoritos", null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                int produtoId = cursor.getInt(cursor.getColumnIndexOrThrow(PRODUTO_ID));
                String nomeProduto = cursor.getString(cursor.getColumnIndexOrThrow(NOME_PRODUTO));
                int utilizadorId = cursor.getInt(cursor.getColumnIndexOrThrow(UTILIZADOR_ID));
                float precoProduto = cursor.getFloat(cursor.getColumnIndexOrThrow(PRECO_PRODUTO));
                String categoriaProduto = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORIA_PRODUTO));
                String imagemProduto = cursor.getString(cursor.getColumnIndexOrThrow(IMAGEM_PRODUTO));

                Favoritos favoritos = new Favoritos(utilizadorId, produtoId, nomeProduto, categoriaProduto,
                        imagemProduto, precoProduto);
                favoritosList.add(favoritos);
            }

            cursor.close();
        }

        db.close();
        return favoritosList;
    }

    public void removerEstadoFavoritosBD(int user_id, int produto_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, UTILIZADOR_ID + " = ? AND " + PRODUTO_ID + " = ?", new String[]{String.valueOf(user_id)
                , String.valueOf(produto_id)});
        db.close();
    }

    public boolean isProdutoInFavorites(int utilizador_id, int produto_id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {ID};
        String selection = UTILIZADOR_ID + " = ? AND " + PRODUTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(utilizador_id), String.valueOf(produto_id)};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        boolean produtoInFavorites = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return produtoInFavorites;
    }

    //limpar a tabela dos favoritos
    public void removerTodosFavoritosBD() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);  // Deletes all rows in the table
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
