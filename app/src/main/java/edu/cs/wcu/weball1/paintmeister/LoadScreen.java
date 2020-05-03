package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * LoadScreen.java
 *
 * The screen that will allow the user to choose to load a painting from their file system
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class LoadScreen extends AppCompatActivity implements MyAdapter.ItemWasClicked {

    /**
     * Behavior to be completed when the activity first generates
     * @param savedInstanceState preferences from a previous use of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        // Get all of the file names
        List<String> fileNames = new ArrayList<>();
        File[] files = this.getFilesDir().listFiles();

        if (files != null) {
            for (File file : files) {
                fileNames.add(file.toString());
            } // end for
        } // end if

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.file_name_view);
        RecyclerView.Adapter myAdapter = new MyAdapter(fileNames, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    } // end onCreate

    /**
     * Behavior to be completed when a file name is clicked
     * @param text the name of the file that was clicked
     */
    @Override
    public void itemClicked(String text) {
        Intent paintScreen = new Intent(this, PaintActivity.class);
        String fullPath = this.getFilesDir().toString();
        fullPath += "/" + text;
        paintScreen.putExtra("loadedFile", fullPath);
        paintScreen.putExtra("fileName", text);
        this.startActivity(paintScreen);
        finish();
    } // end itemClicked
} // LoadScreen
