package es.uv.app.victor.valenbisi;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdapterPartesCursor extends CursorAdapter {

    Context context;

    private void Init (Context ctx) {
        context = ctx;
    }

    public AdapterPartesCursor(Context context, Cursor c) {
        super(context, c, false);
    }

    public AdapterPartesCursor(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        Init(context);
    }

    public AdapterPartesCursor(Context context, Cursor c, int flags) {
        super(context, c, flags);
        Init(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_partes_layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tituloParte = (TextView) view.findViewById(R.id.titulo_parte);
        tituloParte.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int idParada = cursor.getInt(cursor.getColumnIndexOrThrow("parada"));
        view.setTag(R.id.idParte, id);
        view.setTag(R.id.idParada, idParada);
    }
}
