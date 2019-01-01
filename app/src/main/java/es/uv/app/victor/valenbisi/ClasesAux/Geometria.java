package es.uv.app.victor.valenbisi.ClasesAux;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometria implements Parcelable {

    public String type;
    public Coordinates coordinates;

    public Geometria(String type, Coordinates coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public Geometria (Coordinates coordinates) {
        this.type = "Vacio";
        this.coordinates = coordinates;
    }



    /**************************************/
    /* PARCELABLE LOGIC                   */
    /**************************************/

    protected Geometria(Parcel in) {
        type = in.readString();
        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public static final Creator<Geometria> CREATOR = new Creator<Geometria>() {
        @Override
        public Geometria createFromParcel(Parcel in) {
            return new Geometria(in);
        }

        @Override
        public Geometria[] newArray(int size) {
            return new Geometria[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeParcelable(coordinates, flags);
    }
}