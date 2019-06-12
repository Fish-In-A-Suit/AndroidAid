package androidaid.android.com.androidaid.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import androidaid.android.com.androidaid.program_flow.Constants;

/**
 * Class for testing whether internet speed is high enough for this app to be sending data to firebase
 */
public class InternetSpeed {
    private static Context systemContext;

    public static void initialize(Context c) {
        systemContext = c;
    }
    /**
     * @return True if internet speed is high enough and false if internet speed is slow
     */
    public static boolean isOnline() {
        System.out.println("[sproc32.networking.InternetSpeed.isOnline]: Checking internet status...");

        ConnectivityManager connectivityManager = (ConnectivityManager) systemContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        //just to determine if wifi/mobile is connected, no function
        boolean isWifiConnected = false; //wifi connection
        boolean isMobileConnected = false; //mobile data connection

        for(Network network : connectivityManager.getAllNetworks()) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConnected |= networkInfo.isConnected();
            }

            if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConnected |= networkInfo.isConnected();
            }
        }
        System.out.println("[sproc32.networking.InternetSpeed.isOnline]: isWifiConnected = " + isWifiConnected + " | isMobileConnected = " + isMobileConnected);


        Network activeNetwork = connectivityManager.getActiveNetwork();
        if(null != activeNetwork) {
            NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(activeNetwork);

            if(null!=nc) {
                int downloadSpeed = nc.getLinkDownstreamBandwidthKbps();
                int uploadSpeed = nc.getLinkUpstreamBandwidthKbps();
                System.out.println("[sproc32.networking.InternetSpeed.isOnline]: downloadSpeedBandwidth = " + downloadSpeed/1000 + " | uploadSpeedBandwidth = " + uploadSpeed/1000 + " (taken in Mbps)");

                //minimum allowed download speed is 3Mbps, minimul allowed upload speed is 1Mbps
                if(downloadSpeed >= Constants.INTERNET_MINIMUM_ALLOWED_DOWNLOAD_SPEED && uploadSpeed >= Constants.INTERNET_MINIMUM_ALLOWED_UPLOAD_SPEED) {
                    System.out.println("[sproc32.networking.InternetSpeed.isOnline]: Clients internet connection is fast enough.");
                    return true;
                } else {
                    System.out.println("[sproc32.networking.InternetSpeed.isOnline]: Client's internet connection is TOO SLOW. Returning false.");
                    return false;
                }
            } else {
                System.out.println("[sproc32.networking.InternetSpeed.isOnline]: No network capabilities found! Returning false.");
                return false;
            }
        } else {
            System.out.println("[sproc32.networking.InternetSpeed.isOnline]: No active network found! Returning false.");
            return false;

        }

    }
}
