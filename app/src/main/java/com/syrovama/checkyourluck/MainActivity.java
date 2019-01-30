package com.syrovama.checkyourluck;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView;
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
    private ArrayList<ImageView> mImageViewArrayList = new ArrayList<>();
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
        ImageView imageView = findViewById(R.id.first);
        mImageViewArrayList.add(imageView);
        imageView = findViewById(R.id.second);
        mImageViewArrayList.add(imageView);
        imageView = findViewById(R.id.third);
        mImageViewArrayList.add(imageView);
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
        for (ImageView imageView: mImageViewArrayList) {
            imageView.setImageResource(R.drawable.thimble);
            imageView.setTag(R.drawable.thimble);
            imageView.setEnabled(true);
        }
    }

    private void showAllImages() {
        for (ImageView imageView: mImageViewArrayList) {
            int position = mImageViewArrayList.indexOf(imageView);
            if (position == selectedPosition) {
                openSelectedImageView(imageView);
            } else {
                int image;
                if (isAllShown) {
                    if (ballPosition == position) {
                        image = R.drawable.success;
                    } else {
                        image = R.drawable.fail;
                    }
                } else {
                    image = R.drawable.thimble;
                }
                changeImage(imageView, image);
            }
            imageView.setEnabled(false);
        }
    }

    public void openSelectedImageView(ImageView imageView) {
        if (selectedPosition == ballPosition) {
            changeImage(imageView, R.drawable.success);
        } else {
            changeImage(imageView, R.drawable.fail);
        }
    }

    public void checkTheBall(View view) {
        ImageView selectedImageView = (ImageView) view;
        selectedPosition = mImageViewArrayList.indexOf(selectedImageView);
        if (selectedPosition == ballPosition) {
            score++;
        }
        attempts++;
        openSelectedImageView(selectedImageView);
        for (ImageView imageView: mImageViewArrayList) {
            imageView.setEnabled(false);
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

    public void changeImage(ImageView view, int imageRes) {
        Object tag = view.getTag();
        if (tag == null) view.setImageResource(imageRes);
        else if (imageRes != (int)tag) {
            if ((int)tag == R.drawable.thimble) animateImageChange(view, imageRes);
            else view.setImageResource(imageRes);
        }
        view.setTag(imageRes);
    }

    private void animateImageChange(final ImageView view, final int imageRes) {
        Animation animSlide = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                view.setImageResource(imageRes);
            }
        });
        view.startAnimation(animSlide);
    }
}
