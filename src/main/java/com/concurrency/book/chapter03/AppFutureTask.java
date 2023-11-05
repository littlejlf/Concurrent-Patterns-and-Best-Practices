package com.concurrency.book.chapter03;

import java.time.ZonedDateTime;
import java.util.concurrent.*;

public class AppFutureTask {

    private static RunnableFuture<String> createAFutureTask() {
        final Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws InterruptedException {
                Thread.sleep(4000);
                return "Hello World";
            }
        };
        //return new FutureTask<String>(callable);
        return new MyFuture<String>(callable);
    }

    private static void timeTheCall(final RunnableFuture<String> future) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println(future.get());
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time " + elapsedTime);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

      final RunnableFuture<String> future = createAFutureTask();
        final Thread thread = new Thread(future);

        thread.start();

        timeTheCall(future);
        timeTheCall(future);
        timeTheCall(future);
    }
}
