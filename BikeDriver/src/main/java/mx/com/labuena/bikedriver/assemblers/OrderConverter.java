package mx.com.labuena.bikedriver.assemblers;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import java.util.List;

import mx.com.labuena.bikedriver.data.BikeDriverContracts;
import mx.com.labuena.bikedriver.models.Coordinate;
import mx.com.labuena.services.bikers.model.Coordinates;
import mx.com.labuena.services.bikers.model.Order;

/**
 * Created by clerks on 8/13/16.
 */

public final class OrderConverter {
    private OrderConverter() {
    }

    public static ContentValues[] toContentValues(List<Order> orders) {
        ContentValues[] values = new ContentValues[orders.size()];
        int index = 0;
        for (Order order :
                orders) {
            values[index] = toContentValues(order);
            index++;
        }

        return values;
    }

    public static ContentValues toContentValues(Order order) {
        ContentValues values = new ContentValues();
        Coordinates coordinates = order.getCoordinates();
        values.put(BikeDriverContracts.OrderEntry.ID, order.getOrderId());
        values.put(BikeDriverContracts.OrderEntry.CLIENT_EMAIL_COLUMN, order.getClientEmail());
        values.put(BikeDriverContracts.OrderEntry.CLIENT_NAME_COLUMN, order.getClientName());
        values.put(BikeDriverContracts.OrderEntry.LATITUDE_COLUMN, coordinates.getLatitude());
        values.put(BikeDriverContracts.OrderEntry.LONGITUDE_COLUMN, coordinates.getLongitude());
        values.put(BikeDriverContracts.OrderEntry.QUANTITY_COLUMN, order.getQuantity());
        return values;
    }

    public static mx.com.labuena.bikedriver.models.Order toTransferObject(String message) {
        return new Gson().fromJson(message, mx.com.labuena.bikedriver.models.Order.class);
    }

    public static mx.com.labuena.bikedriver.models.Order toModel(Cursor cursor) {
        int orderId = cursor.getInt(0);
        String clientEmail = cursor.getString(1);
        String clientName = cursor.getString(2);

        double latitude = cursor.getDouble(3);
        double logitude = cursor.getDouble(4);

        Coordinate coordinates = new Coordinate(latitude, logitude);
        int quantity = cursor.getInt(5);

        return new mx.com.labuena.bikedriver.models.Order(orderId, clientEmail, clientName, coordinates, quantity);
    }
}
