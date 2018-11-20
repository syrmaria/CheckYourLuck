package com.syrova_ma.checkyourluck;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String SCORE = "SCORE";
    public static final String ATTEMPTS = "ATTEMPTS";
    private ArrayList<ImageButton> buttonArrayList = new ArrayList<ImageButton>();
    private ImageButton containsBall;
    private Button resetButton;
    private TextView result;
    private int score;
    private int attempts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result_text);
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE);
            attempts = savedInstanceState.getInt(ATTEMPTS);
            String resultText = String.format(getString(R.string.resultText), score, attempts);
            result.setText(resultText);
        }
        resetButton = findViewById(R.id.reset_button);
        resetButton.setEnabled(false);
        resetGame(resetButton);
    }

    public void resetGame(View v) {
        ImageButton tmp = findViewById(R.id.first);
        setImageButton(tmp);
        tmp = findViewById(R.id.second);
        setImageButton(tmp);
        tmp = findViewById(R.id.third);
        setImageButton(tmp);
        Random ran = new Random();
        int num = ran.nextInt(3);
        containsBall = buttonArrayList.get(num);
    }

    private void setImageButton(ImageButton imageButton) {
        imageButton.setImageResource(R.drawable.button_picture);
        imageButton.setEnabled(true);
        buttonArrayList.add(imageButton);
    }

    public void checkTheBall(View v) {
        ImageView current = (ImageView) v;
        if (current.getId()==containsBall.getId()) {
            current.setImageResource(R.drawable.success);
            score++;
        } else {
            current.setImageResource(R.drawable.fail);
        }
        for (ImageView imageView: buttonArrayList) {
            imageView.setEnabled(false);

        }
        resetButton.setEnabled(true);
        attempts++;
        String resultText = String.format(getString(R.string.resultText), score, attempts);
        result.setText(resultText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCORE, score);
        outState.putInt(ATTEMPTS, attempts);
    }
}
