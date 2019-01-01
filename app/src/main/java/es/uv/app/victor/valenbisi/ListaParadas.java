package es.uv.app.victor.valenbisi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import es.uv.app.victor.valenbisi.ClasesAux.Coordinates;
import es.uv.app.victor.valenbisi.ClasesAux.Geometria;
import es.uv.app.victor.valenbisi.ClasesAux.Parada;

public class ListaParadas extends AppCompatActivity {
    private AdapterParadas ap;
    private ArrayList paradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new HTTPConnector().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_paradas_layout);
    }

    protected void crearListView() {
        ap = new AdapterParadas(getApplicationContext(), paradas);

        final ListView listaParadas = (ListView) findViewById(R.id.lista_paradas);
        listaParadas.setAdapter(ap);

        listaParadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent (view.getContext(), InformacionParada.class);
                i.putExtra("parada", (Parada) listaParadas.getItemAtPosition(position));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        crearListView();
    }

    class HTTPConnector extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            paradas = new ArrayList<Parada>();

            String url = "http://mapas.valencia.es/lanzadera/opendata/Valenbisi/JSON";
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),
                        "UTF-8"));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            parseJSONValenbisi(writer);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            // Crear la ListView
            crearListView();
        }

        private void parseJSONValenbisi (Writer writer) {

            try {
                JSONObject primero = new JSONObject(writer.toString());
                JSONArray padre = primero.getJSONArray("features");

                for (int i = 0; i < padre.length(); ++i){
                    JSONObject paradaJSON = padre.getJSONObject(i);
                    JSONObject propiedadParada = paradaJSON.getJSONObject("properties");

                    String name = propiedadParada.getString("name");
                    int number = propiedadParada.getInt("number");
                    String address = propiedadParada.getString("address");
                    String open =  propiedadParada.getString("open");
                    int available = propiedadParada.optInt("available", 0);
                    int free = propiedadParada.optInt("free" ,0);
                    int total = propiedadParada.optInt("total",0);
                    String ticket = propiedadParada.getString("ticket");
                    long lastUpdate = propiedadParada.optLong("updated_at", 0);

                    JSONObject geometriaParada = paradaJSON.getJSONObject("geometry");
                    String type = geometriaParada.getString("type");

                    JSONArray coordinatesJSON = geometriaParada.getJSONArray("coordinates");
                    float longitude = (float)coordinatesJSON.getDouble(0);
                    float latitude = (float)coordinatesJSON.getDouble(1);

                    Coordinates coordinates = new Coordinates(longitude, latitude);
                    Geometria geometry = new Geometria(type, coordinates);

                    Parada parada = new Parada(name, number, address, open.charAt(0), available, free, total, ticket.charAt(0), lastUpdate, geometry);
                    paradas.add(parada);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}