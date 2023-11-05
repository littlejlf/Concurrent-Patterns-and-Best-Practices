package com.concurrency.book.chapter04;

public class ThreadInterruption {
    public static void main(String[] args) throws InterruptedException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(40000);
                } catch (InterruptedException e) {
                    /*
                    * 线程可能已经在某个地方捕获了 InterruptedException 异常，这将会清除线程的中断状态
                    * 。InterruptedException 在一些阻塞操作中会被抛出，如果线程捕获了这个异常，那么中断状态会被清除
                    * 。如果线程在捕获异常后未重新设置中断状态，isInterrupted() 可能会返回 false*/
                    System.out.println("I was woken up!"+Thread.currentThread().getState());

                }
                System.out.println("Am done");
            }
        };
        Thread t = new Thread(r);
        t.start();
        t.interrupt();
        System.out.println("I was woken up!!!"+t.getState()+t.isInterrupted());
        Thread.sleep(1000);
        t.join();
        System.out.println("After run excuted t is"+t.getState());

    }
}
