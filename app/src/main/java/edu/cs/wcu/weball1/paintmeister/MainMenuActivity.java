package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The main menu of the application. Handles navigating to each portion of the app.
 *
 * @author Evert Ball
 * @author Chris Wolf
 *
 * @version 05 May 2020
 */
public class MainMenuActivity extends AppCompatActivity {

    private Button startPaintBtn;
    private Button loadPaintBtn;
    private Button prefsBtn;
    private Button aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.startPaintBtn = findViewById(R.id.btn_paint);
        this.loadPaintBtn = findViewById(R.id.btn_load);
        this.prefsBtn = findViewById(R.id.btn_prefs);
        this.aboutBtn = findViewById(R.id.btn_info);
    } // end onCreate method

    public void onClick(View v) {
        Intent nextActivity;

        if(v == startPaintBtn) {
            nextActivity = new Intent(this, PaintActivity.class);
            startActivity(nextActivity);
        } else if(v == loadPaintBtn) {
            this.loadPaintButtonPressed();
        } else if(v == prefsBtn) {
            Toast.makeText(this, "Will be implemented soon...", Toast.LENGTH_SHORT).show();
        } else if(v == aboutBtn) {
            Toast.makeText(this, "Will be implemented soon...", Toast.LENGTH_SHORT).show();
        } // end if statement
    } // end onClick method

    private void loadPaintButtonPressed() {
        Intent loadScreen = new Intent(this, LoadScreen.class);
        this.startActivity(loadScreen);
//        File[] files = this.getFilesDir().listFiles();
//
//        List<String> fileNames = new ArrayList<>();
//
//        for (File file : files) {
//            fileNames.add(file.toString());
//        }
//
//        Toast.makeText(this, fileNames.toString(), Toast.LENGTH_SHORT).show();
    }

}
