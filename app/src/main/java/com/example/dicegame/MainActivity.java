package com.example.dicegame;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;
import android.util.Log;
import android.widget.CheckBox;

public class MainActivity extends Activity {

    //background num values
    private int rand = 0;
    private int value = 1;
    private int progress = 0;
    // program control
    private boolean running;
    private boolean wasRunning;
    CheckBox hardcorecb;
    private TextView roll;
    private TextView chance;
    private TextView dice;
    //dice imagine control (no longer used)

    //array for controlling dice size and info
    final int[] diceSize = {4,6,8,10,12,20,100};
    final String[] sdice ={"The D4!","The D6!","The D8!","The D10!","The D12!!","The D20!!!","The D100!!!"};
    private double dchance = .25;
    //media control
    private MediaPlayer fail;
    private MediaPlayer success;
    private MediaPlayer msuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {

            // Get the previous state of the dice
            // if the activity has been
            // destroyed and recreated.
            progress
                    = savedInstanceState
                    .getInt("progress");
            rand
                    = savedInstanceState
                    .getInt("rand");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        runTimer();
    }

    // Save the state of the dice
    // if it's about to be destroyed.
    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState)
    {
        savedInstanceState
                .putInt("progress", progress);
        savedInstanceState
                .putInt("rand", rand);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    // If the activity is paused,
    // stop the roll.
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the dice
    // again if it was running previously.
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Start the dice rolling

    // when the Start button is clicked.
    public void onClickStart(View view)
    {
        running = true;
    }






    // Sets the visible dice.
    // The runTimer() method uses a Handler
    // to randomise the dice and
    // update the text view.
    private void runTimer()
    {

        // Get the text view.
        roll = (TextView)findViewById(R.id.roll);
        chance = (TextView)findViewById(R.id.chance);
        dice = (TextView)findViewById(R.id.dice);
        // Initialising dice
        //dice = findViewById(R.id.diceImg);
        success = MediaPlayer.create(this, R.raw.minor_success);
        fail = MediaPlayer.create(this, R.raw.fail);
        msuccess = MediaPlayer.create(this, R.raw.major_success);
        hardcorecb = (CheckBox) findViewById(R.id.hardcore);
        //hardcorecb.setOnClickListener(this);
        // Creates a new Handler
        final Handler handler
                = new Handler();



        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                // If running is true, randomise the
                // rand variable.
                if (running) {
                    Random random = new Random();
                    rand = random.nextInt(100);
                    rand++;



                    value = rand % diceSize[progress] + 1;
                    String sRoll = Integer.toString(value);

                    // Set the text view text.
                    roll.setText(sRoll);
                    //dice.setImageResource(diceArr[(value % 6)]);
                }


                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 100);
            }
        });
    }
    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickStop(View view)
    {
        // checks if running to prevent double clicks resetting
        if (running){
            running = false;
            //updates the dice, plays sound effects and deals with victory
            if (diceSize[progress]==value){
                if (progress==6){
                    msuccess.start();
                    Log.i("Victory",Integer.toString(value));
                    dice.setText("Now to try Hardcore!!!");
                }
                else {
                    Log.i("sucess",Integer.toString(value));
                    success.start();
                    progress++;
                    dice.setText(sdice[progress]);
                    dchance = dchance /diceSize[progress];
                    String schance = Double.toString(dchance);
                    Log.i("chance",schance);
                    chance.setText(schance+"%");
                }

            }
            //resets progress on hardcore
            else if (hardcorecb.isChecked()){
                Log.i("fail",Integer.toString(value));
                if (progress!=0){
                    fail.start();
                    progress=0;
                    dice.setText(sdice[progress]);
                    dchance = 0.25;
                    String schance = Double.toString(dchance);
                    chance.setText(schance+"%");
                }
            }
        }

    }

    // Reset the game when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view)
    {
        running = false;
        rand = 1;
        progress = 0;
        Log.i("reset",Integer.toString(progress));
        dice.setText(sdice[progress]);
        dchance = .25;
        String schance = Double.toString(dchance);
        chance.setText(schance+"%");
    }
}