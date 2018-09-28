package com.company;

class MatrixThreads implements Runnable {

    int start;
    int stop;
    int a[][];
    int b[][];
    int d[][];

    public MatrixThreads(int[][] _a, int[][] _b, int[][] _d, int _start, int _stop){
        this.start = _start;
        this.stop = _stop;
        this.a = _a;
        this.b = _b;
        this.d = _d;

    }

    @Override
    public void run() {

        int size = 1000;
        for(int i = start;  i < stop; i++ ){
            for(int j = 0; j < size; j ++){
                for(int k = 0; k < size; k++){
                    d[i][j] = d[i][j] + (a[i][k] * b[k][j]);
                }
            }
        }
    }
}

class MatrixThreads2 implements Runnable{

    @Override
    public void run() {


    }
}



public class Main {

    public static int getRand(int min, int max) {
        int x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    public static int[][] multiplySerial(int[][] a, int[][] b) {
        int size = a.length;
        int answer[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    answer[i][j] = answer[i][j] + (a[i][k] * b[k][j]);
                }
            }
        }
        return answer;
    }

    public static int[][] multiplyParallel(int[][] a, int[][] b) {
        int size = a.length;
        int answer[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    answer[i][j] = answer[i][j] + (a[i][k] * b[k][j]);
                }
            }
        }
        return answer;
    }

    public static boolean checkAnswer(int[][] a, int[][] b) {
        int size = a.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }
        return true;
    }

    public static void main(String[] args)  {
        int size = 1000;




        int a[][] = new int[size][size];
        int b[][] = new int[size][size];




        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = getRand(1, 1000);
                b[i][j] = getRand(1, 1000);
            }
        }
        long startTime = System.nanoTime();
        int c[][] = multiplySerial(a, b);
        long endTime = System.nanoTime();
        long serialTime = endTime - startTime;
        System.out.println("Serial Time " + serialTime + " ns");

        startTime = System.nanoTime();
        // filler, make either a new class that extends thread, or have this one extend thread
        // figure out how to split work up into at least 2 more threads
        int d[][] = new int[size][size];
        MatrixThreads work1 = new MatrixThreads( a, b, d, 0, 500);
        MatrixThreads work2 = new MatrixThreads( a, b, d, 500, 1000);
        Thread thread1 = new Thread(work1);
        Thread thread2 = new Thread(work2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        endTime = System.nanoTime();
        long parallelTime = endTime - startTime;
        System.out.println("Parallel Time " + parallelTime + " ns");
        long diff = (parallelTime - serialTime);
        long percent = diff * 100 / serialTime;
        System.out.printf("Percent change %.2f \n", (float) percent);
        if (checkAnswer(c, d)) {
            System.out.println("Valid Answer");
        } else {
            System.out.println("Invalid Answer");
        }
    }
}
