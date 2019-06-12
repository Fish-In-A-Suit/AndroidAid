package androidaid.android.com.androidaid.visualization;

import android.graphics.Rect;

public class Rectangle {
    public int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rect r) {
        this.x = r.left;
        this.y = r.top;
        this.width = r.right - r.left;
        this.height = r.bottom - r.top;
    }
}
