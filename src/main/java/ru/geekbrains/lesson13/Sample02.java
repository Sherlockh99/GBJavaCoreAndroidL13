package ru.geekbrains.lesson13;

import java.util.concurrent.Semaphore;

public class Sample02 {

    public static void main(String[] args) {

        /*
        Параметр permits в new Semaphore указывает на количество допустимых разрешений для доступа к ресурсу.
        Параметр fair во втором конструкторе позволяет установить очередность получения доступа.
        Если он равен true, то разрешения будут предоставляться ожидающим потокам в том порядке,
        в каком они запрашивали доступ. Если же он равен false,
        то разрешения будут предоставляться в неопределенном порядке.
         */


        SampleClass sampleClass = new SampleClass();
        Semaphore semaphore = new Semaphore(1, true);

        new Thread(new MyThread(sampleClass, semaphore), "Thread #1").start();
        new Thread(new MyThread(sampleClass, semaphore), "Thread #2").start();
        new Thread(new MyThread(sampleClass, semaphore), "Thread #3").start();


        //--------

        Semaphore semaphore02 = new Semaphore(2);
        for (int i = 0; i < 7; i++) {
            new CatV2("Cat #" + (i + 1), semaphore02).start();
        }
    }

}

class SampleClass{

    int a = 0;

}

class MyThread implements Runnable{

    SampleClass sampleClass;
    Semaphore semaphore;

    public MyThread(SampleClass sampleClass, Semaphore semaphore) {
        this.sampleClass = sampleClass;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName() + " ожидает разрешение.");
            semaphore.acquire(); // 1

            sampleClass.a  = 1;
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + sampleClass.a);
                sampleClass.a++;
                Thread.sleep(300);
            }

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " освобождает разрешение.");
        semaphore.release(); // 1

    }


}

class CatV2 extends Thread{

    public static final String RESET = "\u001B[0m";
    public static final String RED_BOLD = "\033[1;91m";
    public static final String GREEN_BOLD = "\033[1;92m";

    Semaphore semaphore;
    int num = 0;

    public CatV2(String name, Semaphore semaphore) {
        super(name);
        this.semaphore = semaphore;
    }

    @Override
    public void run() {

        try {
            while (num < 3) {

                semaphore.acquire();
                System.out.printf("%s %sподходит%s к миске, начинает кушать ...\n", getName(), GREEN_BOLD, RESET);
                sleep(500);
                num++;

                System.out.printf("%s %sотходит%s от миски; сытость: %d/3\n", getName(), RED_BOLD, RESET, num);
                semaphore.release();
                sleep(500);

            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}