package androidaid.android.com.androidaid.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTools {
    /**
     * @return The current date and time
     */
    public static String getCurrentDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy, MM, dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
