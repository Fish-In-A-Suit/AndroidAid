package androidaid.android.com.androidaid.utilities;

import java.util.ArrayList;

public class StringUtils {

    public static String omitBrackets(String source) {
        return source.replaceAll("\\(", "").replaceAll("\\)", "");
    }



    public static String removeWhitespace(String source) {
        return source.replaceAll("\\s+","");
    }

    /**
     * Substracts s1 from s2
     * @param s1
     * @param s2
     * @return
     */
    public static String subtract(String s1, String s2) {
        //System.out.println("[sproc32.StringUtils.subtract]: Subtracting '" + s1 + "' from '" + s2 + "'. Result = " + s2.substring(s1.length()));
        return s2.substring(s1.length());
    }

    /**
     * Returns true if text is not equal to any of the strings in the array
     * @param text
     * @param forbiddenStrings
     * @return
     */
    public static boolean checkIfNotForbidden(String text, String[] forbiddenStrings) {
        for(String s : forbiddenStrings) {
            if(s.equals(text)) {
                return false;
            }
        }

        return true;
    }

    public static void displayArray(String[] array) {
        for (String s : array) {
            System.out.println("[sproc32.StringKeeper.displayArray]: " + s);
        }
    }

    public static void displayArray(ArrayList<String> array) {
        for (String s : array) {
            System.out.println("[sproc32.StringKeeper.displayArray]: " + s);
        }
    }
}
