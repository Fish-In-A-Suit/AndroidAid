package androidaid.android.com.androidaid.persistance;

import android.app.Application;
import android.content.Context;

/**
 * The role of this class is to provide an application class which is going to be used in the process
 * of starting the application if an uncaught/unexpected exception occurs somewhere during the execution of
 * the application and restart the application
 */
public class PersistentApplication extends Application {
    public static PersistentApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static PersistentApplication getInstance() {
        return instance;
    }
}
