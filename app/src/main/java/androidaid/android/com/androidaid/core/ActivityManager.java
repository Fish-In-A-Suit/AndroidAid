package androidaid.android.com.androidaid.core;

import android.app.Activity;
import android.content.Context;

public class ActivityManager {
    private static Context applicationContext;
    private static Activity mainActivity;

    public static void init(Context c, Activity main) {
        applicationContext = c;
        mainActivity = main;
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static Activity getMainActivity() {
        return mainActivity;
    }
}
