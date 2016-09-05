package mx.com.labuena.tortillas.data;

import android.content.ContentValues;
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
        mContext.deleteDatabase(QuieroTortillasContract.DATABASE_NAME);
    }

    public void testCreateDataBase() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(QuieroTortillasContract.OrderEntry.TABLE_NAME);

        SQLiteDatabase sqliteDatabase = new QuieroTortillasDbHelper(mContext).getWritableDatabase();
        assertTrue("An exception has occurred opening the database " + QuieroTortillasContract.DATABASE_NAME,
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

    public void testOrderTableInsertion() {
        SQLiteDatabase database = new QuieroTortillasDbHelper(mContext).getWritableDatabase();
        ContentValues contentValues = TestUtilities.createOrderDummyValues();
        long id = database.insert(QuieroTortillasContract.OrderEntry.TABLE_NAME, null, contentValues);

        Cursor movieCursor = database.query(QuieroTortillasContract.OrderEntry.TABLE_NAME, null, null,
                null, null, null, null);
        assertTrue("No records return from order query", movieCursor.moveToFirst());
        TestUtilities.validateCurrentRecord("The query doesn't return the espected values and columns " +
                "for the order ", movieCursor, contentValues);
        assertFalse(movieCursor.moveToNext());

        movieCursor.close();
        database.close();
    }
}
