package mx.com.labuena.bikedriver.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikeDriverDbHelper extends SQLiteOpenHelper {
    public BikeDriverDbHelper(Context context) {
        super(context, BikeDriverContracts.DATABASE_NAME, null, BikeDriverContracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ORDER_TABLE = "CREATE TABLE " + BikeDriverContracts.OrderEntry.TABLE_NAME + " (" +
                BikeDriverContracts.OrderEntry.ID + " INTEGER PRIMARY KEY," +
                BikeDriverContracts.OrderEntry.CLIENT_EMAIL_COLUMN + " TEXT NOT NULL," +
                BikeDriverContracts.OrderEntry.CLIENT_NAME_COLUMN + " TEXT NOT NULL," +
                BikeDriverContracts.OrderEntry.LATITUDE_COLUMN + " REAL NOT NULL," +
                BikeDriverContracts.OrderEntry.LONGITUDE_COLUMN + " REAL NOT NULL," +
                BikeDriverContracts.OrderEntry.QUANTITY_COLUMN + " TEXT NULL);";

        db.execSQL(SQL_CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BikeDriverContracts.OrderEntry.TABLE_NAME);
        onCreate(db);
    }
}
