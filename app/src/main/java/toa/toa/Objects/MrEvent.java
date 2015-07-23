/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.Objects;

import java.util.Date;

/**
 * Created by Guillermo on 7/21/2015.
 */
public class MrEvent {

    private int id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private String organizador;
    private String address;
    private String descr;
    private float x, y, price = 0, distance = 0;
    private String cat;

    public MrEvent(int id, String name, Date dateStart, Date dateEnd, String organizador, String descr, String address, float x, float y) {
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.organizador = organizador;
        this.descr = descr;
        this.address = address;
        this.x = x;
        this.y = y;
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
