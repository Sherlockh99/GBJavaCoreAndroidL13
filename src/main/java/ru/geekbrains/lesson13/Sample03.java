package ru.geekbrains.lesson13;

import java.util.concurrent.*;

public class Sample03 {

    public static void main(String[] args) {

        System.out.println("Начало работы приложения");

        /*
        ExecutorService - класс пула потоков.
        Позволяет на лету создавать различные потоки.
        newFixedThreadPool - возвращает готовый класс типа ExecutorService,
        а именно создает готовый пакет (пул) потоков, который ограничен количеством,
        где nThreads - это максимальное количество потоков, которое мы в одну единицу времени можем создать
         */

        ExecutorService service = Executors.newFixedThreadPool(3);

        /*
        чтобы выполнить задачу в рамках определенного потока, можно воспользоваться методом execute,
        в качестве параметра передается объект типа Runnable
         */
        service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Выполнение потока #1");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Завершение потока #1");
            }
        });

        /*
        метод submit, в отличии от execute, возвращает переменную типа Future,
        использовать её можно для анализа в дальнейшем,
        например для ожидания завершения
         */

        Future future01 = service.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Выполнение потока #2");
                try{
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        /*
        submit(new Callable<T>() -
        интерфейс Callable, в отличии от метода run() возвращает значение,
        которое мы укажем в переопределяемом методе call()
         */

        Future<String> future02 = service.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                System.out.println("Выполнение потока #3");
                try{
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "Hello, GeekBrains!";
            }
        });

        try{
            future01.get();
            System.out.println("Завершение выполнения потока #2");
            System.out.printf("Завершение выполнения потока #3\nРезультат: %s\n",future02.get());

        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }


        /*
        ОБЯЗАТЕЛЬНЫЙ вызов метода shutdown() после работы с пуллом пакетов
         */
        service.shutdown();

        System.out.println("Завершение работы приложения");
    }

}
