import java.util.concurrent.*;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static ExecutorService carThread = Executors.newCachedThreadPool();
    private static CyclicBarrier cb;
    private static Car[] cars = new Car[CARS_COUNT];
    private static  Race race = new Race(new Road(60), new Tunnel(CARS_COUNT), new Road(40));
    public static CountDownLatch cdl = new CountDownLatch(CARS_COUNT);

    public static void main(String[] args) {
        Runnable startRaceMessage = () -> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        cb = new CyclicBarrier(CARS_COUNT,startRaceMessage);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        carsCreate();
        waitingFinished();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
        carThread.shutdown();
    }

    private static void waitingFinished() {
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void carsCreate() {
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cb, cdl);
        }
        for (int i = 0; i < cars.length; i++) {
            carThread.execute(cars[i]);
        }
    }
}