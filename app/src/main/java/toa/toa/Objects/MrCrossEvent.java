package toa.toa.Objects;

import java.util.Date;

/**
 * Created by Guillermo on 7/21/2015.
 */
public class MrCrossEvent extends MrEvent {
    private String cat;

    public MrCrossEvent(int id, String name, Date dateStart, Date dateEnd, String organizador, String descr, float x, float y, String cat) {
        super(id, name, dateStart, dateEnd, organizador, descr, x, y);

    }

    public MrCrossEvent withCategory(String cat) {
        this.cat = cat;
        return this;
    }
}
