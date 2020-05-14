package com.example.parcial.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.parcial.BD;
import com.example.parcial.Config.Config;
import com.example.parcial.MainActivity;
import com.example.parcial.Model.Playlist;
import com.example.parcial.Recyclers.RecyclerCanciones;
import com.example.parcial.Routes.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ListarCancionesService extends AsyncTask<Void,Void,String> {
    Context context;
    ProgressDialog progressDialog;
    Boolean error;

    public ListarCancionesService(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.context,"Canciones","Validando informacion...");
    }

    protected String doInBackground(Void... voids) {

        String uri = Routes.listar_canciones;
        URL url = null;
        try {
            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            int response_code = urlConnection.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String linea = "";
                while ((linea = br.readLine()) != null){
                    sb.append(linea);
                    break;
                }
                br.close();

                String json = "";
                json = sb.toString();

                JSONObject jo = null;
                jo = new JSONObject(json);

                JSONObject json_canciones = null;
                BD admin = new BD(context, Config.database_name, null, 1);
                SQLiteDatabase db = admin.getWritableDatabase();

                db.execSQL("DELETE FROM Playlist");
                db.close();

                json_canciones = new JSONObject(json);
                json_canciones = jo.getJSONObject("tracks");

                JSONArray array_canciones = json_canciones.getJSONArray("track");

                for (int i=0; i<array_canciones.length();i++){
                    JSONObject r = array_canciones.getJSONObject(i);
                    Playlist playlist = new Playlist();
                    playlist.Nombre = r.getString("name");
                    JSONObject jo_artista = r.getJSONObject("artist");
                    playlist.Artista = jo_artista.getString("name");
                    playlist.Save(this.context);
                }
                this.error = false;
                return "se guardo todo";
            }

        } catch (MalformedURLException e) {
            error = true;
            return "Error de ruta: "+e.getMessage();
        } catch (IOException e) {
            error = true;
            return "Error de conexion a la ruta: "+e.getMessage();
        } catch (JSONException e) {
            error = true;
            return "Error con los parametros del json de envio: "+e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String respuesta) {
        super.onPostExecute(respuesta);
        progressDialog.dismiss();
        if (respuesta.equals("se guardo todo")){
            MainActivity.activity_main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<Playlist> lista = Playlist.FindAll(context);
                    RecyclerCanciones adapter = new RecyclerCanciones(lista, context);
                    MainActivity.recyclerView.setAdapter(adapter);
                }
            });

        }else{
            Toast.makeText(context, "No se caragron las canciones", Toast.LENGTH_SHORT).show();
        }
    }

}
