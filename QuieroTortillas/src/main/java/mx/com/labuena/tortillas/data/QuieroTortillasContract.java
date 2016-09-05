package mx.com.labuena.tortillas.data;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by moracl6 on 9/05/2016.
 */

public class QuieroTortillasContract {
    public static final String CONTENT_AUTHORITY = "mx.com.labuena.tortillas";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_NAME = "quiero_tortillas.db";

    public static final int DATABASE_VERSION = 1;

    public static final String ORDERS_PATH = "orders";


    public static final class OrderEntry implements BaseColumns {

        public static final String TABLE_NAME = "orders";

        public static final String CLIENT_EMAIL_COLUMN = "client_email";

        public static final String LATITUDE_COLUMN = "latitude";

        public static final String LONGITUDE_COLUMN = "longitude";

        public static final String QUANTITY_COLUMN = "quantity";

        public static final String BIKER_NAME_COLUMN = "biker_name";

        public static final String CREATED_AT_COLUMN = "created_at";

        public static final String UPDATED_AT_COLUMN = "updated_at";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(ORDERS_PATH).build();

        /**
         * MIME type to represent a directory with order items.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ORDERS_PATH;

        /**
         * MIME type to represent a movie item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ORDERS_PATH;


        public static Uri buildMovieUri(String orderId) {
            return CONTENT_URI.buildUpon().appendPath(orderId).build();
        }
    }
}
