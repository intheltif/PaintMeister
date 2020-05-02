package edu.cs.wcu.weball1.paintmeister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom view to capture onClickEvents and draw paths where the user clicks on the screen.
 *
 * @author Evert Ball
 * @author Chris Wolf
 *
 */
public class CustomView extends View implements View.OnTouchListener {

    /** An array list of Path objects which make up the lines on the canvas **/
    private ArrayList<Path> lines;

    /** An array of active points to allow multiple lines to be drawn at the same time */
    private SparseArray<PointF> mActivePointers;

    /**The path currently being drawn by the user**/
    private Path activePath;

    /**The pain that is currently used to draw**/
    Paint mPaint;

    StringBuilder xml;

    /**
     * Called when the custom view is initialized
     * @param context
     */
    public CustomView(Context context, AttributeSet attrs) {
        super(context,attrs);

        this.setOnTouchListener(this);


        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.GREEN);

        this.xml = new StringBuilder();
        this.xml.append("<drawing>\n");
    }


    /**
     * Draw the canvas items
     */
    protected void onDraw(Canvas canvas) {

        if(lines != null){
            for(Path p: lines){
                canvas.drawPath(p, mPaint);
            }// end for
            if(activePath != null) {
                canvas.drawPath(activePath, mPaint);
            } else {
                this.xml.append("<path>\n");
            }

        }// end if
    }


    /**
     * When the users finger is placed on the screem
     * @param x coordinate
     * @param y The y coordinate.
     * @param v The view touched
     */
    public void touchDown(float x, float y, View v){
        if(lines == null)
            lines = new ArrayList<>();
        if(activePath == null)
            activePath = new Path();

        this.xml.append("<path>\n<coordinate>\n<x>" + x + "</x>\n</y>" + y + "</y>\n</coordinate>\n");

        activePath.moveTo(x,y);
    }

    /**
     * When the users finger is dragged.
     * @param x coordinate
     * @param y The y coordinate.
     * @param v The view touched
     */
    public void touchMove(float x, float y, View v){
        //Your Code here
        activePath.lineTo(x,y);
    }

    /**
     * When the users finger is lifted.
     * @param x The x coordinate
     * @param y The y coordinate.
     * @param v The view touched
     */
    public void touchUp(float x, float y,View v){
        //Your code here
        lines.add(activePath);
        activePath = null;

        this.xml.append("<coordinate>\n<x>" + x + "</x>\n</y>" + y + "</y>\n</coordinate>\n");

        this.xml.append("</path>\n");
    }

    /**
     * When the view is touched.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.v("TouchDemo","Touch");

        //Define drawable attributes.
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y, v);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y, v);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(x,y,v);
                break;
        }//end switch

        invalidate();

        return true;

    } // end onDraw method

    public List<Path> getLines() {
        return this.lines;
    }

    public String toXML() {
        // remove the last added path tag
        this.xml.setLength(this.xml.length() - 7);

        return this.xml.toString() + "</drawing>";
    }

} // end CustomView class

