package com.example.groovetech.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UtilizadorBDHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDUtilizadores";
    public static final String TABLE_NAME = "UtilizadoresTable";
    private static final int DB_VERSION = 18;
    private SQLiteDatabase db;

    public static final String ID = "id", USERNAME = "username", EMAIL = "email",
            AUTH_KEY = "auth_key", PASSWORD_HASH = "password_hash", PASSWORD_RESET_TOKEN = "password_reset_token",
            STATUS = "status", CREATED_AT = "created_at", UPDATED_AT = "updated_at", VERIFICATION_TOKEN = "verification_token";


    public UtilizadorBDHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTableUtilizadores = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERNAME + " TEXT NOT NULL," +
                EMAIL + " TEXT NOT NULL," +
                AUTH_KEY + " TEXT NOT NULL," +
                PASSWORD_HASH + " TEXT NOT NULL," +
                PASSWORD_RESET_TOKEN + " TEXT," +
                STATUS + " TEXT NOT NULL," +
                CREATED_AT + " TEXT NOT NULL," +
                UPDATED_AT + " TEXT NOT NULL," +
                VERIFICATION_TOKEN + " TEXT);";

        db.execSQL(sqlTableUtilizadores);

    }

    public Utilizador adicionarUtilizadorBD(Utilizador utilizador) {
        ContentValues values = new ContentValues();

        values.put(USERNAME, utilizador.getUsername());
        values.put(EMAIL, utilizador.getEmail());
        values.put(AUTH_KEY, utilizador.getAuth_key());
        values.put(PASSWORD_HASH, utilizador.getPassword_hash());
        values.put(PASSWORD_RESET_TOKEN, utilizador.getPassword_reset_token());
        values.put(STATUS, utilizador.getStatus());
        values.put(CREATED_AT, utilizador.getCreated_at());
        values.put(UPDATED_AT, utilizador.getUpdated_at());
        values.put(VERIFICATION_TOKEN, utilizador.getVerification_token());

        db.insert(TABLE_NAME, null, values);

        return utilizador;
    }

    public boolean editarUtilizadorBD(Utilizador utilizador) {
        ContentValues values = new ContentValues();
        values.put(USERNAME, utilizador.getUsername());
        values.put(EMAIL, utilizador.getEmail());
        values.put(AUTH_KEY, utilizador.getAuth_key());
        values.put(PASSWORD_HASH, utilizador.getPassword_hash());
        values.put(PASSWORD_RESET_TOKEN, utilizador.getPassword_reset_token());
        values.put(STATUS, utilizador.getStatus());
        values.put(CREATED_AT, utilizador.getCreated_at());
        values.put(UPDATED_AT, utilizador.getUpdated_at());
        values.put(VERIFICATION_TOKEN, utilizador.getVerification_token());

        //insert devolve o id do livro inserido, caso não seja possível inderir devolve -1
        int nLinhasEdit = (int) db.update(TABLE_NAME, values, ID + " = ?", new String[]{utilizador.getId() + ""});
        return nLinhasEdit > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getUsernameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = null;

        Cursor cursor = db.query(
                "UtilizadoresTable",
                new String[]{"username"},
                "id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("username");

                if (columnIndex != -1) {
                    username = cursor.getString(columnIndex);
                } else {
                    // Log an error or handle the situation where "username" column is not found
                    Log.d("TAG", "getUsernameById: " + "username column not found");
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return username;
    }

}
