package androidaid.android.com.androidaid.core;

import androidaid.android.com.androidaid.program_flow.Constants;
import androidaid.android.com.androidaid.utilities.StringUtils;

public class StringKeeper {
    private static String currentText  = "";

    public static void setText(String string) {
        if(StringUtils.checkIfNotForbidden(string, Constants.DATABASE_FORBIDDEN_FIELD_TEXT_VALUES)) {
            currentText = string;
        }
    }

    public static String getText() {
        return currentText;
    }



}
