package com.pkisi.dkuc.lifealgorithm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Stage extends SurfaceView {
    private Paint paint;

    private Cell[][] cells;// = new Cell[gridHeight][gridWidth];
    private boolean addition;
    private int gridWidth;
    private int gridHeight;
    private int x;
    private int y;

    public Stage(Context context, AttributeSet attrs) {
        super(context, attrs);
        gridWidth = Resources.getSystem().getDisplayMetrics().widthPixels/32;
        gridHeight = Resources.getSystem().getDisplayMetrics().heightPixels/32;
        cells = new Cell[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                Cell c = new Cell(false, j, i);
                cells[i][j] = c;
            }
        }
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2f);

    }

    protected void onDraw(Canvas canvas) {
        int gridWidthLines = canvas.getWidth() / 32;
        if (canvas.getWidth() % 32 != 0 && gridWidthLines < (float) canvas.getWidth() / 32) {
            gridWidthLines++;
        }
        int gridHeightLines = canvas.getHeight() / 32;


        if (cells != null) {
            for (int i = 0; i < gridHeight; i++) {
                for (int j = 0; j < gridWidth; j++) {
                    if (cells[i][j] != null && cells[i][j].isAlive()) {
                        canvas.drawRect(cells[i][j].getX() * 32, cells[i][j].getY() * 32, (cells[i][j].getX() + 1) * 32, (cells[i][j].getY() + 1) * 32, paint);
                    }
                }
            }
                for (int i = 0; i < gridWidthLines; i++) {
                    canvas.drawLine(32 * i, 0, 32 * i, canvas.getHeight(), paint);
                }
                for (int i = 0; i < gridHeightLines; i++) {
                    canvas.drawLine(0, 32 * i, canvas.getWidth(), 32 * i, paint);
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

    public boolean isAddition() {
        return addition;
    }

    public void setAddition(boolean addition) {
        this.addition = addition;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int)event.getX()/32;
        y = (int)event.getY()/32;

        if(x<gridWidth && y<gridHeight){
            if(addition){
                cells[y][x].setAlive(true);
            }else{
                cells[y][x].setAlive(false);
            }
        }
        invalidate();
        return true;
    }
}
