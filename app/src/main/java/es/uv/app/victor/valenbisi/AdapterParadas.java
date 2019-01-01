package es.uv.app.victor.valenbisi;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.uv.app.victor.valenbisi.ClasesAux.Parada;

public class AdapterParadas extends BaseAdapter {

    private ArrayList<Parada> paradas;

    Context context;

    static class ViewHolder {
        TextView number;
        TextView address;
        TextView partes;
    }

    public AdapterParadas(Context c, ArrayList paradas) {
        context = c;
        Init(paradas);
    }

    private void Init(ArrayList paradas) {

        //Leemos el fichero JSON y rellenamos el ArrayList paradas
        this.paradas = paradas;

    }


    @Override
    public int getCount() {
        return paradas.size();
    }

    @Override
    public Object getItem(int position) {
        return paradas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return paradas.get(position).number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Usamos el patrón del ViewHolder para almacenar las vistas de cada
        // elemento de la lista para mostrarlos más rápido al desplazar la lista
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            // Si es null la creamos a partir de layout
            LayoutInflater li = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

            v = li.inflate(R.layout.adapter_paradas_layout, null);
            holder = new ViewHolder();
            holder.number = (TextView) v.findViewById(R.id.paradaviewnumber);
            holder.address = (TextView) v.findViewById(R.id.paradaviewaddress);
            holder.partes = (TextView) v.findViewById(R.id.paradaviewpartes);
            v.setTag(holder);
        } else {
            // Si no es null la reutilizamos para actualizarla
            holder = (ViewHolder) v.getTag();
        }

        //Rellenar el holder con la información de la parada que está en la posicion position del ArrayList
        Parada parada = paradas.get(position);
        holder.number.setText(String.valueOf(parada.number));
        holder.address.setText(String.valueOf(parada.address));

        //Añadir tercer elemento al item de la lista.
        PartesDbHelper db = new PartesDbHelper(context);
        Cursor cursor = db.obtenerPartePorParada(parada.number);
        holder.partes.setText(""+cursor.getCount());
        cursor.close();
        db.close();


        return v;
    }

}