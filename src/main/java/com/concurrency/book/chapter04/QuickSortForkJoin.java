package com.concurrency.book.chapter04;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class QuickSortForkJoin {

    public static final int NELEMS = 10000;

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Random r = new Random();

        int[] arr = new int[NELEMS];
        for (int i = 0; i < arr.length; i++) {
            int k = r.nextInt(NELEMS);
            arr[i] = k;
        }
        int[] arr01=Arrays.copyOfRange(arr,0,arr.length-1);
        ForkJoinQuicksortTask forkJoinQuicksortTask = new ForkJoinQuicksortTask(arr,
                0, arr.length - 1);
        final int[] result = forkJoinPool.invoke(forkJoinQuicksortTask);
        long end=System.currentTimeMillis();
        System.out.println("cost="+(end-start));
        System.out.println(Arrays.toString(result));
        long s2=System.currentTimeMillis();
        long end2=System.currentTimeMillis();
        System.out.println("cost="+(end2-s2));
        System.out.println(Arrays.toString(arr01));
    }
}

class ForkJoinQuicksortTask extends RecursiveTask<int[]> {
    public static final int LIMIT = 100;

    int[] arr;
    int left;
    int right;

    public ForkJoinQuicksortTask(int[] arr) {
        this(arr, 0, arr.length - 1);
    }

    public ForkJoinQuicksortTask(int[] arr, int left, int right) {
        this.arr = arr;
        this.left = left;
        this.right = right;
    }

    @Override
    protected int[] compute() {
        if (isItASmallArray()) {
            Arrays.sort(arr, left, right + 1);
            return arr;
        } else {
            List<ForkJoinTask<int[]>> tasks = Lists.newArrayList();
            int pivotIndex = partition(arr, left, right);

/*            int[] arr0 = Arrays.copyOfRange(arr, left, pivotIndex-1);
            int[] arr1 = Arrays.copyOfRange(arr, pivotIndex + 1, right + 1);*/
/*            tasks.add(new ForkJoinQuicksortTask(arr0));
            tasks.add(new ForkJoinQuicksortTask(arr1));*/

            tasks.add(new ForkJoinQuicksortTask(arr, left, pivotIndex-1));
            tasks.add(new ForkJoinQuicksortTask(arr, pivotIndex + 1, right ));
//            boolean pivotElemCopied = false;
            for (final ForkJoinTask<int[]> task : invokeAll(tasks)) {
                int[] taskResult = task.join();
/*                if (!pivotElemCopied) {
                    result = Ints.concat(taskResult, result);
                    pivotElemCopied = true;
                } else {
                    result = Ints.concat(result, taskResult);
                }*/
            }
            return arr;
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    void swap(int[] a, int p, int r) {
        int t = a[p];
        a[p] = a[r];
        a[r] = t;
    }

    private boolean isItASmallArray() {
        return right - left <= LIMIT;
    }
}
