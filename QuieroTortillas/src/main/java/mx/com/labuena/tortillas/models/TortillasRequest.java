package mx.com.labuena.tortillas.models;

/**
 * Created by clerks on 8/9/16.
 */

public class TortillasRequest {
    public static final int MAX_AMOUNT = 40;
    public static final int MIN_AMOUNT = 1;
    public static final int DEFAULT_CONSUME = 3;

    private int amount = DEFAULT_CONSUME;
    private final User user;
    private DeviceLocation deviceLocation;

    public TortillasRequest(User user) {
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(DeviceLocation deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public boolean isAmountUnderMaxLimit() {
        return amount < MAX_AMOUNT;
    }

    public void increaseAmount() {
        ++amount;
    }

    public boolean isAmountUnderMinLimit() {
        return amount > MIN_AMOUNT;
    }

    public void decreaseAmount() {
        --amount;
    }
}
