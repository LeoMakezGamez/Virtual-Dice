package com.example.virtualdice;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView totalScoreText;
    Button button;
    Button buttonAddDice;
    Button buttonRemoveDice;
    LinearLayout diceContainer;
    Random random = new Random();
    Handler handler = new Handler();
    int diceCount = 1; // Initially one die
    int[] diceImages = {
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.scoreText);
        totalScoreText = findViewById(R.id.totalScoreText);
        button = findViewById(R.id.buttonRoll);
        buttonAddDice = findViewById(R.id.buttonAddDice);
        buttonRemoveDice = findViewById(R.id.buttonRemoveDice);
        diceContainer = findViewById(R.id.diceContainer);

        updateDiceDisplay();  // Update dice display initially

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffleDice();
            }
        });

        buttonAddDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diceCount < 4) {
                    diceCount++;
                    updateDiceDisplay();
                }
            }
        });

        buttonRemoveDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diceCount > 1) {
                    diceCount--;
                    updateDiceDisplay();
                }
            }
        });
    }

    private void updateDiceDisplay() {
        // Clear existing dice
        diceContainer.removeAllViews();

        // Dynamically create ImageViews for dice
        for (int i = 0; i < diceCount; i++) {
            ImageView diceImageView = new ImageView(this);
            int size = 250 - (diceCount - 1) * 30; // Decrease size as dice count increases

            // Set the size of the dice
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

            // Set margin between the dice
            int margin = 5; // Adjust the margin size to your preference
            params.setMargins(margin, 5, margin, 5);  // Apply margin for left and right spacing
            diceImageView.setLayoutParams(params);

            // Set the default dice image
            diceImageView.setImageResource(diceImages[0]);

            // Add the dice to the container
            diceContainer.addView(diceImageView);
        }

        textView.setText("Dice: " + diceCount);
    }


    private void shuffleDice() {
        final int shuffleDuration = 1000;
        final int shuffleInterval = 100;
        final int[] currentShuffleCount = {0};

        handler.post(new Runnable() {
            @Override
            public void run() {
                int randomIndex = random.nextInt(diceImages.length);
                for (int i = 0; i < diceContainer.getChildCount(); i++) {
                    ImageView diceImageView = (ImageView) diceContainer.getChildAt(i);
                    diceImageView.setImageResource(diceImages[randomIndex]);
                }

                currentShuffleCount[0] += shuffleInterval;

                if (currentShuffleCount[0] < shuffleDuration) {
                    handler.postDelayed(this, shuffleInterval);
                } else {
                    showFinalDiceRoll();
                }

            }
        });
    }

    private void showFinalDiceRoll() {
        int totalScore = 0;
        for (int i = 0; i < diceContainer.getChildCount(); i++) {
            int finalScore = random.nextInt(6) + 1;
            totalScore += finalScore;
            ImageView diceImageView = (ImageView) diceContainer.getChildAt(i);
            diceImageView.setImageResource(diceImages[finalScore - 1]);
        }

        totalScoreText.setText("Total: " + totalScore);
        Toast.makeText(MainActivity.this, "Total Score: " + totalScore, Toast.LENGTH_SHORT).show();
    }
}
