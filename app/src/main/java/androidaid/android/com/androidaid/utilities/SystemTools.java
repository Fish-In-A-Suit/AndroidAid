package androidaid.android.com.androidaid.utilities;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ActionMenuView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidaid.android.com.androidaid.core.ActivityManager;

public class SystemTools {
    private static PowerManager powerManager;

    public static void init() {
        powerManager = (PowerManager) ActivityManager.getApplicationContext().getSystemService(Context.POWER_SERVICE);
    }

    /**
     * @return The current date and time
     */
    public static String getCurrentDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy, MM, dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isLocationEnabled(Context context) {
        //System.out.println("[sproc32.SystemTools.isLocationEnabled]: Starting to query whether location is enabled on device.");
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(ActivityManager.getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            boolean isEnabled = locationMode!=Settings.Secure.LOCATION_MODE_OFF;

            /*
            if(isEnabled) {
                System.out.println("[sproc32.SystemTools.isLocationEnabled]: Location setting is enabled! Returning true.");
            } else {
                System.out.println("[sproc32.SystemTools.isLocationEnabled]: Location setting is disabled! Returning false.");
            }
            */

            return isEnabled;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            boolean isEnabled = !TextUtils.isEmpty(locationProviders);

            /*
            if(isEnabled) {
                System.out.println("[sproc32.SystemTools.isLocationEnabled]: Location setting is enabled! Returning true.");
            } else {
                System.out.println("[sproc32.SystemTools.isLocationEnabled]: Location setting is disabled! Returning false.");
            }
            */

            return isEnabled;
        }
    }
}
