package androidaid.android.com.androidaid.utilities;

import java.util.ArrayList;

public class ArrayUtils {
    public static boolean isWithin(int i, ArrayList<Integer> src) {
        for(int integer : src) {
            if(i == integer) {
                return true;
            }
        }

        return false;
    }
}
