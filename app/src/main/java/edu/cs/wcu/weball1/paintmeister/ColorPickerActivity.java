package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


/**
 * ColorPickerActivity.java
 *
 * The screen that will allow the user to choose a selection of colors to paint with.
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class ColorPickerActivity extends AppCompatActivity implements ColorAdapter.ColorWasClicked {
    /** minimum number of colors allowed */
    private static final int COLOR_REQ = 2;

    /**
     * Behavior to be completed when the activity is created
     * @param savedInstanceState saved preferences from an earlier iteration of this application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        // Get all of the file names
        int[] colors = getResources().getIntArray(R.array.rainbow);

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.color_view);
        RecyclerView.Adapter myAdapter = new ColorAdapter(colors, this);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    } // end onCreate

    /**
     * Behavior to be completed when a color is clicked
     * @param text the name of the color that was clicked
     */
    @Override
    public void colorClicked(String text) {
        Intent result = new Intent();
        result.putExtra("color", text);
        setResult(COLOR_REQ, result);

        // Save this to shared preferences
        SharedPreferences brushStats = getSharedPreferences("brushStats",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = brushStats.edit();

        editor.putInt("brushColor", Integer.parseInt(text));
        editor.apply();

        finish();
    } // end colorClicked
} // end ColorPicker class
