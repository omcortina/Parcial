package com.example.parcial.Recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial.Model.Playlist;
import com.example.parcial.R;

import java.util.List;

public class RecyclerCanciones extends RecyclerView.Adapter<RecyclerCanciones.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_nombre, txt_artista, txt_album, txt_duracion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre = (TextView) itemView.findViewById(R.id.txt_nombre);
            txt_artista = (TextView) itemView.findViewById(R.id.txt_artista);
        }
    }

    public List<Playlist> listaPlaylist;
    public Context context;

    public RecyclerCanciones(List<Playlist> listaPlaylist, Context context) {
        this.listaPlaylist = listaPlaylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancion,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = listaPlaylist.get(position);
        holder.txt_nombre.setText(playlist.getNombre());
        holder.txt_artista.setText(playlist.getArtista());
    }

    @Override
    public int getItemCount() {
        return listaPlaylist.size();
    }
}
