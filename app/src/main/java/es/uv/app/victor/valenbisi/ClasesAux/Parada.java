package es.uv.app.victor.valenbisi.ClasesAux;

import android.os.Parcel;
import android.os.Parcelable;

public class Parada implements Parcelable {

    public String name;
    public int number;
    public String address;
    public char isOpen; //T o F
    public int availableBicis;
    public int freeEspacios;
    public int total;
    public char ticket;
    public long lastUpdate;
    public Geometria geometria;

    public Parada(String name, int number, String address, char isOpen, int availableBicis, int freeEspacios, int total, char ticket, long lastUpdate, Geometria geometria) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.isOpen = isOpen;
        this.availableBicis = availableBicis;
        this.freeEspacios = freeEspacios;
        this.total = total;
        this.ticket = ticket;
        this.lastUpdate = lastUpdate;
        this.geometria = geometria;
    }



    /**************************************/
    /* PARCELABLE LOGIC                   */
    /**************************************/


    protected Parada(Parcel in) {
        name = in.readString();
        number = in.readInt();
        address = in.readString();
        availableBicis = in.readInt();
        freeEspacios = in.readInt();
        total = in.readInt();
        lastUpdate = in.readLong();
        geometria = in.readParcelable(Geometria.class.getClassLoader());
    }

    public static final Creator<Parada> CREATOR = new Creator<Parada>() {
        @Override
        public Parada createFromParcel(Parcel in) {
            return new Parada(in);
        }

        @Override
        public Parada[] newArray(int size) {
            return new Parada[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(number);
        dest.writeString(address);
        dest.writeInt(availableBicis);
        dest.writeInt(freeEspacios);
        dest.writeInt(total);
        dest.writeLong(lastUpdate);
        dest.writeParcelable(geometria, flags);
    }
}



