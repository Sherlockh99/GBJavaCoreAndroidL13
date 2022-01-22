package ru.geekbrains.lesson13;

import java.util.Random;

public class Sample01 {

    public static void main(String[] args) {

        Plate plate01 = new Plate();

        Human human01 = new Human(plate01);
        Cat cat01 = new Cat(plate01);
        Cat cat02 = new Cat(plate01);


        new Thread(human01).start();
        new Thread(cat01, "Cat #1").start();
        new Thread(cat02, "Cat #2").start();

    }

}

class Plate{

    private int food = 0;
    //private Random random = new Random();

    public synchronized void get(String name){
        while (food < 1){
            try {
                wait();
                Thread.sleep(500);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        food--;
        System.out.printf("Кот %s покушал еду из миски\n", name);
        System.out.println("Еды в миске: " + food);
        notify();

    }

    public synchronized void put(){
        while (food >= 3){
            try {
                wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        food++;
        System.out.println("Человек добавил еду в миску");
        System.out.println("Еды в миске: " + food);
        //notify();
        notifyAll();
    }

}

class Human implements Runnable{

    Plate plate;

    public Human(Plate plate) {
        this.plate = plate;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Человек ложит тарелку в еду: "+(i+1));
            plate.put();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("У человека закончилась еда ...");
    }
}

class Cat implements Runnable{

    Plate plate;

    public Cat(Plate plate) {
        this.plate = plate;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            plate.get(Thread.currentThread().getName());
        }
        System.out.printf("Кот %s наелся ...\n", Thread.currentThread().getName());
    }
}













