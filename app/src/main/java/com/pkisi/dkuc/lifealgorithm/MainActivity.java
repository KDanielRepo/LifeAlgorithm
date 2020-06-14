package com.pkisi.dkuc.lifealgorithm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Cell[][] cells;
    private DrawerLayout drawerLayout;
    Stage stage;
    MainThread mainThread;
    EditText speedField;
    Integer animationSpeed = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);
        speedField = findViewById(R.id.speedField);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.naviagtion_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mainThread.getMutex().lock();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(speedField.getText().toString()!=null && !speedField.getText().toString().isEmpty()){
                    animationSpeed = Integer.parseInt(speedField.getText().toString());
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();

        stage = findViewById(R.id.mainStage);
        cells = stage.getCells();

        mainThread = new MainThread();
        mainThread.start();

    }

    public class MainThread extends Thread{
        private final Mutex mutex = new Mutex(new AtomicBoolean());
        public MainThread(){
            mutex.lock();
        }
        public Mutex getMutex(){
            return mutex;
        }

        @Override
        public void run() {
            while (!isInterrupted()){
                mutex.step();
                int[][] neighbours = new int[24][17];
                int[][] alive = new int[24][17];
                int[][] dead = new int[24][17];


                for (int i = 0; i < 24; i++) {
                    for (int j = 0; j < 17; j++) {

                        for (int k = -1; k < 2; k++) {
                            for (int l = -1; l < 2; l++) {
                                if ((i + k) > 0 && (i + k) < 24 && (j + l) > 0 && (j + l) < 17) {
                                    if (cells[i + k][j + l].isAlive()) {
                                        if (i + k != i || j + l != j) {
                                            neighbours[i][j]++;
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
                for (int i = 0; i < 24; i++) {
                    for (int j = 0; j < 17; j++) {
                        if (cells[i][j].isAlive() && neighbours[i][j] > 3) {
                            dead[i][j] = 1;
                        }
                        if (cells[i][j].isAlive() && neighbours[i][j] < 2) {
                            dead[i][j] = 1;
                        }
                        if (!cells[i][j].isAlive() && neighbours[i][j] == 3) {
                            alive[i][j] = 1;
                        }
                        if (dead[i][j] == 1) {
                            cells[i][j].setAlive(false);
                        }
                        if (alive[i][j] == 1) {
                            cells[i][j].setAlive(true);
                        }
                    }
                }

                try {
                    Thread.sleep(animationSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (this) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stage.setCells(cells);
                            stage.invalidate();
                        }
                    });
                }
            }
        }
    }

    public void clearCells(){
        cells = new Cell[24][17];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 17; j++) {
                Cell c = new Cell(false, j, i);
                cells[i][j] = c;
            }
        }
        stage.setCells(cells);
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.resume:
                drawerLayout.closeDrawer(GravityCompat.START);
                cells = stage.getCells();
                mainThread.getMutex().unlock();
                break;
            case R.id.restart:
                drawerLayout.closeDrawer(GravityCompat.START);
                clearCells();
                stage.invalidate();
                break;
            case R.id.exit:

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
