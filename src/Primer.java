import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Primer {
    public static void run() {
        ExecutorService pool = Executors.newCachedThreadPool();

        int taskCount = 8;

        submitTaskInto(pool, taskCount);
        System.out.println(" ");

        pool.shutdown();

        measure(pool);
    }

    private static void submitTaskInto(ExecutorService pool, int taskCount) {
        System.out.println("Create tasks");
        IntStream.rangeClosed(1, taskCount)
                .mapToObj(value -> makeTask(value))
                .forEach(pool::submit);
    }

    private static Runnable makeTask(int taskId) {
        int temp = new Random().nextInt(20000) + 10000;
        int taskTime = (int) TimeUnit.MILLISECONDS.toSeconds(temp);

        return () -> heavyTask(taskId, taskTime);
    }

    private static void heavyTask(int taskId, int taskTime) {
        System.out.printf("Задача %s займет %s секунд%n", taskId, taskTime);

        try {
            Thread.sleep(taskTime * 1000);
            System.out.printf("Завершилась задача %s завершилась за %s секунд%n", taskId, taskTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void measure(ExecutorService pool) {
        long start = System.nanoTime();
        try {
            pool.awaitTermination(600, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long delta = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("Выполение заняло: %s мсек%n", delta);
    }
}
