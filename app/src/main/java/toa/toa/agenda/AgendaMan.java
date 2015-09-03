/*
 * Copyright TOA Inc. 2015.
 */

package toa.toa.agenda;

/**
 * Created by gbern on 8/13/2015.
 */
public class AgendaMan {

    public static void saveEvent(int id) {

    }


    public interface AgendaCols {
        String _ID = "_id_";
        String NAME = "name";
        String DATESTART = "dates";
        String DATEEND = "datee";
        String RELID = "rel_id";
        String ORGANIZADOR = "organizer";
        String ADDRESS = "address";
        String DESCRIPTION = "descr";
        String X = "x",
                Y = "y",
                PRICE = "price",
                DISTANCE = "distance";
        String CATEGORY = "cat";
    }
    
}
