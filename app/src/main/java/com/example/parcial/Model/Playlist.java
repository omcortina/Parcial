package com.example.parcial.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parcial.BD;
import com.example.parcial.Config.Config;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    public int Id;
    public String Nombre;
    public String Artista;

    public Playlist() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }


    public void Save(Context context){
        BD admin = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre_cancion", this.Nombre);
        registro.put("artista", this.Artista);
        db.insert("Playlist", null, registro);
        db.close();
    }

    public Playlist Find(Context context, int id){
        BD bd = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        String sql = "select * from Playlist where id_playlist="+id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            this.Id = Integer.parseInt(cursor.getString(0));
            this.Nombre = cursor.getString(1);
            this.Artista = cursor.getString(2);
            return this;
        }
        db.close();
        return null;
    }

    public static List<Playlist> FindAll(Context context){
        BD bd = new BD(context, Config.database_name, null, 1);
        SQLiteDatabase db = bd.getWritableDatabase();
        List<Playlist> lista = new ArrayList<>();

        String sql = "select * from Playlist";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()){
            do{
                Playlist playlist = new Playlist();
                playlist.Id = Integer.parseInt(cursor.getString(0));
                playlist.Nombre = cursor.getString(1);
                playlist.Artista = cursor.getString(2);
                lista.add(playlist);
            }while(cursor.moveToNext());
            db.close();
            return lista;
        }
        db.close();
        return null;
    }
}
