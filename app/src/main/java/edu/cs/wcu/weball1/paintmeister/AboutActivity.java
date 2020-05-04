package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

/**
 * AboutActivity.java
 *
 * The screen that will display details about the developers (along with our lovely smiles)
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * Called when this activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    } // end onCreate

    /**
     * Allows us to select an item in our AppBar.
     * @param item The item selected by the user.
     * @return true to consume method for our own use, false otherwise.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } // end if

        return super.onOptionsItemSelected(item);

    } // end onOptionsItemSelected

} // end AboutActivity class
