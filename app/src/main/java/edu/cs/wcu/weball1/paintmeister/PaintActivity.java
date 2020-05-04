package edu.cs.wcu.weball1.paintmeister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * PaintActivity.java
 *
 * The screen that will allow users to paint to their canvas
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */

public class PaintActivity extends AppCompatActivity {

    private static final int COLOR_REQ = 2;

    /**
     * Reference to the area on the screen that is being touched.
     */
    CustomView touchArea;

    /**
     * Reference to the ActionBar menu
     */
    Menu menu;

    /**
     * Called when the view is created.
     * @param savedInstanceState The savedInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        // Setting up a back button on the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        } // end if

        touchArea = this.findViewById(R.id.view1);
        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            this.setPictureFromSavedFile(extras.getString("loadedFile"));
        } // end if
    } //==========================================================

    /**
     * Initializes the contents of the Activity's options menu.
     * @param menu The options menu.
     * @return True if the menu is to be displayed, false otherwise.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        this.menu = menu;

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            MenuItem item = this.menu.findItem(R.id.painting_title);
            item.setTitle(extras.getString("fileName"));
        } // end if

        return true;
    } // end onCreateOptionsMenu

    /**
     * Recreates a saved drawing on the screen.
     * @param loadedFile Text file that contains the information for drawing a saved painting on
     *                   the screen.
     */
    private void setPictureFromSavedFile(String loadedFile) {
        String fileText = getFileText(loadedFile);
        this.touchArea.setDrawingFromString(fileText);
    } // end setPictureFromSavedFile

    /**
     * Gets the textual drawing information contained within a file.
     * @param fileName The name of the file that contains the painting.
     * @return
     */
    private String getFileText(String fileName) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            } // end while
            br.close();
        } catch (IOException e) {
            Toast.makeText(this, "File could not be read.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } // end catch

        return text.toString();
    } // end getFileText

    /**
     * Behavior to be completed when an action bar item is clicked
     *
     * @param item the action bar item that was clicked
     * @return true if the action could be completed, false otherwise
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                this.onSaveButtonClicked();
                break;
            case R.id.load_button:
                this.onLoadButtonClicked();
                break;
            case R.id.painting_title:
                this.onPaintingTitleClicked();
                break;
            case R.id.choose_color_button:
                this.onChooseColorClicked();
                break;
            case R.id.brush_width_button:
                this.onBrushWidthClicked();
                break;
            case android.R.id.home:
                // Exits w/out saving on back button pressed
                finish();
                break;
        } // end switch

        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected

    /**
     * Behavior to be completed when the painting's title is clicked
     */
    public void onPaintingTitleClicked() {
        AlertDialog.Builder inputTitleChange = new AlertDialog.Builder(this);
        inputTitleChange.setTitle(R.string.change_title_title);
        inputTitleChange.setMessage(R.string.new_title_message);
        final EditText newTitle = new EditText(this);
        inputTitleChange.setView(newTitle);
        inputTitleChange.setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitleText = newTitle.getText().toString();
                if (!newTitleText.equals("")) {
                    MenuItem item = menu.findItem(R.id.painting_title);
                    item.setTitle(newTitleText);
                } // end if

                dialog.dismiss();
            } // end onClick
        }); // end setPositiveButton
        inputTitleChange.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } // end onClick
        }); // end setNegativeButton

        AlertDialog prompt = inputTitleChange.create();
        prompt.show();
    } // end onPaintingTitleClicked

    /**
     * Behavior to be completed when the save button is clicked
     */
    public void onSaveButtonClicked() {
        FileOutputStream stream;

        MenuItem item = menu.findItem(R.id.painting_title);
        String fileName = item.getTitle().toString();

        // See if I can load files from directory
        File[] files = this.getFilesDir().listFiles();

        try {
            stream = openFileOutput(fileName, Context.MODE_PRIVATE);
            stream.write(this.touchArea.getStringRepresentation().getBytes());
            stream.close();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR -- Could not read from file!", Toast.LENGTH_SHORT).show();
        } // end catch

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    } // end onSaveButtonClicked

    /**
     * Behavior to be performed when the load button is clicked
     */
    public void onLoadButtonClicked() {
        Intent loadScreen = new Intent(this, LoadScreen.class);
        loadScreen.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        this.startActivity(loadScreen);
    } // end onLoadButtonClicked

    /**
     * Behavior to be performed the color chooser is clicked
     */
    private void onChooseColorClicked() {
        Intent colorPicker = new Intent(this, ColorPickerActivity.class);
        startActivityForResult(colorPicker, COLOR_REQ);
    } // end onBrushColorClicked

    /**
     * Called when an activity returns a result to this activity.
     * @param requestCode The requestCode that returned the result.
     * @param resultCode The result code to determine if the desired response happened.
     * @param data The data that is held in the intent being returned from the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if(requestCode == 2) {
            String color = data.getStringExtra("color");
            if(color != null)
                touchArea.setPaintColor(Integer.parseInt(color));
        }
    }

    /**
     * Behavior to be performed when the brush width action button is clicked
     */
    private void onBrushWidthClicked() {
        final EditText newWidth = new EditText(this);
        int maxLen = 3;

        // Create new dialog and set it up
        AlertDialog.Builder brushWidthChange = new AlertDialog.Builder(this);
        brushWidthChange.setTitle(R.string.choose_width);
        brushWidthChange.setMessage(R.string.width_msg);
        brushWidthChange.setView(newWidth);

        // Make edit text only accept numbers up to 3 digits in length
        newWidth.setInputType(InputType.TYPE_CLASS_NUMBER);
        newWidth.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});

        // Actions for confirmation and cancel buttons
        brushWidthChange.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newBrushWidth = newWidth.getText().toString();
                if (!newBrushWidth.equals("") && newBrushWidth.matches("\\d+")) {
                    touchArea.setBrushWidth(Integer.parseInt(newBrushWidth));

                    // Save the new brush width to shared preferences
                    SharedPreferences brushStats = getSharedPreferences("brushStats",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = brushStats.edit();

                    editor.putInt("brushWidth", Integer.parseInt(newBrushWidth));
                    editor.commit();
                } // end if

                dialog.dismiss();
            } // end onClick
        }); // end setPositiveButton
        brushWidthChange.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } // end onClick
        }); // end setNegativeButton

        // Show the dialog
        AlertDialog prompt = brushWidthChange.create();
        prompt.show();
    } // end onBrushWidthClicked

} // end PaintActivity
