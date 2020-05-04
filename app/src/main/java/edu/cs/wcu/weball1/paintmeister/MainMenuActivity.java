package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MainMenuActivity.java
 *
 * The main menu of the application. Handles navigating to each portion of the app.
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class MainMenuActivity extends AppCompatActivity {

    /** The button to start a new painting */
    private Button startPaintBtn;
    /** The button to load a painting from storage */
    private Button loadPaintBtn;
    /** The button to lead to the "About Developer" screen */
    private Button aboutBtn;
    /** The main menu title at the top of the page */
    private TextView menuTitle;

    /**
     * Behavior to be completed when the screen is loaded
     * @param savedInstanceState the preferences from a previous use of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.startPaintBtn = findViewById(R.id.btn_paint);
        this.loadPaintBtn = findViewById(R.id.btn_load);
        this.aboutBtn = findViewById(R.id.btn_info);

        // Hiding the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        // Giving the title a rainbow style gradient
        menuTitle = findViewById(R.id.tv_main_menu_title);
        TextPaint paint = menuTitle.getPaint();
        float width = paint.measureText(menuTitle.getText().toString());
        Shader textShader = new LinearGradient(0, 0, width, menuTitle.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        menuTitle.getPaint().setShader(textShader);
        menuTitle.setTextColor(Color.parseColor("#F97C3C"));

    } // end onCreate method

    public void onClick(View v) {
        Intent nextActivity;

        if(v == startPaintBtn) {
            nextActivity = new Intent(this, PaintActivity.class);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(nextActivity);
        } else if(v == loadPaintBtn) {
            this.loadPaintButtonPressed();
        } else if(v == aboutBtn) {
            nextActivity = new Intent(this, AboutActivity.class);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(nextActivity);
        } // end if statement
    } // end onClick method

    /**
     * Behavior to be performed when the load paint button is pressed
     */
    private void loadPaintButtonPressed() {
        Intent loadScreen = new Intent(this, LoadScreen.class);
        loadScreen.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        this.startActivity(loadScreen);
    } // end loadPaintButtonPressed

}
