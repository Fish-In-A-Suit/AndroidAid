package androidaid.android.com.androidaid.visualization;

import android.graphics.Rect;
import android.os.Environment;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is responsible for saving the borders of the AccessibilityNodeInfo children
 */
public class ANIviewExplorer {
    private static ArrayList<Rectangle> rectangles = new ArrayList<>();

    public static File exploreViewsInANI(AccessibilityNodeInfo ani) {
        findAllRectangles(ani);
        return writeRectanglesToFile();
    }

    private static void findAllRectangles(AccessibilityNodeInfo ani) {
        for(int i = 0; i<ani.getChildCount(); i++) {
            AccessibilityNodeInfo child = ani.getChild(i);

            if(child.getChildCount() == 0) {
                //this element has no further children. Add it's screen position to rectangles
                Rect r = new Rect();
                child.getBoundsInScreen(r);
                Rectangle rectangle = new Rectangle(r);
                rectangles.add(rectangle);
            } else {
                findAllRectangles(child);
            }
        }
    }

    private static File writeRectanglesToFile() {
        return null; //todo: START OPFF FROM HERE!!! write all the rectangles to a file and seend it to a server and then create an img of the views from it
    }
}
