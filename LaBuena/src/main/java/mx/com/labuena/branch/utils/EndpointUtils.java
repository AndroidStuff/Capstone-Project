package mx.com.labuena.branch.utils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by clerks on 8/16/16.
 */

public class EndpointUtils {
    public static final String URL_FORMAT = "https://%s.appspot.com/_ah/api/";
    public static final String GC_PROJECT_ID_PROPERTY = "google_cloud_project_id";

    private EndpointUtils() {
    }

    public static String getRootUrl(Context context) {
        return String.format(URL_FORMAT, getProjectId(context));
    }

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    @NonNull
    private static String getProjectId(Context context) {
        return PropertiesReader.getProperty(context, GC_PROJECT_ID_PROPERTY);
    }
}
