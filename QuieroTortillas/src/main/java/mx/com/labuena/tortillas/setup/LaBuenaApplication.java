package mx.com.labuena.tortillas.setup;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by clerks on 8/6/16.
 */

public class LaBuenaApplication extends Application {

    private LaBuenaModules laBuenaModules;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initObjectGraph();
        MultiDex.install(this);
    }

    public LaBuenaModules getObjectGraph() {
        if (laBuenaModules == null)
            initObjectGraph();

        return laBuenaModules;
    }


    public static LaBuenaModules getObjectGraph(Context context) {
        return ((LaBuenaApplication) context).getObjectGraph();
    }

    private void initObjectGraph() {
        laBuenaModules = DaggerLaBuenaModules.builder()
                .laBuenaApplicationModules(
                        new LaBuenaApplicationModules(this)).build();
    }
}
