/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Guillermo on 7/21/2015.
 */
public class MrEvent implements Parcelable {

    public static final Creator<MrEvent> CREATOR = new Creator<MrEvent>() {
        @Override
        public MrEvent createFromParcel(Parcel in) {
            return new MrEvent(in);
        }

        @Override
        public MrEvent[] newArray(int size) {
            return new MrEvent[size];
        }
    };
    public String hEndDate;
    private String hStartDate;
    private int id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private String imgurl;
    private String organizador;
    private String address;
    private String descr;
    private float x, y, price = 0, distance = 0;
    private String cat;

    protected MrEvent(Parcel in) {
        hEndDate = in.readString();
        hStartDate = in.readString();
        id = in.readInt();
        name = in.readString();
        organizador = in.readString();
        address = in.readString();
        descr = in.readString();
        x = in.readFloat();
        y = in.readFloat();
        price = in.readFloat();
        distance = in.readFloat();
        cat = in.readString();
        dateStart = (Date) in.readSerializable();
        dateEnd = (Date) in.readSerializable();
        imgurl = in.readString();
    }

    public MrEvent(int id, String name, Date dateStart, Date dateEnd, String organizador, String descr, String address, float x, float y, String imgurl) {
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.organizador = organizador;
        this.descr = descr;

        this.address = address;
        this.x = x;
        this.y = y;
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM/yy HH:mm ", Locale.getDefault());
        hStartDate = format2.format(this.dateStart);
        hEndDate = format2.format(this.dateEnd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hEndDate);
        parcel.writeString(hStartDate);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(organizador);
        parcel.writeString(address);
        parcel.writeString(descr);
        parcel.writeFloat(x);
        parcel.writeFloat(y);
        parcel.writeFloat(price);
        parcel.writeFloat(distance);
        parcel.writeString(cat);
        parcel.writeSerializable(dateStart);
        parcel.writeSerializable(dateEnd);
        parcel.writeString(imgurl);
    }

    public String getImgurl() {
        return imgurl;
    }

    public String gethEndDate() {
        return hEndDate;
    }

    public MrEvent withPrice(float price) {
        this.price = price;
        return this;
    }

    public MrEvent withDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public MrEvent withCategory(String cat) {
        this.cat = cat;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public float getPrice() {
        return price;
    }

    public float getDistance() {
        return distance;
    }

    public String getCat() {
        return cat;
    }

    public String gethStartDate() {
        return hStartDate;
    }

    public String getName() {
        return name;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public String getOrganizador() {
        return organizador;
    }

    public String getDescr() {
        return descr;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
