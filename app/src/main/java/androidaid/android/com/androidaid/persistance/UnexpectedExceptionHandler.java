package androidaid.android.com.androidaid.persistance;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidaid.android.com.androidaid.Main;

/**
 * This class makes sure to restart the application, should any unexpected exception occur
 */
public class UnexpectedExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public UnexpectedExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("[sproc32.persistance.UnexpectedExceptionHandler]: Caught an unexpected exception! Restarting app!");

        Intent intent = new Intent(activity, Main.class);
        intent.putExtra("crash", true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(PersistentApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) PersistentApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis()+100, pendingIntent);

        activity.finish();
        System.exit(2);

    }
}
