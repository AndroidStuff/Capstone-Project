package mx.com.labuena.tortillas.events;

import android.support.v4.app.Fragment;

/**
 * Created by clerks on 8/6/16.
 */

public class ReplaceFragmentEvent {
    private final Fragment fragment;
    private final boolean requiredToAddInBackStack;

    public ReplaceFragmentEvent(Fragment fragment, boolean requiredToAddInBackStack) {
        this.fragment = fragment;
        this.requiredToAddInBackStack = requiredToAddInBackStack;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public boolean isRequiredToAddInBackStack() {
        return requiredToAddInBackStack;
    }
}
