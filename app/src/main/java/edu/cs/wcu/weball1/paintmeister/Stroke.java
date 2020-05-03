package edu.cs.wcu.weball1.paintmeister;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

/**
 * Stroke.java
 *
 * A class that represents one stroke in a painting
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class Stroke {
    /** The path that this stroke will take on the screen */
    private Path path;
    /** The brush that will be painting this stroke */
    private Paint paint;

    /**
     * Generates a new stroke
     * @param paint the brush that will be painting this stroke
     */
    public Stroke (Paint paint) {
        this.paint = paint;
    } // end constructor

    /**
     * Getter for the path field
     * @return the path that this stroke will take on the screen
     */
    public Path getPath() {
        return path;
    } // end getPath

    /**
     * Getter for the paint field
     * @return the brush that will be painting this stroke
     */
    public Paint getPaint() {
        return paint;
    } // end getPaint

    /**
     * Adds a point to this stroke
     * @param pt the point to be added to this stroke
     */
    public void addPoint(PointF pt) {
        if (path == null) {
            path = new Path();
            path.moveTo(pt.x, pt.y);
        } else {
            path.lineTo(pt.x, pt.y);
        } // end if-else
    } // end addPoint
} // end Stroke
