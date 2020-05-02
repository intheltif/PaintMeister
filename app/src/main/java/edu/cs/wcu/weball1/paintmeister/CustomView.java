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

    /** Holds the text data for saving/loading paintings */
    StringBuilder stringRepresentation;

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

        this.stringRepresentation = new StringBuilder();
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

        activePath.moveTo(x,y);

        this.stringRepresentation.append("# " + x + " " + y + " ");
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
        this.stringRepresentation.append(x + " " + y);
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

        this.stringRepresentation.append(x + " " + y + "\n");
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


    /**
     * Provides the textual representation of this drawing
     * @return the textual representation of this drawing
     */
    public String getStringRepresentation() {
        return this.stringRepresentation.toString();
    } // end getStringRepresentation

} // end CustomView class

