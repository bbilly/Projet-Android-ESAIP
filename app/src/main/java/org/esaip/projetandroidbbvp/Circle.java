package org.esaip.projetandroidbbvp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.Random;
import java.util.Stack;

public class Circle extends SurfaceView implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener{

    Paint paint = new Paint();
    float x = 550;
    float y = 630;
    private Cercle cercleSelected = null;

    private GestureDetector gestureScanner;
    Stack<Cercle> CircleStack = new Stack<Cercle>();

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);


        gestureScanner = new GestureDetector(context,this);

    }

    //generator of random colors
    Random rnd = new Random();

    int randomColor;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);

        for(Cercle i:CircleStack){
            i.DrawCercle(canvas,paint);
        }

    }

    public void setCircleStack(Stack<Cercle> circleStack) {
        CircleStack = circleStack;
    }

    public Stack<Cercle> getCircleStack() {

        return CircleStack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureScanner.onTouchEvent(event)){
            return true;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                x = event.getX();
                y = event.getY();
                Boolean addCircle = false;
                randomColor = Color.rgb(rnd.nextInt(),rnd.nextInt(),rnd.nextInt());
                Cercle cercle=null;

                for(Cercle i:CircleStack) {
                    if (coordinateBelongToCircle(x, y,i)) {
                        cercle=i;

                        break;
                    }
                }
                if(cercle==null){
                    cercle = new Cercle(y,x,randomColor);
                    CircleStack.add(cercle);
                }
                cercleSelected=cercle;
                break;

            case MotionEvent.ACTION_MOVE:
                CircleStack.remove(cercleSelected);
                CircleStack.add(cercleSelected);
                cercleSelected.setX(event.getX());
                cercleSelected.setY(event.getY());
                break;
        }
        invalidate();
        return true;

    }
    private boolean coordinateBelongToCircle(float x, float y, Cercle circle) {
        if ((x - circle.getX()) * (x - circle.getX()) + (y - circle.getY()) * (y - circle.getY()) <= circle.getRadius() * circle.getRadius()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        for(Cercle i:CircleStack) {
            if (coordinateBelongToCircle(x, y,i)) {
                CircleStack.remove(i);
                invalidate();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        /*x = event.getX();
        y = event.getY();
        Boolean addCircle = false;
        randomColor = Color.rgb(rnd.nextInt(),rnd.nextInt(),rnd.nextInt());
        Cercle cercle=null;

        for(Cercle i:CircleStack) {
            if (coordinateBelongToCircle(x, y,i)) {
                cercle=i;
                cercleSelected=cercle;
                break;
            }
        }
        if(cercle==null){
            cercle = new Cercle(y,x,randomColor);
            CircleStack.add(cercle);
            cercleSelected=cercle;
        }
        invalidate();*/
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }
}