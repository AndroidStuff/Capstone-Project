package mx.com.labuena.branch.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by clerks on 8/14/16.
 */

public final class InputMethodManagerExtensor {

    private InputMethodManagerExtensor(){}

    public static void hideKeyBoard(Context context){
        ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
}
