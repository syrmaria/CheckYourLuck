package com.syrovama.checkyourluck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String SCORE = "SCORE";
    public static final String ATTEMPTS = "ATTEMPTS";
    public static final String SELECTED_POSITION ="SELECTED_POSITION";
    public static final String BALL_POSITION ="BALL_POSITION";
    public static final String FLAG = "FLAG";
    private ArrayList<ImageButton> mImageButtonArrayList = new ArrayList<>();
    private Button mResetButton;
    private TextView mResultTextView;
    int ballPosition;
    int selectedPosition = -1;
    int score;
    int attempts;
    boolean isAllShown = false;
    Random random = new Random();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isAllShown = true;
            showAllImages();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (savedInstanceState != null) {
            restoreSavedData(savedInstanceState);
            showScore();
            if (selectedPosition == -1) {
                resetGame(mResetButton);
            } else {
                showSavedGameState();
            }
        } else {
            resetGame(mResetButton);
        }
    }

    private void initViews() {
        mResetButton = findViewById(R.id.reset_button);
        mResultTextView = findViewById(R.id.result_text);
        ImageButton imageButton = findViewById(R.id.first);
        mImageButtonArrayList.add(imageButton);
        imageButton = findViewById(R.id.second);
        mImageButtonArrayList.add(imageButton);
        imageButton = findViewById(R.id.third);
        mImageButtonArrayList.add(imageButton);
    }

    private void restoreSavedData(Bundle state) {
        score = state.getInt(SCORE);
        attempts = state.getInt(ATTEMPTS);
        ballPosition = state.getInt(BALL_POSITION);
        selectedPosition = state.getInt(SELECTED_POSITION);
        isAllShown = state.getBoolean(FLAG);
    }

    private void showScore() {
        String resultText = String.format(getString(R.string.resultText), score, attempts);
        mResultTextView.setText(resultText);
    }

    private void showSavedGameState() {
        mResetButton.setEnabled(true);
        showAllImages();
        if (!isAllShown) mResetButton.postDelayed(runnable, 2000);
    }

    public void resetGame(View view) {
        selectedPosition = -1;
        isAllShown = false;
        mResetButton.removeCallbacks(runnable);
        mResetButton.setEnabled(false);
        ballPosition = random.nextInt(3);
        initImages();
    }

    private void initImages() {
        for (ImageButton imageButton: mImageButtonArrayList) {
            imageButton.setImageResource(R.drawable.thimble);
            imageButton.setEnabled(true);
        }
    }

    private void showAllImages() {
        for (ImageButton imageButton: mImageButtonArrayList) {
            int position = mImageButtonArrayList.indexOf(imageButton);
            if (position == selectedPosition) {
                openSelectedImageButton(imageButton);
            } else {
                if (isAllShown) {
                    if (ballPosition == position) {
                        imageButton.setImageResource(R.drawable.success);
                    } else {
                        imageButton.setImageResource(R.drawable.fail);
                    }
                } else {
                    imageButton.setImageResource(R.drawable.thimble);
                }
            }
            imageButton.setEnabled(false);
        }
    }

    public void openSelectedImageButton(ImageButton imageButton) {
        if (selectedPosition == ballPosition) {
            imageButton.setImageResource(R.drawable.success);
        } else {
            imageButton.setImageResource(R.drawable.fail);
        }
    }

    public void checkTheBall(View view) {
        ImageButton selectedImageButton = (ImageButton) view;
        selectedPosition = mImageButtonArrayList.indexOf(selectedImageButton);
        if (selectedPosition == ballPosition) {
            score++;
        }
        attempts++;
        openSelectedImageButton(selectedImageButton);
        for (ImageButton imageButton: mImageButtonArrayList) {
            imageButton.setEnabled(false);
        }
        showScore();
        mResetButton.setEnabled(true);
        mResetButton.postDelayed(runnable, 2000);
        Log.d(TAG, "Runnable with delay sent, flag = " + isAllShown);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE, score);
        outState.putInt(ATTEMPTS, attempts);
        outState.putInt(SELECTED_POSITION, selectedPosition);
        outState.putInt(BALL_POSITION, ballPosition);
        outState.putBoolean(FLAG, isAllShown);
    }

}
