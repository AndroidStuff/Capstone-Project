package mx.com.labuena.services.utils;


import java.util.Date;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class DateExtensor {
    public static java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }
}
