package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creado por : lawliet
 * Cecha: 17/06/2015.
 * Proyecto: Toa.
 * Hora: 18:33.
 */
public class MrComunity implements Parcelable {

    public static final Creator<MrComunity> CREATOR = new Creator<MrComunity>() {
        @Override
        public MrComunity createFromParcel(Parcel in) {
            return new MrComunity(in);
        }

        @Override
        public MrComunity[] newArray(int size) {
            return new MrComunity[size];
        }
    };
    private String comunityName;
    private String comunityImg;
    private String comunityBack;

    public MrComunity(String comunityName, String comunityImg, String comunityBack) {
        this.comunityName = comunityName;
        this.comunityImg = comunityImg;
        this.comunityBack = comunityBack;
    }

    protected MrComunity(Parcel in) {
        comunityName = in.readString();
        comunityImg = in.readString();
        comunityBack = in.readString();
    }

    public String getComunityName() {
        return comunityName;
    }

    public void setComunityName(String comunityName) {
        this.comunityName = comunityName;
    }

    public String getComunityImg() {
        return comunityImg;
    }

    public void setComunityImg(String comunityImg) {
        this.comunityImg = comunityImg;
    }

    public String getComunityBack() {
        return comunityBack;
    }

    public void setComunityBack(String comunityBack) {
        this.comunityBack = comunityBack;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comunityName);
        dest.writeString(comunityImg);
        dest.writeString(comunityBack);
    }
}
