package androidaid.android.com.androidaid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidaid.android.com.androidaid.Main;
import androidaid.android.com.androidaid.program_flow.Flags;

public class StartOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            System.out.println("[sproc32.StartOnBootReceiver]: Boot is completed.");

            if(Flags.isRunning == false) {
                System.out.println("[sproc32.StartOnBootReceiver]: App isn't runing. Starting app.");
                Flags.isRunning = true;

                Intent i = new Intent(context, Main.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //NECESSARY CAUSE WE WANT TO START AN ACTIVITY OUTSIDE OF OUR APP
                context.startActivity(i);
            } else {
                System.out.println("[sproc32.StartOnBootReceiver]: App is already running.");
            }
        }
    }
}
