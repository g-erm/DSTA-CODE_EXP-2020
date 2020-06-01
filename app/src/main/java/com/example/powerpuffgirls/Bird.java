package com.example.powerpuffgirls;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.powerpuffgirls.GameView.screenRatioX;
import static com.example.powerpuffgirls.GameView.screenRatioY;

public class Bird {

    public int speed = 20;
    public boolean wasShot = true;
    int x, y, width, height;
    Bitmap bird1;

    Bird (Resources res) {
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);

        width = bird1.getWidth();
        height = bird1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);

        y = -height;
    }

    Bitmap getBird() {
        return bird1;
    }

    Rect getCollisionShape() {
        return new Rect(x,y,x+width, y+height);
    }
}
