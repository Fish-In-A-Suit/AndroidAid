package androidaid.android.com.androidaid.core;

import java.io.File;

public class FileUtils {
    /**
     * Gets the relative file of a path. If path is /data/user/0/androidaid.android.com.androidaid/files/field_text_messenger, then
     * this file returns field/text/messenger
     * @param file
     * @return
     */
    public static String getRelativePath(File file) {
        String fileName = file.getAbsolutePath();
        String[] dirs = fileName.split("/");
        String finalRp = null;

        for(int i = 0; i<dirs.length; i++) {
            if(i == dirs.length-1) {
                finalRp = dirs[i];

                //System.out.println("[sproc32.core.FileUtils.getRelativePath]: Selected relative path = " + finalRp);
            }
        }

        return finalRp;
    }
}
