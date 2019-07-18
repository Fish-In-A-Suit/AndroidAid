package androidaid.android.com.androidaid.storage;

import com.squareup.okhttp.internal.Internal;

/**
 * This class represents data in the most basic form which is stored on the internal storage.
 */
public class InternalStorageFileData implements StorageFile{
    String date;
    String text;
    String recipient;
    String extraText;

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

    public String getRecipient() {
        return recipient;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }
}
