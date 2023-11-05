package com.concurrency.book.chapter03;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Mybarrier extends CyclicBarrier {
    private ReentrantLock rlk;
    private Condition condition;
    private volatile int count;
    private Runnable action;
    public Mybarrier( int count,Runnable runnable) {
        super(count);
        this.rlk = new ReentrantLock();
        this.condition = rlk.newCondition();
        this.count = count;
        this.action=runnable;
    }


    @Override
    public int await() throws InterruptedException {
        rlk.lock();
        System.out.println(Thread.currentThread().getName()+" lock");
        count--;
        if (count!=0){
            System.out.println(Thread.currentThread().getName()+" wait");
            //调用condition.await();会释放锁
            condition.await();
        }
        else {
            condition.signalAll();
            action.run();
        }
        rlk.unlock();
        //System.out.println("sum="+count);
        return count;
    }
}
