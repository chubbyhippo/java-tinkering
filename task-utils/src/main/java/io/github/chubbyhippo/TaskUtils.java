package io.github.chubbyhippo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskUtils {
    private TaskUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static void runCompletableFutureVoid(Runnable runnable, long timeout, TimeUnit timeUnit) throws TimeoutException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(runnable);
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
}
