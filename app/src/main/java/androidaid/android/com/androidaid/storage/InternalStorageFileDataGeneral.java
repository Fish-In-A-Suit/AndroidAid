package androidaid.android.com.androidaid.storage;

/**
 * Represents file data in the general_text internal storage file
 */
public class InternalStorageFileDataGeneral extends InternalStorageFileData implements StorageFile {
    String source;

    public InternalStorageFileDataGeneral(String date, String text, String source) {
        super(date, text);
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
