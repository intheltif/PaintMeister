package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
}
