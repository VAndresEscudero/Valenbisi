package es.uv.app.victor.valenbisi.ClasesAux;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinates implements Parcelable {

    public float longitude;
    public float latitude;

    public Coordinates(float longitude, float latitude) {
        String result = "30 S ";
        result += String.valueOf((int)longitude) + " ";
        result += String.valueOf((int)latitude);
        double[] coord = new CoordinateConversion().utm2LatLon(result);

        this.longitude = (float)coord[1];
        this.latitude = (float)coord[0];
    }

    public Coordinates (String coord) {
        String[] parts = coord.split(", ");

        this.longitude = Float.parseFloat(parts[0]);
        this.latitude = Float.parseFloat(parts[1]);
    }

    public String toParse () {
        //34 G 683473 4942631
        String result = "30 S ";
        result += String.valueOf((int)longitude) + " ";
        result += String.valueOf((int)latitude);

        return result;
    }




    /**************************************/
    /* PARCELABLE LOGIC                   */
    /**************************************/


    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };

    protected Coordinates(Parcel in) {
        longitude = in.readFloat();
        latitude = in.readFloat();
    }

    public String toString() {
        return (Float.toString(longitude) + ", " + Float.toString(latitude));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
    }
}
