package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by gbern on 9/23/2015.
 */
public class List extends ArrayList<MrCommunity> implements Parcelable {
    public static final Creator<List> CREATOR = new Creator<List>() {
        @Override
        public List createFromParcel(Parcel in) {
            return new List(in);
        }

        @Override
        public List[] newArray(int size) {
            return new List[size];
        }
    };

    public List() {

    }

    protected List(Parcel in) {
        this();
        this.clear();

        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            MrCommunity r = new MrCommunity(in.readString(), in.readString(), in.readString(), in.readString());
            this.add(r);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int f) {
        int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            MrCommunity r = this.get(i);

            dest.writeString(r.getComunityName());
            dest.writeString(r.getComunityImg());
            dest.writeString(r.getComunityBack());
            dest.writeString(r.getComunityImgAlt());
        }

    }
}
