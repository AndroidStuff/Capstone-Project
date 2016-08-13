package mx.com.labuena.bikedriver.assemblers;

import android.content.ContentValues;

import java.util.List;

import mx.com.labuena.services.bikers.model.Order;

/**
 * Created by clerks on 8/13/16.
 */

public final class OrderConverter {
    private OrderConverter() {
    }

    public static ContentValues[] toContentValues(List<Order> orders) {
        return new ContentValues[0];
    }

    public static Order toTransferObject(String message) {
        return null;
    }
}
