package com.example.parcial.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.parcial.Main2Activity;
import com.example.parcial.MainActivity;
import com.example.parcial.Model.Playlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AgregarCancionService extends AsyncTask<Void,Void,String> {
    private Context httpContext;
    ProgressDialog progressDialog;
    public boolean error=true;
    public String cancion_nombre;
    public Playlist playlist = new Playlist();

    public AgregarCancionService(Context httpContext, String cancion) {
        this.httpContext = httpContext;
        this.cancion_nombre = cancion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(httpContext, "Canciones", "buscando cancion...");
    }

    @Override
    protected String doInBackground(Void... params) {
        String uri = "http://ws.audioscrobbler.com/2.0/?method=track.search&track="+cancion_nombre+"&api_key=b284db959637031077380e7e2c6f2775&format=json";
        URL url = null;
        try {

            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            os.close();

            int responseCode=urlConnection.getResponseCode();// conexion OK?
            if(responseCode== HttpURLConnection.HTTP_OK){
                BufferedReader in= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb= new StringBuffer("");
                String linea="";
                while ((linea=in.readLine())!= null){
                    sb.append(linea);
                    break;
                }
                in.close();
                String json = "";
                json = sb.toString();
                JSONObject jo = null;
                jo = new JSONObject(json);

                JSONObject jo_canciones = null;
                jo_canciones = new JSONObject(json);
                jo_canciones = jo.getJSONObject("results");
                JSONObject jo_trackmatches = jo_canciones.getJSONObject("trackmatches");

                JSONArray array_canciones = jo_trackmatches.getJSONArray("track");
                for (int i=0; i<array_canciones.length();i++){

                    JSONObject r = array_canciones.getJSONObject(i);


                    playlist.Nombre = r.getString("name");
                    playlist.Artista = r.getString("artist");
                    return "ok";
                }

            }
            else{
                Toast.makeText(httpContext,"Ocurrio un error al procesar la solicitud",Toast.LENGTH_LONG).show();
                return "Ocurrio un error al procesar la peticion";
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error de red, revise su conexion";
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return  "";

    }

    @Override
    protected void onPostExecute(String mensaje) {
        super.onPostExecute(mensaje);
        progressDialog.dismiss();
        if (mensaje.equals("ok")){
            Main2Activity.my_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Main2Activity.txt_nombre_cancion.setText(playlist.Nombre);
                    Main2Activity.txt_artista_cancion.setText(playlist.Artista);

                }
            });
        }else{
            Toast.makeText(httpContext,mensaje , Toast.LENGTH_LONG).show();
        }

    }
}
