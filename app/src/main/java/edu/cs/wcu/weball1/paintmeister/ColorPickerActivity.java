package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


public class ColorPickerActivity extends AppCompatActivity implements ColorAdapter.ColorWasClicked {

    private static final int COLOR_REQ = 2;

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
        finish();
    } // end colorClicked

} // end ColorPicker class
