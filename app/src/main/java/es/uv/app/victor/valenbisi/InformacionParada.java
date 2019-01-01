package es.uv.app.victor.valenbisi;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import es.uv.app.victor.valenbisi.ClasesAux.Parada;

public class InformacionParada extends AppCompatActivity {

    private Parada parada;

    private TextView number;
    private TextView address;
    private TextView total;
    private TextView availableBicis;
    private TextView freeEspacios;
    private TextView coordinates;

    private ImageButton mapa_boton;
    private ImageButton editar_boton;

    AdapterPartesCursor partesAdapter;

    static class ViewHolder {
        int idParte;
        int idParada;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion_parada_layout);

        //Dar valores a los TextView
        inicializarDatos();
        //Crear OnClickListener para botones
        inicializarBotones();
        //Dar valores lista partes
        inicializarPartes();

    }

    private void inicializarPartes() {

        PartesDbHelper db = new PartesDbHelper(getApplicationContext());

        Cursor partesByParada = db.obtenerPartePorParada(parada.number);
        partesAdapter = new AdapterPartesCursor(getApplicationContext(), partesByParada);
        ListView lv=(ListView) findViewById(R.id.lista_partes);
        lv.setAdapter(partesAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), ListaPartes.class);
                i.putExtra("id", (int)view.getTag(R.id.idParte));
                i.putExtra("paradaId", (int)view.getTag(R.id.idParada));
                startActivity(i);
            }
        });
}


    private void inicializarDatos() {

        inicializarParada();

        setTitle(parada.name);

        number = (TextView)findViewById(R.id.number);
        number.setText(""+parada.number);

        address = (TextView)findViewById(R.id.address);
        address.setText(""+parada.address);


        total = (TextView)findViewById(R.id.total);
        total.setText(""+parada.total);

        availableBicis = (TextView)findViewById(R.id.availableBicis);
        availableBicis.setText(""+parada.availableBicis);

        freeEspacios = (TextView)findViewById(R.id.freeEspacios);
        freeEspacios.setText(""+parada.freeEspacios);

        coordinates = (TextView)findViewById(R.id.coordinates);
        coordinates.setText(""+parada.geometria.coordinates.toString());
    }

    private void inicializarParada() {
        Intent i = this.getIntent();

        parada = i.getParcelableExtra("parada");
    }

    private void inicializarBotones() {

        //Boton Mapa
        mapa_boton = (ImageButton) findViewById(R.id.mapa_boton);
        mapa_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri gmmIntentUri;
                gmmIntentUri = Uri.parse("geo:0,0?q=" + parada.geometria.coordinates.latitude +","+ parada.geometria.coordinates.longitude +"("+parada.name+")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                //Si no está instalada la aplicación, se lanza la actividad de Google Maps
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent); }
            }
        });


        //Listener Boton Editar
        editar_boton = (ImageButton) findViewById(R.id.editar_boton);
        editar_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ListaPartes.class);
                i.putExtra("paradaId", parada.number);
                startActivity(i);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        View v = findViewById(R.id.parent);
        inicializarPartes();
        v.invalidate();
    }
}
