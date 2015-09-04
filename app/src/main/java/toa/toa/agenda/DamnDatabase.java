package toa.toa.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gbern on 9/3/2015.
 */
public class DamnDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "agendaEvents_db";
    private static final int DB_VERSION = 1;

    public DamnDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Tables.Events + " (" +
                        AgendaMan.AgendaCols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AgendaMan.AgendaCols.NAME + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.RELID + " INTEGER NOT NULL, )" +
                        AgendaMan.AgendaCols.DATESTART + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.DATEEND + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.ADDRESS + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.DESCRIPTION + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.CATEGORY + " TEXT, " +
                        AgendaMan.AgendaCols.ORGANIZADOR + " TEXT NOT NULL, " +
                        AgendaMan.AgendaCols.X + " FLOAT NOT NULL, " +
                        AgendaMan.AgendaCols.Y + " FLOAT NOT NULL, " +
                        AgendaMan.AgendaCols.DISTANCE + " FLOAT, " +
                        AgendaMan.AgendaCols.PRICE + " TEXT"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public interface Tables {
        String Events = "events";
    }
}

