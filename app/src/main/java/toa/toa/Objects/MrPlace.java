/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import java.util.Date;

/**
 * Created by programador on 8/24/15.
 */
public class MrPlace {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private float X;
    private float Y;
    private String zone;
    private String FB;
    private String TW;
    private String IG;
    private String weekTime;
    private String weekendTime;
    private String website;
    private Date OpenDateToday;
    private Date CloseDateToday;

    public MrPlace(int id, String name, String email, String phone, String address, float x, float y, String zone, String FB, String TW, String IG, String weekTime, String weekendTime, String website) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        X = x;
        Y = y;
        this.zone = zone;
        this.FB = FB;
        this.TW = TW;
        this.IG = IG;
        this.weekTime = weekTime;
        this.weekendTime = weekendTime;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public String getZone() {
        return zone;
    }

    public String getFB() {
        return FB;
    }

    public String getTW() {
        return TW;
    }

    public String getIG() {
        return IG;
    }

    public int getId() {
        return id;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public String getWeekendTime() {
        return weekendTime;
    }

    public String getWebsite() {
        return website;
    }

    private void setDates() {

    }
}
