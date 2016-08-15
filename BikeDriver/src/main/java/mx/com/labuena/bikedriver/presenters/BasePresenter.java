package mx.com.labuena.bikedriver.presenters;

import android.app.Application;
import android.app.job.JobScheduler;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by clerks on 8/6/16.
 */

public class BasePresenter {
    protected final EventBus eventBus;
    protected final Application application;

    public BasePresenter(Application application, EventBus eventBus) {
        this.application = application;
        this.eventBus = eventBus;
    }

    public void cancelAllJobs() {
        JobScheduler tm = (JobScheduler) application.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancelAll();
    }
}
