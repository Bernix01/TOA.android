package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillermo on 6/6/2015.
 */
public class MrSport implements Parcelable {
    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MrSport createFromParcel(Parcel in) {
            return new MrSport(in);
        }

        public MrSport[] newArray(int size) {
            return new MrSport[size];
        }
    };
    String name;
    int img;
    Boolean isChecked = false;

    public MrSport(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public MrSport(Parcel in) {
        this.name = in.readString();
        this.img = in.readInt();
        this.isChecked = (in.readInt() != 0);
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
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
        dest.writeString(name);
        dest.writeInt(img);
        dest.writeInt(getIsChecked() ? 1 : 0);
    }
}
