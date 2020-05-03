package edu.cs.wcu.weball1.paintmeister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class PaintActivity extends AppCompatActivity {

    /** Reference to the area on the screen that is being touched. */
    CustomView touchArea;

    /** Reference to the ActionBar menu */
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
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }


        touchArea = this.findViewById(R.id.view1);
        //	touchArea.setOnTouchListener(this);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            this.setPictureFromSavedFile(extras.getString("loadedFile"));
        }
    } //==========================================================

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        this.menu = menu;

        Bundle extras = this.getIntent().getExtras();

        if(extras != null) {
            MenuItem item = this.menu.findItem(R.id.painting_title);
            item.setTitle(extras.getString("fileName"));
        }

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
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    } // end getFileText

    /**
     * Behavior to be completed when an action bar item is clicked
     * @param item the action bar item that was clicked
     * @return true if the action could be completed, false otherwise
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.preferences:
                this.onPreferencesButtonClicked();
                break;
            case R.id.save_button:
                this.onSaveButtonClicked();
                break;
            case R.id.load_button:
                this.onLoadButtonClicked();
                break;
            case R.id.painting_title:
                this.onPaintingTitleClicked();
                break;
            case android.R.id.home:
                // Exits w/out saving on back button pressed
                // TODO: Maybe make this save when the button is pressed
                finish();
        }

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
        } catch (Exception e) {
            // do nothing
        } // end catch

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    } // end onSaveButtonClicked

    /**
     * Behavior to be performed when the load button is clicked
     */
    public void onLoadButtonClicked() {
        Intent loadScreen = new Intent(this, LoadScreen.class);
        this.startActivity(loadScreen);
    } // end onLoadButtonClicked

    /**
     * Behavior to be completed when the preferences button is clicked
     */
    public void onPreferencesButtonClicked() {
        Toast.makeText(this, "Preferences clicked", Toast.LENGTH_SHORT).show();
    } // end onPreferencesButtonClicked
} // end PaintActivity
