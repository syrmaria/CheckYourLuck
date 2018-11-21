package com.syrovama.checkyourluck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String SCORE = "SCORE";
    public static final String ATTEMPTS = "ATTEMPTS";
    public static final String SELECTED_POSITION ="SELECTED_POSITION";
    public static final String BALL_POSITION ="BALL_POSITION";
    private ArrayList<ImageButton> mImageButtonArrayList = new ArrayList<>();
    private Button mResetButton;
    private TextView mResultTextView;
    int ballPosition;
    int selectedPosition = -1;
    int score;
    int attempts;
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResetButton = findViewById(R.id.reset_button);
        mResultTextView = findViewById(R.id.result_text);
        ImageButton imageButton = findViewById(R.id.first);
        mImageButtonArrayList.add(imageButton);
        imageButton = findViewById(R.id.second);
        mImageButtonArrayList.add(imageButton);
        imageButton = findViewById(R.id.third);
        mImageButtonArrayList.add(imageButton);

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE);
            attempts = savedInstanceState.getInt(ATTEMPTS);
            ballPosition = savedInstanceState.getInt(BALL_POSITION);
            selectedPosition = savedInstanceState.getInt(SELECTED_POSITION);
            if (selectedPosition == -1) {
                resetGame(mResetButton);
            } else {
                showSavedGameState();
            }
            String resultText = String.format(getString(R.string.resultText), score, attempts);
            mResultTextView.setText(resultText);
        } else {
            resetGame(mResetButton);
        }
    }

    public void resetGame(View view) {
        selectedPosition = -1;
        mResetButton.setEnabled(false);
        ballPosition = random.nextInt(3);
        for (ImageButton imageButton: mImageButtonArrayList) {
            imageButton.setImageResource(R.drawable.thimble);
            imageButton.setEnabled(true);
        }
    }

    private void showSavedGameState() {
        mResetButton.setEnabled(true);
        for (ImageButton imageButton: mImageButtonArrayList) {
            setImageForButton(imageButton);
            imageButton.setEnabled(false);
        }
    }

    public void checkTheBall(View view) {
        ImageButton selectedImageButton = (ImageButton) view;
        selectedPosition = mImageButtonArrayList.indexOf(selectedImageButton);
        if (selectedPosition == ballPosition) {
            score++;
        }
        setImageForButton(selectedImageButton);
        for (ImageButton imageButton: mImageButtonArrayList) {
            imageButton.setEnabled(false);
        }
        mResetButton.setEnabled(true);
        attempts++;
        String resultText = String.format(getString(R.string.resultText), score, attempts);
        mResultTextView.setText(resultText);
    }

    public void setImageForButton(ImageButton imageButton) {
        if (selectedPosition == mImageButtonArrayList.indexOf(imageButton)) {
            if (selectedPosition == ballPosition) {
                imageButton.setImageResource(R.drawable.success);
            } else {
                imageButton.setImageResource(R.drawable.fail);
            }
        } else {
            imageButton.setImageResource(R.drawable.thimble);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE, score);
        outState.putInt(ATTEMPTS, attempts);
        outState.putInt(SELECTED_POSITION, selectedPosition);
        outState.putInt(BALL_POSITION, ballPosition);
    }
}
