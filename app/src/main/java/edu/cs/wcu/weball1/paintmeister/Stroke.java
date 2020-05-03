package edu.cs.wcu.weball1.paintmeister;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Stroke {
    private Path path;
    private Paint paint;

    public Stroke (Paint paint) {
        this.paint = paint;
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void addPoint(PointF pt) {
        if (path == null) {
            path = new Path();
            path.moveTo(pt.x, pt.y);
        } else {
            path.lineTo(pt.x, pt.y);
        }
    }
}
