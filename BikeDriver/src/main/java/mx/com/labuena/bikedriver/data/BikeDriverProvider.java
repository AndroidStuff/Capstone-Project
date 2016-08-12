package mx.com.labuena.bikedriver.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by moracl6 on 8/12/2016.
 */

public class BikeDriverProvider extends ContentProvider {
    public static final int ORDERS = 100;

    public static final int ORDER_BY_ID = 101;

    private  BikeDriverDbHelper bikeDriverDbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        bikeDriverDbHelper = new BikeDriverDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result;
        final int match = buildUriMatcher().match(uri);
        switch (match) {
            case ORDERS:
            case ORDER_BY_ID:
                result = bikeDriverDbHelper.getReadableDatabase().query(BikeDriverContracts.OrderEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = buildUriMatcher().match(uri);
        switch (match) {
            case ORDERS:
                return BikeDriverContracts.OrderEntry.CONTENT_TYPE;
            case ORDER_BY_ID:
                return BikeDriverContracts.OrderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = bikeDriverDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        Uri result;

        switch (match) {
            case ORDERS: {
                long id = database.insert(BikeDriverContracts.OrderEntry.TABLE_NAME, null, values);
                if (id > 0)
                    result = BikeDriverContracts.OrderEntry.buildMovieUri(Long.toString(id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        database.close();
        getContext().getContentResolver().notifyChange(result, null);

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = bikeDriverDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsDeleted = 0;

        switch (match) {
            case ORDERS: {
                rowsDeleted = database.delete(BikeDriverContracts.OrderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        database.close();

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = bikeDriverDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsUpdated = 0;

        switch (match) {
            case ORDERS: {
                rowsUpdated = database.update(BikeDriverContracts.OrderEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        database.close();

        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase database = bikeDriverDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match) {
            case ORDERS: {
                return insertBulkData(BikeDriverContracts.OrderEntry.TABLE_NAME, uri, values, database);
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int insertBulkData(String table, Uri uri, ContentValues[] values, SQLiteDatabase database) {
        int rowsInserted = 0;
        database.beginTransaction();
        try {
            for (ContentValues value : values) {
                long id = database.insert(table, null, value);
                if (id != -1)
                    rowsInserted++;
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsInserted;
    }

    @Override
    public void shutdown() {
        bikeDriverDbHelper.close();
        super.shutdown();
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(BikeDriverContracts.CONTENT_AUTHORITY, BikeDriverContracts.ORDERS_PATH, ORDERS);
        matcher.addURI(BikeDriverContracts.CONTENT_AUTHORITY, BikeDriverContracts.ORDERS_PATH + "/*", ORDER_BY_ID);
        return matcher;
    }
}
