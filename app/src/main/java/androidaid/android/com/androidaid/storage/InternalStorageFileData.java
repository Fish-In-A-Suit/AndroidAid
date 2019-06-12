package androidaid.android.com.androidaid.storage;

import com.squareup.okhttp.internal.Internal;

/**
 * This class represents data in the most basic form which is stored on the internal storage.
 */
public class InternalStorageFileData implements StorageFile{
    String date;
    String text;

    public InternalStorageFileData(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
