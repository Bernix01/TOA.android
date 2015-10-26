package toa.toa.Objects;

/**
 * Created by gbern on 10/23/2015.
 */
public class MrCommunity {
    private String name;
    private String zone;
    private String motto;
    private int ID;

    public MrCommunity(int ID, String name, String zone, String motto) {
        this.name = name;
        this.zone = zone;
        this.motto = motto;
        this.ID = ID;
    }

    public MrCommunity() {
    }

    public String getName() {
        return name;
    }

    public String getZone() {
        return zone;
    }

    public String getMotto() {
        return motto;
    }
}
