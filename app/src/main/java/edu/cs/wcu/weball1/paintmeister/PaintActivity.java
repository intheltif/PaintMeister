package edu.cs.wcu.weball1.paintmeister;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class PaintActivity extends Activity implements OnTouchListener {

    CustomView touchArea;
    TextView tv;
    EditText name;

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
        name = (EditText) this.findViewById(R.id.painting_name);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            name.setText(extras.getString("fileName"));
            this.setPictureFromSavedFile(extras.getString("loadedFile"));
        }

        //touchArea.setTextView(tv);

    }//==========================================================

    private void setPictureFromSavedFile(String loadedFile) {
        String fileText = getFileText(loadedFile);
        Toast.makeText(this, fileText, Toast.LENGTH_SHORT).show();
    }

    private String getFileText(String fileName) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }

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

    /**
     * Behavior to be completed when the save button is clicked
     * @param v the save button that was clicked
     */
    public void onSaveButtonClicked(View v) {
        FileOutputStream stream;

        String fileName = name.getText().toString();

        // See if I can load files from directory
        File[] files = this.getFilesDir().listFiles();

        try {
            stream = openFileOutput(fileName, Context.MODE_PRIVATE);
            stream.write(this.touchArea.getStringRepresentation().getBytes());
            stream.close();
        } catch (Exception e) {
            // do nothing
        }

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
