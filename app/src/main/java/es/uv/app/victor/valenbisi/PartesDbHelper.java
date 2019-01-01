package es.uv.app.victor.valenbisi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PartesDbHelper extends SQLiteOpenHelper {


    public PartesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PartesDbHelper(Context context) {
        super(context, "PARTES_DB", null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Partes (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, descripcion TEXT, parada INTEGER, estado TEXT, tipo TEXT)");
    }

    public void InsertarParte (String nombre, String descripcion, int parada, String estado, String tipo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(6);
        int id = nextId();

        values.put("_id",id);
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("parada", parada);
        values.put("estado", estado);
        values.put("tipo",tipo);

        db.insert("Partes", null, values);
    }

    public void ActualizarParte (int id, String nombre, String descripcion, int parada, String estado, String tipo){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(6);
        values.put("_id",id);
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("parada", parada);
        values.put("estado", estado);
        values.put("tipo",tipo);


        db.update("Partes", values, "_id="+id, null);
    }

    public void borrarParte (int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "_id=?";
        String [] whereArgs = new String[] {String.valueOf(id)};

        db.delete("Partes", whereClause, whereArgs );
    }

    public Cursor obtenerPartePorParada (int parada){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = new String[] {"*"};
        String whereClause = "parada=?";
        String [] whereArgs = new String[] {String.valueOf(parada)};

        Cursor c = db.query("Partes", columnas, whereClause, whereArgs, null, null,null);

        return c;
    }

    public Cursor obtenerPartePorId (int id){

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = new String[] {"*"};
        String whereClause = "_id=?";
        String [] whereArgs = new String[] {String.valueOf(id)};

        Cursor c = db.query("Partes", columnas, whereClause, whereArgs, null, null,null);

        return c;
    }

    public int nextId () {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;

        Cursor c = db.rawQuery("SELECT MAX(_id) FROM Partes",null);
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }

        c.close();

        return ++id;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Partes");
        onCreate(db);
    }
}
