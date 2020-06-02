package com.example.powerpuffgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainGame2Activity extends AppCompatActivity {

    //frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    //Image
    private ImageView box, black, orange, pink;
    private Drawable imageBoxRight,imageBoxLeft;

    //Button
    private Button restartButton;

    //size
    private int boxSize;

    // Speed
    private int SPEED_BOX, SPEED_ORANGE, SPEED_PINK, SPEED_BLACK;

    //position
    private float boxX, boxY;
    private float blackX, blackY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;

    //score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    //Class
    private Timer timer;
    private Handler handler = new Handler();
    private SoundPlayerGame2 soundPlayer;

    //status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;

    @Override
    protected void onStop() {
        super.onStop();

        if(timer != null) {
            timer.cancel();
            timer = null;
            start_flg = false;
            restartButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catching_game);

        soundPlayer = new SoundPlayerGame2(this);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        restartButton = findViewById(R.id.restartButton);

        restartButton.setVisibility(View.INVISIBLE);

        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

        // Speed
        SPEED_BOX = getResources().getInteger(R.integer.SPEED_BOX);
        SPEED_ORANGE = getResources().getInteger(R.integer.SPEED_ORANGE);
        SPEED_PINK = getResources().getInteger(R.integer.SPEED_PINK);
        SPEED_BLACK = getResources().getInteger(R.integer.SPEED_BLACK);

        // High Score
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreLabel.setText(getString(R.string.highScore, highScore));
        scoreLabel.setText(getString(R.string.score, score));
    }

    public void changePos() {
        //AddTimer
        timeCount += 20;

        //Orange
        orangeY += SPEED_ORANGE;

        float orangeCentreX = orangeX + orange.getWidth() /2.0f;
        float orangeCentreY = orangeY + orange.getHeight()/2.0f;

        if (hitCheck(orangeCentreX,orangeCentreY)) {
            orangeY = frameHeight +100;
            score += 10;
            soundPlayer.playHitOrangeSound();
        }

        if (orangeY > frameHeight) {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //pink
        if(!pink_flg && timeCount%9000 ==0) {
            pink_flg = true;
            pinkY = -20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }

        if(pink_flg) {
            pinkY += SPEED_PINK;
            float pinkCenterX = pinkX + pink.getWidth() /2.0f;
            float pinkCenterY = pinkY + pink.getWidth()/2.0f;

            if(hitCheck(pinkCenterX, pinkCenterY)) {
                pinkY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
                soundPlayer.playHitPinkSound();
            }

            if(pinkY > frameHeight) {
                pink_flg = false;
            }
            pink.setX(pinkX);
            pink.setY(pinkY);
        }

        // Black
        blackY += SPEED_BLACK;

        float blackCenterX = blackX + black.getWidth() / 2.0f;
        float blackCenterY = blackY + black.getHeight() / 2.0f;

        if (hitCheck(blackCenterX, blackCenterY)) {
            blackY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);

            if (frameWidth <= boxSize) {
                gameOver(); //game over
            }

            soundPlayer.playHitBlackSound();
        }
        if (blackY > frameHeight) {
            blackY = -80;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }

        black.setX(blackX);
        black.setY(blackY);


        //move box
        if (action_flg) {
            //touching
            boxX += SPEED_BOX;
            box.setImageDrawable(imageBoxRight);
        } else {
            //releasing
            boxX -= SPEED_BOX;
            box.setImageDrawable(imageBoxLeft);
        }
        //check box position
        if(boxX <0) {
            boxX =0;
            box.setImageDrawable(imageBoxRight);
        }
        if(frameWidth-boxSize <boxX) {
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }
        box.setX(boxX);

        scoreLabel.setText(getString(R.string.score, score));
    }

    public boolean hitCheck (float x, float y) {
        if (boxX <= x && x <= boxX + boxSize && boxY <=y && y <=frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        //stop timer
        timer.cancel();
        timer = null;
        start_flg = false;

        // Before showing startLayout, sleep 1 second.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        box.setVisibility(View.INVISIBLE);
        black.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink.setVisibility(View.INVISIBLE);

        // Update High Score
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText(getString(R.string.highScore, highScore));

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.apply();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg= true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);
        if(frameHeight==0) {
            frameHeight = gameFrame.getHeight();
            frameWidth=gameFrame.getWidth();
            initialFrameWidth=frameWidth;

            boxSize=box.getHeight();
            boxX = box.getX();
            boxY=box.getY();

            frameWidth =initialFrameWidth;

            box.setX(0.0f);
            black.setY(3000.0f);
            orange.setY(3000.0f);
            pink.setY(3000.0f);

            blackY = black.getY();
            orangeY = orange.getY();
            pinkY = pink.getY();

            box.setVisibility(View.VISIBLE);
            black.setVisibility(View.VISIBLE);
            orange.setVisibility(View.VISIBLE);
            pink.setVisibility(View.VISIBLE);

            timeCount = 0;
            score = 0;
            scoreLabel.setText(getString(R.string.score, score));

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (start_flg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                changePos();
                            }
                        });
                    }
                }
            },0,20);
        }

    }

    public void restartGame(View View) {
        start_flg = true;
        restartButton.setVisibility(View.INVISIBLE);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    public void quitGame(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}