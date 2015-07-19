package toa.toa.Objects;

/**
 * Creado por : lawliet
 * Cecha: 17/06/2015.
 * Proyecto: Toa.
 * Hora: 18:33.
 */
public class MrComunity {

    private String comunityName;
    private String comunityImg;
    private String comunityBack;

    public MrComunity(String comunityName, String comunityImg, String comunityBack) {
        this.comunityName = comunityName;
        this.comunityImg = comunityImg;
        this.comunityBack = comunityBack;
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
}
