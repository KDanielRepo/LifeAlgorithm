package com.pkisi.dkuc.lifealgorithm;

import java.util.concurrent.atomic.AtomicBoolean;

public class Mutex {
    private final AtomicBoolean lock;
    private final Object mutex;

    public Mutex(AtomicBoolean lock) {
        this.lock = lock;
        this.mutex = new Object();
    }

    public void step(){
        if(lock.get()) synchronized (mutex){
            try{
                mutex.wait();
            }catch (InterruptedException ex){

            }
        }
    }
    public void lock(){
        lock.set(true);
    }
    public void unlock(){
        lock.set(false);

        synchronized (mutex){
            mutex.notify();
        }
    }
}
