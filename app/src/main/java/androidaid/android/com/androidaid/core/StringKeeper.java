package androidaid.android.com.androidaid.core;

public class StringKeeper {
    private static String currentText;

    public static void setText(String string) {
        currentText = string;
    }

    public static String getText() {
        return currentText;
    }



}
