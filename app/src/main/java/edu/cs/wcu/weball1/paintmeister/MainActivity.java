package edu.cs.wcu.weball1.paintmeister;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * MainActivity.java
 * <p>
 * The entry point int our application. A splash screen that waits for five
 * seconds and then continues to the main menu.
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version (May 4, 2020)
 */
public class MainActivity extends AppCompatActivity {
    /**
     * A constant value representing the length of the splash screen
     **/
    private static int PAUSE = 5000;
    /**
     * Creating a handler reference
     **/
    private Handler handler;


    /**
     * The initial point in the application.
     *
     * @param savedInstanceState - The saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        } // end if

    } // end onCreate method

    /**
     * This method deals with when the app is at a stopped state and
     * then resumes.
     */
    protected void onStart() {
        super.onStart();

        //Creating a new handler
        handler = new Handler();
        // Setting the handler to the delayed time amount
        handler.postDelayed(runner, PAUSE);

    } // end onStart method

    /**
     * Creating a new runnable object
     **/
    private final Runnable runner = new Runnable() {
        /**
         * This is a required method that is called when runnable is deployed.
         */
        @Override
        public void run() {
            goToNextScreen();
            //remove this screen from the backstack after delay
            finish();
        }
    }; // end runnable

    /**
     * This method allows the application to go to the next screen
     */
    private void goToNextScreen() {
        Intent mainMenu = new Intent(this, MainMenuActivity.class);
        this.startActivity(mainMenu);

        // stop the user from going back to the splash screen
        this.finish();
    } // end goToNextScreen method
} // end MainActivity class
