/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
    private String routeImgUrl;
    private int minAge;
    private String prize;

    private float x, y, price = 0, distance = 0;
    private String cat;
    private String eventsportimg;

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
        routeImgUrl = in.readString();
        minAge = in.readInt();
        prize = in.readString();
    }

    public MrEvent(int id, String name, Date dateStart, Date dateEnd, String organizador, String descr, String address, float x, float y, String imgurl, int minAge, String prize) {
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.organizador = organizador;
        this.descr = descr;
        this.imgurl = imgurl;
        this.address = address;
        this.x = x;
        this.y = y;
        this.prize = prize;
        this.minAge = minAge;
        Instant start = new Instant(new Date());
        Instant end = new Instant(dateEnd);
        Period period = new Period(start, end, PeriodType.yearMonthDayTime());
        PeriodFormatter format = new PeriodFormatterBuilder()
                .printZeroNever().appendYears().appendSuffix(" año", " años")
                .appendSeparator(", ")
                .printZeroNever().appendMonths().appendSuffix(" mes", " meses")
                .appendSeparator(", ")
                .printZeroNever().appendDays().appendSuffix(" día", " días")
                .appendSeparator(", ")
                .printZeroNever().appendHours().appendSuffix(" hr", " hrs")
                .appendSeparator(", ")
                .printZeroRarelyLast().appendMinutes().appendSuffix(" min", " mins")
                .toFormatter();
        this.hStartDate = format.withLocale(Locale.getDefault()).print(period);

    }

    public String getRouteImgUrl() {
        return routeImgUrl;
    }

    public int getMinAge() {
        return minAge;
    }

    public String getPrize() {
        return prize;
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
        parcel.writeString(routeImgUrl);
        parcel.writeInt(minAge);
        parcel.writeString(prize);
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

    public MrEvent withEventSportImg(String eventimg) {
        this.eventsportimg = eventimg;
        return this;
    }

    public MrEvent withDistance(float distance, String routeImgUrl) {
        this.distance = distance;
        this.routeImgUrl = routeImgUrl;
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

    public String getEventsportimg() {
        return eventsportimg;
    }
}
