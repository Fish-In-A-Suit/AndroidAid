package androidaid.android.com.androidaid.core;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * provides access to system stuff, like the screen, etc
 */
public class SystemManager {
    private static int screenWidth;
    private static int screenHeight;

    public static void init(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    /**
     * A value which determines if the specified view is inside the screen coordinates of the specified quadrant at the given precision
     * @param quadrant The quadrant which should be used for checking if it contains the view
     * @param view The view whose coordinates to check if it is inside the quadrant
     * @param precision The precision at which to shrink the quadrant. 1 = default quadrant, 2 = one half of the default quadrant, 3 = one third of the default quadrant
     * @param insets The insets to apply to the specified view, if it is out of the screen for some reason
     * @return
     */
    public static boolean isViewInQuadrant(Quadrant quadrant, AccessibilityNodeInfo view, int precision, Insets insets) {
        int offsetWidth;
        int offsetHeight;

        if(precision > 1) {
            offsetWidth = screenWidth/(2*precision);
            offsetHeight = screenHeight/(2*precision);
        } else {
            offsetHeight = 0;
            offsetWidth = 0;
        }

        int halfWidth = screenWidth/2;
        int halfHeight = screenHeight/2;

        int left;
        int top;
        int right;
        int bottom;

        Rect bounds = null;

        switch (quadrant) {
            case TOP_RIGHT:
                left = halfWidth + offsetWidth;
                top = 0;
                right = halfWidth;
                bottom = halfHeight-offsetHeight;
                bounds = new Rect(left, top, right, bottom);
                break;
            case TOP_LEFT:
                left = 0;
                top = 0;
                right = halfWidth - offsetWidth;
                bottom = halfHeight - offsetHeight;
                bounds = new Rect(left, top, right, bottom);
                break;
            case BOTTOM_LEFT:
                left = 0;
                top = halfHeight+offsetHeight;
                right = halfWidth - offsetWidth;
                bottom = screenHeight;
                bounds = new Rect(left, top, right, bottom);
                break;
            case BOTTOM_RIGHT:
                left = halfWidth + offsetWidth;
                top = halfHeight + offsetHeight;
                right = screenWidth;
                bottom = screenHeight;
                bounds = new Rect(left, top, right, bottom);
                break;
        }

        Rect viewRectangle = new Rect();
        view.getBoundsInScreen(viewRectangle);

        //System.out.println("[sproc32.SystemManager.isViewInQuadrant]: Checking if rectangle with coordinates [" + bounds.left + ", " + bounds.top + "|" + bounds.right + ", " + bounds.bottom +
        //"] contains view rectangle with coordinates [" + viewRectangle.left + ", " + viewRectangle.top + "|" + viewRectangle.right + ", " + viewRectangle.bottom + "]");

        //boolean contains = bounds.contains(viewRectangle);
        boolean contains = bounds.intersect(viewRectangle);

        return contains;
    }
}
