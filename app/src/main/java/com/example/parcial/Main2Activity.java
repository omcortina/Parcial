package com.example.parcial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parcial.Controllers.AgregarCancionService;
import com.example.parcial.Controllers.ListarCancionesService;
import com.example.parcial.Model.Playlist;

public class Main2Activity extends AppCompatActivity {
    public static Activity my_activity;
    public static EditText txt_nombre_cancion, txt_artista_cancion;
    public Button btn_buscar, btn_guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txt_nombre_cancion = (EditText) findViewById(R.id.txt_nombre_cancion);
        txt_artista_cancion = (EditText) findViewById(R.id.txt_artista_cancion);
        my_activity = this;
        btn_buscar = (Button) findViewById(R.id.button_buscar);
        btn_guardar = (Button) findViewById(R.id.btn_guardar);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search = (EditText) findViewById(R.id.search);
                String nombre_cancion = search.getText().toString();
                AgregarCancionService service = new AgregarCancionService(Main2Activity.this, nombre_cancion);
                service.execute();
            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_cancion = txt_nombre_cancion.getText().toString();
                String artista = txt_artista_cancion.getText().toString();
                Playlist playlist =new Playlist();
                playlist.Nombre = nombre_cancion;
                playlist.Artista = artista;
                playlist.Save(Main2Activity.this);
                ListarCancionesService service = new ListarCancionesService(Main2Activity.this);
                service.execute();
                Intent inten = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(inten);
            }
        });

    }

}
