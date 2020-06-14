package com.pkisi.dkuc.lifealgorithm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Stage extends SurfaceView {
    private Paint paint;
    private Cell[][] cells = new Cell[24][17];
    int x;
    int y;
//60 x 34
    public Stage(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 17; j++) {
                Cell c = new Cell(false, j, i);
                cells[i][j] = c;
            }
        }
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2f);

    }

    protected void onDraw(Canvas canvas) {
        int widthLines = canvas.getWidth() / 64;
        if (canvas.getWidth() % 64 != 0 && widthLines < (float) canvas.getWidth() / 64) {
            widthLines++;
        }
        int heightLines = canvas.getHeight() / 64;


        if (cells != null) {
            for (int i = 0; i < 24; i++) {
                for (int j = 0; j < 17; j++) {
                    if (cells[i][j] != null && cells[i][j].isAlive()) {
                        canvas.drawRect(cells[i][j].getX() * 64, cells[i][j].getY() * 64, (cells[i][j].getX() + 1) * 64, (cells[i][j].getY() + 1) * 64, paint);
                    }
                }
            }
                for (int i = 0; i < widthLines; i++) {
                    canvas.drawLine(64 * i, 0, 64 * i, canvas.getHeight(), paint);
                }
                for (int i = 0; i < heightLines; i++) {
                    canvas.drawLine(0, 64 * i, canvas.getWidth(), 64 * i, paint);
                }
        }
    }


    public void surfaceCreated(SurfaceHolder arg0) {
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Handler handler = new Handler();
        x = (int)event.getX()/64;
        y = (int)event.getY()/64;
        /*Runnable r = new Runnable() {
            @Override
            public void run() {
                cells[y][x].setAlive(false);
            }
        };
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            handler.postDelayed(r, ViewConfiguration.getLongPressTimeout());
        }
        if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_MOVE){
        }*/
        cells[y][x].setAlive(true);
        // NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE NAPRAW TE WSPOZRZEDNE
        invalidate();
        return true;
    }
}
