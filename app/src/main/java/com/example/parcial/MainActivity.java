package com.example.parcial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.parcial.Controllers.ListarCancionesService;
import com.example.parcial.Model.Playlist;
import com.example.parcial.Recyclers.RecyclerCanciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static Activity activity_main;
    private RecyclerCanciones adapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.f);
        recyclerView = (RecyclerView) findViewById(R.id.id_recycler_playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activity_main = this;
        ListarCancionesService servicio = new ListarCancionesService(MainActivity.this);
        servicio.execute();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(inten);
            }
        });
    }
}
