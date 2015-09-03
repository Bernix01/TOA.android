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
                        Contract.PeopleColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Contract.PeopleColumns.FIRST_NAME + " TEXT NOT NULL, " +
                        Contract.PeopleColumns.SECOND_NAME + " TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public interface Tables {
        String Events = "events";
    }
}

