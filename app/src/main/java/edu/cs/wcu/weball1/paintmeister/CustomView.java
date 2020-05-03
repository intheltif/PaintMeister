package edu.cs.wcu.weball1.paintmeister;

import android.content.Context;
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
 * A custom view to capture onClickEvents and draw paths where the user clicks on the screen.
 *
 * @author Evert Ball
 * @author Chris Wolf
 *
 */
public class CustomView extends View {

    /** An array list of Stroke objects to draw lines on the canvas**/
    private List<Stroke> strokes;

    /** A list of lines that make up the drawings on a canvas. */
    private List<Path> lines;

    private SparseArray<Stroke> activeStrokes;

    /**The path currently being drawn by the user**/
    private Path activePath;

    /**The paint that is currently used to draw**/
    Paint currentPaint;

    /** Holds the text data for saving/loading paintings */
    StringBuilder stringRepresentation;

    /** The brush width */
    private int width;

    /**
     * Called when the custom view is initialized.
     *
     * @param context The application context
     * @param attrs
     */
    public CustomView(Context context, AttributeSet attrs) {
        super(context,attrs);

        //this.setOnTouchListener(this);

        width = 6;

        currentPaint = new Paint();
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(width);
        currentPaint.setColor(Color.GREEN);

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

        if(strokes != null) {
            for(Stroke stroke : strokes) {
                if(stroke != null) {
                    Path path = stroke.getPath();
                    Paint paint = stroke.getPaint();
                    if(path != null && paint != null) {
                        canvas.drawPath(path, paint);
                    }
                }
            }
        }

    } // end onDraw

    /**
     * When the users finger is placed on the screem
     * @param x coordinate
     * @param y The y coordinate.
     * @param id The pointer id
     */
    public void touchDown(float x, float y, int id){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(currentPaint.getColor());

        PointF pt = new PointF(x, y);
        Stroke stroke = new Stroke(paint);
        stroke.addPoint(pt);

        activeStrokes.put(id, stroke);
        strokes.add(stroke);


        this.stringRepresentation.append("# " + x + " " + y + " ");
    }

    /**
     * When the users finger is dragged.
     * @param x coordinate
     * @param y The y coordinate.
     * @param id The pointer id
     */
    public void touchMove(float x, float y, int id){

        Stroke stroke = activeStrokes.get(id);
        if(stroke != null) {
            PointF pt = new PointF(x,y);
            stroke.addPoint(pt);
        }

        this.stringRepresentation.append(x + " " + y + " ");
    }

    /**
     * When the users finger is lifted.
     * @param x The x coordinate
     * @param y The y coordinate.
     */
    public void touchUp(float x, float y){
        activePath = null;

        this.stringRepresentation.append(x + " " + y + "\n");
    }

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
                for(int i = 0; i < pointerCount; i++) {
                    touchMove(event.getX(i), event.getY(i), event.getPointerId(i) );
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touchUp(x,y);
                break;
        }//end switch

        invalidate();


        return true;

    } // end onDraw method

    /**
     * Provides the textual representation of this drawing
     * @return the textual representation of this drawing
     */
    public String getStringRepresentation() {
        return this.stringRepresentation.toString();
    } // end getStringRepresentation

    /**
     * Takes in a string (from a file) and parses it and converts it into a painting
     * @param fileText the text from the file that has a saved painting
     */
    //TODO: Update to use the Stroke class
    public void setDrawingFromString(String fileText) {
        // Save the text to be added onto later
        this.stringRepresentation = new StringBuilder(fileText);

        // Split the text file into pieces for parsing
        String[] fileCommands = fileText.split("\\s+");
        this.activePath = null;
        this.lines = new ArrayList<>();

        int i = 0;
        while(i < fileCommands.length) {
            // if this is the start of a new path
            if (fileCommands[i].equals("#")) {
                if (this.activePath != null) {
                    lines.add(this.activePath);
                } // end inner-if

                this.activePath = new Path();
            } else {
                float xCoor = Float.parseFloat(fileCommands[i]);
                i++;
                float yCoor = Float.parseFloat(fileCommands[i]);

                // if this is the first "dot" in the line, move to it
                // otherwise, draw a line to it
                if (fileCommands[i - 2].equals("#")) {
                    this.activePath.moveTo(xCoor, yCoor);
                } else {
                    this.activePath.lineTo(xCoor, yCoor);
                } // end if-else
            } // end outer-else

            i++;
        } // end while
    } // end setDrawingFromString
    /**
     * Sets the color of the paint we draw on the canvas with.
     * @param color The color to change to.
     */
    public void setPaintColor(int color) {
        this.currentPaint.setColor(color);
    } // end setPaintColor method

    public void setBrushWidth(int width) {
        this.width = width;
    }

} // end CustomView class

