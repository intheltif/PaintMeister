package edu.cs.wcu.weball1.paintmeister;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * CustomView.java
 *
 * A custom view to capture onClickEvents and draw paths where the user clicks on the screen.
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class CustomView extends View {
    /**
     * An array list of Stroke objects to draw lines on the canvas
     **/
    private List<Stroke> strokes;

    /**
     * The list of strokes that will allow for multi-touch painting
     */
    private SparseArray<Stroke> activeStrokes;

    /**
     * The paint that is currently used to draw
     **/
    Paint currentPaint;

    /**
     * Holds the text data for saving/loading paintings
     */
    StringBuilder stringRepresentation;

    /**
     * Default brush size
     */
    private static final int DEFAULT_WIDTH = 6;

    /**
     * The brush width
     */
    private int width;

    /**
     * Called when the custom view is initialized.
     *
     * @param context The application context
     * @param attrs attributes of this CustomView
     */
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        width = DEFAULT_WIDTH;

        // Set up the brush
        SharedPreferences brushStats = context.getSharedPreferences("brushStats", Context.MODE_PRIVATE);
        currentPaint = new Paint();
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(brushStats.getInt("brushWidth", DEFAULT_WIDTH));
        currentPaint.setColor(brushStats.getInt("brushColor", Color.GREEN));

        // Set up the list of strokes
        strokes = new ArrayList<>();
        activeStrokes = new SparseArray<>();
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.stringRepresentation = new StringBuilder();
    }

    /**
     * Draw the canvas items
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (strokes != null) {
            for (Stroke stroke : strokes) {
                if (stroke != null) {
                    Path path = stroke.getPath();
                    Paint paint = stroke.getPaint();
                    if (path != null && paint != null) {
                        canvas.drawPath(path, paint);
                    } // end inner-if
                } // end outer-if
            } // end for
        } // end if
    } // end onDraw

    /**
     * When the users finger is placed on the screem
     *
     * @param x  coordinate
     * @param y  The y coordinate.
     * @param id The pointer id
     */
    public void touchDown(float x, float y, int id) {
        // Set the new brush settings
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(currentPaint.getStrokeWidth());
        paint.setColor(currentPaint.getColor());

        // Set up the new stroke
        PointF pt = new PointF(x, y);
        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);

        // add to the current strokes
        activeStrokes.put(id, stroke);
        strokes.add(stroke);

        // add textual data for saving and loading
        this.stringRepresentation.append("# " + currentPaint.getColor() + " " + width + " ");
        this.stringRepresentation.append(x + " " + y + " ");
    } // end touchDown

    /**
     * When the users finger is dragged.
     *
     * @param x  coordinate
     * @param y  The y coordinate.
     * @param id The pointer id
     */
    public void touchMove(float x, float y, int id) {
        // Get the stroke currently being drawn and add this point to it
        Stroke stroke = activeStrokes.get(id);
        if (stroke != null) {
            PointF pt = new PointF(x, y);
            stroke.addPoint(pt);
        } // end if

        // add this textual data for saving and loading
        this.stringRepresentation.append(x + " " + y + " ");
    } // end touchMove

    /**
     * When the users finger is lifted.
     *
     * @param x The x coordinate
     * @param y The y coordinate.
     */
    public void touchUp(float x, float y) {
        Path activePath = null;

        // add this textual data for saving and loading
        this.stringRepresentation.append(x + " " + y + "\n");
    } // end touchUp

    /**
     * When the view is touched.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        final int pointerCount = event.getPointerCount();
        //Define drawable attributes.
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchDown(x, y, event.getPointerId(0));
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < pointerCount; i++) {
                    touchMove(event.getX(i), event.getY(i), event.getPointerId(i));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touchUp(x, y);
                break;
        }//end switch

        // force the screen to "redraw"
        invalidate();
        return true;

    } // end onDraw method

    /**
     * Provides the textual representation of this drawing
     *
     * @return the textual representation of this drawing
     */
    public String getStringRepresentation() {
        return this.stringRepresentation.toString();
    } // end getStringRepresentation

    /**
     * Takes in a string (from a file) and parses it and converts it into a painting
     *
     * @param fileText the text from the file that has a saved painting
     */
    public void setDrawingFromString(String fileText) {
        // Save the text to be added onto later
        this.stringRepresentation = new StringBuilder(fileText);
        this.strokes = new ArrayList<>();
        Stroke activeStroke = null;

        // Split the text file into pieces for parsing
        String[] fileCommands = fileText.split("\\s+");

        int i = 0;
        while (i < fileCommands.length) {
            if (fileCommands[i].equals("#")) {
                // if this is not the first stroke, add the last stroke to our list
                if (activeStroke != null) {
                    this.strokes.add(activeStroke);
                } // end inner-if

                // Get the brush stats for this stroke
                Paint newPaint = new Paint();
                i++;
                newPaint.setColor(Integer.parseInt(fileCommands[i]));
                i++;
                newPaint.setStrokeWidth(Float.parseFloat(fileCommands[i]));
                newPaint.setStyle(Paint.Style.STROKE);

                // "lift the brush" for the next stroke
                activeStroke = new Stroke(newPaint);
            } else {
                float xCoor = Float.parseFloat(fileCommands[i]);
                i++;
                float yCoor = Float.parseFloat(fileCommands[i]);

                activeStroke.addPoint(new PointF(xCoor, yCoor));
            } // end else

            // increment to the next bit of data in the file
            i++;
        } // end while

        // add the last stroke that was read in
        this.strokes.add(activeStroke);
    } // end setDrawingFromString

    /**
     * Sets the color of the paint we draw on the canvas with.
     *
     * @param color The color to change to.
     */
    public void setPaintColor(int color) {
        this.currentPaint.setColor(color);
    } // end setPaintColor method

    /**
     * Sets the width of the current brush stroke
     *
     * @param width the new width of the brush stroke
     */
    public void setBrushWidth(int width) {
        this.width = width;
    } // end setBrushWidth
} // end CustomView class

