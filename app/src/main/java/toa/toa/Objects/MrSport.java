package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creado por : lawliet
 * Cecha: 17/06/2015.
 * Proyecto: Toa.
 * Hora: 18:33.
 */
public class MrSport implements Parcelable {

    public static final Creator<MrSport> CREATOR = new Creator<MrSport>() {
        @Override
        public MrSport createFromParcel(Parcel in) {
            return new MrSport(in);
        }

        @Override
        public MrSport[] newArray(int size) {
            return new MrSport[size];
        }
    };
    private String comunityName;
    private String comunityImg;
    private String comunityBack;
    private String comunityImgAlt;
    private Boolean isChecked = false;

    public MrSport(String comunityName, String comunityImg, String comunityBack, String comunityImgAlt) {
        this.comunityName = comunityName;
        this.comunityImg = comunityImg;
        this.comunityBack = comunityBack;
        this.comunityImgAlt = comunityImgAlt;
    }

    private MrSport(Parcel in) {
        comunityName = in.readString();
        comunityImg = in.readString();
        comunityBack = in.readString();
        this.isChecked = (in.readInt() != 0);
    }

    public String getComunityName() {
        return comunityName;
    }


    public String getComunityImg() {
        return comunityImg;
    }


    public String getComunityBack() {
        return comunityBack;
    }

    public String getComunityImgAlt() {
        return comunityImgAlt;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public MrSport setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
        return this;
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
        dest.writeInt(getIsChecked() ? 1 : 0);
    }
}
