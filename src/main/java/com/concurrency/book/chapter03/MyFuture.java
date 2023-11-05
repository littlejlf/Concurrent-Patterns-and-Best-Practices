package com.concurrency.book.chapter03;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyFuture<V> implements RunnableFuture<V> {
    private ReentrantLock rck;
    private Condition condition;
    private Callable<V> callable;
    private V result;
    //多个线程invoke 同一个Callcable 时的考量
    private boolean isFinished=false;
    public MyFuture(Callable callable) {
        rck= new ReentrantLock();
        condition= rck.newCondition();
        this.callable=callable;
    }

    @Override
    public void run() {
        try {
            result=callable.call();
            isFinished=true;
            rck.lock();
            condition.signalAll();
            rck.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {

        return isFinished;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        rck.lock();//必须获得锁（即 rck.lock()rck.unlock() 包裹 或者使用synchronized）
        if(!isDone()){
            //锁会被释放
            condition.await();
        }
        else {
            rck.unlock();
        }
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
