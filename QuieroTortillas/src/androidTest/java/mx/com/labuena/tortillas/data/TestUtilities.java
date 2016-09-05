package mx.com.labuena.tortillas.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by clerks on 8/26/16.
 */

public class TestUtilities {
    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /**
     * Allows to create a dummy value to insert for an order.
     * @return Dummy value to insert for an order.
     */
    public static ContentValues createOrderDummyValues() {
        return createDummyOrderValues(135397);
    }

    public static ContentValues[] createBulkDummyInsert(int numberOfEntries) {
        ContentValues[] values = new ContentValues[numberOfEntries];

        int orderId = 23;
        for (int i = 0; i < 5; i++) {
            values[i] = createDummyOrderValues(++orderId);
        }

        return values;
    }

    private static ContentValues createDummyOrderValues(int orderId) {
        Date date = new Date(System.currentTimeMillis());

        ContentValues values = new ContentValues();
        values.put(QuieroTortillasContract.OrderEntry.ID, orderId);
        values.put(QuieroTortillasContract.OrderEntry.CLIENT_EMAIL_COLUMN, "morales.fernandez.clemente@gmail.com");
        values.put(QuieroTortillasContract.OrderEntry.BIKER_NAME_COLUMN, "Ramon Garcia Sanchez");
        values.put(QuieroTortillasContract.OrderEntry.CREATED_AT_COLUMN, date.getTime());
        values.put(QuieroTortillasContract.OrderEntry.UPDATED_AT_COLUMN, date.getTime());
        values.put(QuieroTortillasContract.OrderEntry.QUANTITY_COLUMN, 18);
        values.put(QuieroTortillasContract.OrderEntry.LATITUDE_COLUMN, 72.8765);
        values.put(QuieroTortillasContract.OrderEntry.LONGITUDE_COLUMN, 26.4537);
        return values;
    }
}
