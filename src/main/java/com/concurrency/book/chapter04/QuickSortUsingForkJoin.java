package com.concurrency.book.chapter04;

import java.util.Random;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class QuickSortUsingForkJoin {

    public static void main(String[] args) {
        Random r = new Random();
         int NELEMS = 1000;
        int[] arr = new int[NELEMS];
        for (int i = 0; i < arr.length; i++) {
            int k = r.nextInt(NELEMS);
            arr[i] = k;
        }
        ForkJoinPool pool = new ForkJoinPool();
        QuickSortTask task = new QuickSortTask(arr, 0, arr.length - 1);
        pool.invoke(task);

        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    static class QuickSortTask extends RecursiveAction {
        private int[] arr;
        private int low;
        private int high;

        public QuickSortTask(int[] arr, int low, int high) {
            this.arr = arr;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (low < high) {
                int pivot = partition(arr, low, high);
                QuickSortTask leftTask = new QuickSortTask(arr, low, pivot - 1);
                QuickSortTask rightTask = new QuickSortTask(arr, pivot + 1, high);
                invokeAll(leftTask, rightTask);
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

        private void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}

