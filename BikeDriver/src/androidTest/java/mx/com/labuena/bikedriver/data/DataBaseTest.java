package mx.com.labuena.bikedriver.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by clerks on 8/26/16.
 */

public class DataBaseTest extends AndroidTestCase {
    public void setUp() {
        deleteDatabase();
    }

    private void deleteDatabase() {
        mContext.deleteDatabase(BikeDriverContracts.DATABASE_NAME);
    }

    public void testCreateDataBase() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(BikeDriverContracts.OrderEntry.TABLE_NAME);

        SQLiteDatabase sqliteDatabase = new BikeDriverDbHelper(mContext).getWritableDatabase();
        assertTrue("An exception has occurred opening the database " + BikeDriverContracts.DATABASE_NAME,
                sqliteDatabase.isOpen());

        Cursor cursor = sqliteDatabase.rawQuery("select name from sqlite_master where type='table'", null);
        assertTrue("The database has not been created correctly", cursor.moveToFirst());

        while (cursor.moveToNext()) {
            String table = cursor.getString(0);
            if (tableNameHashSet.contains(table))
                tableNameHashSet.remove(table);
        }

        assertTrue("The database is not loading all the tables", tableNameHashSet.isEmpty());

        cursor.close();
        sqliteDatabase.close();
    }
}
