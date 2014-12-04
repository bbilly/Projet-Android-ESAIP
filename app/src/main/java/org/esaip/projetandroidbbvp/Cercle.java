package org.esaip.projetandroidbbvp;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Cercle {
    float x;
    float y;
    int color;

    int radius;


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public int getRadius() {
        return radius;
    }

    public Cercle(float y, float x,int randomcolor) {
        this.y = y;
        this.x = x;
        Random r = new Random();

        this.radius = r.nextInt(200 - 50) + 50;
        this.color = randomcolor;
    }
    public void DrawCercle(Canvas canvas,Paint paint){
        paint.setColor(color);
        canvas.drawCircle(x,y,radius, paint);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
