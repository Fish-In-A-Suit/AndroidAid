package androidaid.android.com.androidaid.utilities;

import com.google.android.gms.common.util.ArrayUtils;

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
            //must not allow this in shipment
            //System.out.println("[sproc32.StringKeeper.displayArray]: " + s);
        }
    }

    public static void displayArray(ArrayList<String> array) {
        for (String s : array) {
            //must not allow this in shipment
            //System.out.println("[sproc32.StringKeeper.displayArray]: " + s);
        }
    }

    public static String getArrayAsString(ArrayList<String> src) {
        return "";

        /* //must not allow this in shipment
        StringBuilder sb = new StringBuilder();
        for(String s:src) {
            sb.append(s).append(" | ");
        }
        return sb.toString();
        */
    }

    /**
     * Finds the firs string in the target array which contains the "target" string
     * @param target
     * @param source
     */
    public static String find(String target, ArrayList<String> source) {
        for(String s : source) {
            if (s.contains(target)) {
                return s;
            }
        }
        //System.out.println("[sproc32.StringKeeper.find]: Couldn't find string with target " + target + " in the specified source arraylist!");
        return "";
    }

    public static void eliminateForbidden(String source, String[] forbiddenValues) {
        //System.out.println("[sproc32.StringUtils.eliminateForbidden]: Eliminating forbidden values from source: " + source);
        for(String forbiddenValue : forbiddenValues) {
            //System.out.println("[sproc32.StringUtils.eliminateForbidden]: Eliminating value " + forbiddenValue);
            source.replaceAll(forbiddenValue, "");
        }
        //System.out.println("[sproc32.StringUtils.eliminateForbidden]: Cleared string: " + source);
    }

    /**
     * Finds the "amount" longest strings in array
     * @param src
     * @return
     */
    public static String determineLongest(ArrayList<String> src, int amount) {
        //System.out.println("[sproc32.StringUtils.determineLongest]: Determining longest " + amount + " strings in array: " + StringUtils.getArrayAsString(src));
        ArrayList<String> amountLongestStrings = new ArrayList<>();

        for(int i = 0; i<src.size(); i++) {
            String s = src.get(i);

            if(i <= amount) {
                //System.out.println("[sproc32.StringUtils.determineLongest]: Automatically added String '" + s + "' to longest strings");
                amountLongestStrings.add(s);
            } else {
                //System.out.println("[sproc32.StringUtils.determineLongest]: Longest strings already full. Checking if string '" + s + "' is longer than the shortest string of lognest strings.");

                String shortestStringFromLongestStrings = determineShortest(amountLongestStrings, false);
                //System.out.println("[sproc32.StringUtils.determineLongest]: Shortest string from amountLongestStrings = " + shortestStringFromLongestStrings);

                if(s.length() > shortestStringFromLongestStrings.length()) {
                    //System.out.println("[sproc32.StringUtils.determineLongest]: String " + s + " is longer than " + shortestStringFromLongestStrings + ". Replacing in array.");
                    amountLongestStrings.remove(determineShortest(amountLongestStrings, true));
                    amountLongestStrings.add(s);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for(String s : amountLongestStrings) {
            sb.append(s).append(" | ");
        }

        return sb.toString();
    }

    /**
     * Determine the shortest tring and remove it from array if needed (if remove == true, then remove, else keep)
     * @param src
     * @param remove
     * @return
     */
    public static String determineShortest(ArrayList<String> src, boolean remove) {
        if (src.size()==0) return null;

        String shortestString = "";
        String previousString = "";

        int indexToRemove = 0;

        for(int i = 0; i<src.size(); i++) {
            String currentString = src.get(i);

            if(currentString.length() < previousString.length()) {
                shortestString = currentString;
                indexToRemove = i;
            }
            previousString = currentString;
        }

        if(remove == true) {
            src.remove(indexToRemove);
        }

        return shortestString;
    }

    public static void clearArray(ArrayList<String> src) {
        if (src.size() == 0) return;
        //System.out.println("[sproc32.StringUtils.clearArray]: Started clearing array: " + getArrayAsString(src));

        ArrayList<Integer> indexesToRemove = new ArrayList<>();

        for(int i = 0; i<src.size(); i++) {
            for(int j = 0; j<src.size(); j++) {
                if (i == j) continue;
                if(src.get(i).length() < src.get(j).length()) continue;
                if(androidaid.android.com.androidaid.utilities.ArrayUtils.isWithin(i, indexesToRemove)) continue;

                //System.out.println("comparing indexes: " + i + " - " + j);

                String s1 = src.get(i);
                String s2 = src.get(j);

                //System.out.println("string1 = " + s1 + " | string2 = " + s2);

                if(s1.equals(s2)) {
                    //System.out.println("s1 equals s2 | adding index " + j);
                    indexesToRemove.add(j);
                    continue;
                }

                if(s1.contains(s2)) {
                    //System.out.println("'" + s1 + "' contains '" + s2 + "'" + ". Adding index " + j);
                    indexesToRemove.add(j);
                }
            }
        }

        ArrayList<String> stringsToRemove = new ArrayList<>();
        for(int index : indexesToRemove) {
            stringsToRemove.add(src.get(index));
        }

        for(String s : stringsToRemove) {
            src.remove(s);
        }
    }

    /**
     * Checks if the source array list contains the given string. True for contains, else false.
     * @param s
     * @param source
     * @return
     */
    public static boolean checkIfContains(String s, ArrayList<String> source) {
        for(String value : source) {
            if(s.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
