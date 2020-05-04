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

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

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

    private void setPictureFromSavedFile(String loadedFile) {
        String fileText = getFileText(loadedFile);
        this.touchArea.setDrawingFromString(fileText);
    } // end setPictureFromSavedFile

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
            //You'll need to add proper error handling here
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
        inputTitleChange.setTitle("Change painting title?");
        inputTitleChange.setMessage("What is the new title of your painting?");
        final EditText newTitle = new EditText(this);
        inputTitleChange.setView(newTitle);
        inputTitleChange.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
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
        inputTitleChange.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                touchArea.setPaintColor(envelope.getColor());

                                SharedPreferences brushStats = getSharedPreferences("brushStats", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = brushStats.edit();

                                editor.putInt("brushColor", envelope.getColor());
                                editor.commit();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    } // end onBrushColorClicked

    /**
     * Behavior to be performed when the brush width action button is clicked
     */
    private void onBrushWidthClicked() {
        AlertDialog.Builder brushWidthChange = new AlertDialog.Builder(this);
        brushWidthChange.setTitle("Change Brush Width");
        brushWidthChange.setMessage("Enter a new brush width:");
        final EditText newWidth = new EditText(this);
        int maxLen = 3;
        newWidth.setInputType(InputType.TYPE_CLASS_NUMBER);
        newWidth.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        brushWidthChange.setView(newWidth);
        brushWidthChange.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newBrushWidth = newWidth.getText().toString();
                if (!newBrushWidth.equals("") && newBrushWidth.matches("\\d+")) {
                    touchArea.setBrushWidth(Integer.parseInt(newBrushWidth));

                    // Save the new brush width to shared preferences
                    SharedPreferences brushStats = getSharedPreferences("brushStats", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = brushStats.edit();

                    editor.putInt("brushWidth", Integer.parseInt(newBrushWidth));
                    editor.commit();
                } // end if

                dialog.dismiss();
            } // end onClick
        }); // end setPositiveButton
        brushWidthChange.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } // end onClick
        }); // end setNegativeButton

        AlertDialog prompt = brushWidthChange.create();
        prompt.show();
    } // end onBrushWidthClicked
} // end PaintActivity
