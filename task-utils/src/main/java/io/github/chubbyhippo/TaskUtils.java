package io.github.chubbyhippo;

import java.util.Optional;
import java.util.concurrent.*;

public class TaskUtils {
    private TaskUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void runCompletableFutureVoid(Runnable runnable, long timeout, TimeUnit timeUnit) throws TimeoutException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(runnable);
        executeRunnable(timeout, timeUnit, future);
    }

    private static void executeRunnable(long timeout, TimeUnit timeUnit, Future<?> future) throws TimeoutException {
        try {
            future.get(timeout, timeUnit);
            System.out.println("Current thread :" + Thread.currentThread().getName());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("The task exceeded the given timeout of: " + timeout + " " + timeUnit);
            System.out.println("Current thread :" + Thread.currentThread().getName());
            future.cancel(true);
            System.out.println("Current thread :" + Thread.currentThread().getName());
            e.printStackTrace();
            throw new TimeoutException("Timed out");
        }
    }

    public static void runTaskExecutor(Runnable runnable, long timeout, TimeUnit timeUnit) throws TimeoutException {

        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            Future<?> future = executorService.submit(runnable);
            executeRunnable(timeout, timeUnit, future);
        }
    }

    public static <T> Optional<T> runTaskExecutor(Callable<T> task, long timeout, TimeUnit timeUnit) throws TimeoutException {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<T> future = executorService.submit(task);
            try {
                return Optional.ofNullable(future.get(timeout, timeUnit));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("The task exceeded the given timeout of: " + timeout + " " + timeUnit);
                throw new TimeoutException("Timed out");
            }
        }
        return Optional.empty();
    }


}
