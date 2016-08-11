package mx.com.labuena.tortillas.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class allows to set the info to display in a dialog. For example to create a ProgressDialog.
 *
 * @author Clemente Morales Fernández
 */
public class DialogData implements Parcelable {
    /**
     * Id of the title resource to use in the Dialog.
     */
    private int resourceTitleId;

    /**
     * Message to use in the Dialog.
     */
    private String message;

    /**
     * If the dialog is cancelable.
     */
    private boolean cancelable;

    /**
     * Id of the icon resource to use in the Dialog.
     */
    private int resourceIconId;

    /**
     * Allows to create an instance of this class with the data for the dialog.
     *
     * @param resourceTitleId   Id of the title resource to use in the Dialog.
     * @param message           Message to use in the Dialog.
     * @param cancelable        Allows to set the dialog as cancelable.
     * @param resourceIconId    Id of the icon resource to use in the Dialog.
     */
    public DialogData(int resourceTitleId,
                      String message, boolean cancelable,
                      int resourceIconId) {
        this.resourceTitleId = resourceTitleId;
        this.message = message;
        this.cancelable = cancelable;
        this.resourceIconId = resourceIconId;
    }

    /**
     * Allows to create an instance of this class with the data for the dialog extracted from the
     * {@link Parcel}.
     *
     * @param source Parcel with the data for the Dialog.
     */
    private DialogData(Parcel source) {
        this.resourceTitleId = source.readInt();
        this.message = source.readString();
        this.cancelable = (source.readByte() == 1);
        this.resourceIconId = source.readInt();
    }

    /**
     * Used to create the items of DialogData previously serialized in the Parcel.
     */
    public static final Parcelable.Creator<DialogData> CREATOR =
            new Parcelable.Creator<DialogData>() {
                public DialogData createFromParcel(Parcel source) {
                    return new DialogData(source);
                }

                public DialogData[] newArray(int size) {
                    return new DialogData[size];
                }

            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destino, int flags) {
        destino.writeInt(this.resourceTitleId);
        destino.writeString(this.message);
        destino.writeByte((byte) (this.cancelable ? 1 : 0));
        destino.writeInt(this.resourceIconId);
    }

    /**
     * Allows to get the Id of the title resource to use in the Dialog.
     * @return Id of the title resource to use in the Dialog.
     */
    public int getResourceTitleId() {
        return resourceTitleId;
    }

    /**
     * Allows to get the message to use in the Dialog.
     * @return Message to use in the Dialog.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Check If the dialog is cancelable.
     * @return If the dialog is cancelable.
     */
    public boolean isCancelable() {
        return cancelable;
    }

    /**
     * Allows to get the Id of the icon resource to use in the Dialog.
     * @return Id of the icon resource to use in the Dialog.
     */
    public int getResourceIconId() {
        return resourceIconId;
    }
}
