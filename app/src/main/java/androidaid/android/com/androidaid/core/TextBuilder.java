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
            //System.out.println("[sproc32.TextBuilder.addText]: Added| " + text);
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
        //System.out.println("[sproc32.TextBuilder.getText]: Returning] " + textBuilder.toString());
        return textBuilder.toString();
    }

    /**
     * Clears the text
     */
    public static void clearText() {
        //System.out.println("[sproc32.TextBuilder.clearText]: Clearing text");
        textBuilder.delete(0, textBuilder.length());
    }

}
