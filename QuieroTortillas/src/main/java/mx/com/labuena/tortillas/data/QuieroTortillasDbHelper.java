package mx.com.labuena.tortillas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class QuieroTortillasDbHelper extends SQLiteOpenHelper {
    public QuieroTortillasDbHelper(Context context) {
        super(context, QuieroTortillasContract.DATABASE_NAME, null, QuieroTortillasContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ORDER_TABLE = "CREATE TABLE " + QuieroTortillasContract.OrderEntry.TABLE_NAME + " (" +
                QuieroTortillasContract.OrderEntry.ID + " INTEGER PRIMARY KEY," +
                QuieroTortillasContract.OrderEntry.CLIENT_EMAIL_COLUMN + " TEXT NOT NULL," +
                QuieroTortillasContract.OrderEntry.BIKER_NAME_COLUMN + " TEXT NULL," +
                QuieroTortillasContract.OrderEntry.CREATED_AT_COLUMN + " INTEGER NOT NULL," +
                QuieroTortillasContract.OrderEntry.UPDATED_AT_COLUMN + " INTEGER NOT NULL," +
                QuieroTortillasContract.OrderEntry.LATITUDE_COLUMN + " REAL NOT NULL," +
                QuieroTortillasContract.OrderEntry.LONGITUDE_COLUMN + " REAL NOT NULL," +
                QuieroTortillasContract.OrderEntry.QUANTITY_COLUMN + " TEXT NULL);";

        db.execSQL(SQL_CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuieroTortillasContract.OrderEntry.TABLE_NAME);
        onCreate(db);
    }
}
