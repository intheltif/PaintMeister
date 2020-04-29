package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class PaintActivity extends Activity implements OnTouchListener {

    CustomView touchArea;
    TextView tv;

    private ShapeDrawable[] mDrawable = new ShapeDrawable[5];

    /**
     * Called when the view is created.
     * @param savedInstanceState The savedInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        touchArea = (CustomView) this.findViewById(R.id.view1);
        //	touchArea.setOnTouchListener(this);

        tv = (TextView) this.findViewById(R.id.textView1);

        //touchArea.setTextView(tv);

    }//==========================================================

    //===========================================================
    /**
     * Called when the view instance is touched by the user.
     * @param v The view instance that was touched.
     * @param event The motion event that carries the touch data.
     */
    //===========================================================
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float raw_x = event.getRawX();
        float raw_y = event.getRawY();

        float axis_x = event.getAxisValue(MotionEvent.AXIS_X);
        float axis_y = event.getAxisValue(MotionEvent.AXIS_Y);


        float x = event.getX();
        float y = event.getY();
        tv.setText("RAW X:" + raw_x + "Y: " + raw_y +
                "     AXIS X:" + axis_x + " Y:" + axis_y +
                "     GETX:" + x + "  GETY:" + y);

        return false;
    }//===========================================================
}
