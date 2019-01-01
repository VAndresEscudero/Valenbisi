package es.uv.app.victor.valenbisi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class ListaPartes extends AppCompatActivity {

    private int id;
    private int paradaId;

    private EditText tituloParte;
    private EditText descripcionParte;
    private Spinner estadoSpninner;
    private Spinner tipoSpinner;

    private ImageView aceptar;
    private ImageView borrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partes_layout);

        initVariables();
        initListeners();

        setSpinner(estadoSpninner, R.array.estado_array);
        setSpinner(tipoSpinner, R.array.tipo_array);

        Intent i = this.getIntent();
        id = i.getIntExtra("id", -1);
        paradaId = i.getIntExtra("paradaId", -1);

        if (id != -1) {
            editarParte(id);
        }
        else
            crearParte();

    }

    private void initVariables () {
        tituloParte = (EditText) findViewById(R.id.titulo_parte);
        descripcionParte = (EditText) findViewById(R.id.descripcion_parte);
        estadoSpninner = (Spinner) findViewById(R.id.estado_spinner);
        tipoSpinner = (Spinner) findViewById(R.id.tipo_spinner);
        aceptar = (ImageView) findViewById(R.id.aceptar);
        borrar = (ImageView) findViewById(R.id.borrar);
    }

    private void initListeners () {
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartesDbHelper db = new PartesDbHelper(getApplicationContext());

                if (id != -1) {
                    db.ActualizarParte(id, tituloParte.getText().toString(), descripcionParte.getText().toString(), paradaId, estadoSpninner.getSelectedItem().toString(), tipoSpinner.getSelectedItem().toString());
                }
                else {
                    db.InsertarParte(tituloParte.getText().toString(), descripcionParte.getText().toString(), paradaId, estadoSpninner.getSelectedItem().toString(), tipoSpinner.getSelectedItem().toString());
                }

                db.close();
                onBackPressed();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartesDbHelper db = new PartesDbHelper(getApplicationContext());

                if (id != -1) {
                    db.borrarParte(id);
                }

                onBackPressed();
                db.close();
            }
        });
    }

    private void setSpinner (Spinner spinner, int valoresId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,valoresId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void crearParte() {

        PartesDbHelper db = new PartesDbHelper(getApplicationContext());
        int id = db.nextId();

        tituloParte.setText("Parte"+id);
        descripcionParte.setHint("Descripción del parte...");
        db.close();
    }

    private void editarParte(int id) {
        PartesDbHelper db = new PartesDbHelper(getApplicationContext());
        Cursor cursor = db.obtenerPartePorId(id);
        cursor.moveToFirst();

        tituloParte.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
        descripcionParte.setText(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));

        String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
        estadoSpninner.setSelection(((ArrayAdapter)estadoSpninner.getAdapter()).getPosition(estado));

        String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
        tipoSpinner.setSelection(((ArrayAdapter)tipoSpinner.getAdapter()).getPosition(tipo));

        db.close();
    }
}