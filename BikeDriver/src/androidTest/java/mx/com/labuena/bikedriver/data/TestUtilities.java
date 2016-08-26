package mx.com.labuena.bikedriver.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

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
        ContentValues values = new ContentValues();
        values.put(BikeDriverContracts.OrderEntry.ID, orderId);
        values.put(BikeDriverContracts.OrderEntry.CLIENT_EMAIL_COLUMN, "morales.fernandez.clemente@gmail.com");
        values.put(BikeDriverContracts.OrderEntry.CLIENT_NAME_COLUMN, "Clemente Morales Fernandez");
        values.put(BikeDriverContracts.OrderEntry.QUANTITY_COLUMN, 18);
        values.put(BikeDriverContracts.OrderEntry.LATITUDE_COLUMN, 72.8765);
        values.put(BikeDriverContracts.OrderEntry.LONGITUDE_COLUMN, 26.4537);
        return values;
    }

    /**
     * Class to test the changes occurred in a block of data.
     */
    public static class TestContentObserver extends ContentObserver {
        /**
         * Helper class to create the Thread with a looper.
         */
        private final HandlerThread handlerThread;

        /**
         * Indicates that the content has change.
         */
        private boolean contentChanged;

        /**
         * Creates a content observer.
         *
         * @param handlerThread The handler to run {@link #onChange} on, or null if none.
         */
        public TestContentObserver(HandlerThread handlerThread) {
            super(new Handler(handlerThread.getLooper()));
            this.handlerThread = handlerThread;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            contentChanged = true;
        }

        public void waitForNotificationOrFail() {
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return contentChanged;
                }
            }.run();
            handlerThread.quit();
        }
    }

    /**
     * Allows to get a content observer.
     * @return Content observer.
     */
    public static TestContentObserver getTestContentObserver() {
        HandlerThread handlerThread = new HandlerThread("ContentObserverThread");
        handlerThread.start();
        return new TestContentObserver(handlerThread);
    }
}
