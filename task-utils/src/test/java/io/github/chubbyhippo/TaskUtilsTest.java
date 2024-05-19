package io.github.chubbyhippo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.*;

class TaskUtilsTest {

    @Test
    @DisplayName("should throw illegal state exception when initialized")
    void shouldThrowIllegalStateExceptionWhenInitialized() {
        var constructor = TaskUtils.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (IllegalStateException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            assertThat(exception.getCause().getClass()).isEqualTo(IllegalStateException.class);
            assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
        }
    }

    @Test
    @DisplayName("should run completable future task")
    void shouldRunCompletableFutureVoidCompletableFutureTask() {
        Runnable task = () -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task completed");
                System.out.println("Current thread :" + Thread.currentThread().getName());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        };
        Assertions.assertAll(
                () -> {
                    // Testing a 2 seconds timeout
                    assertThatNoException().isThrownBy(() -> TaskUtils.runCompletableFutureVoid(task, 2, TimeUnit.SECONDS));
                },
                () -> {
                    // Testing a half second timeout, expecting TimeoutException
                    assertThatThrownBy(() -> TaskUtils.runCompletableFutureVoid(task, 500, TimeUnit.MILLISECONDS))
                            .isExactlyInstanceOf(TimeoutException.class);
                }
        );
    }

    @Test
    @DisplayName("should run task executor service")
    void shouldRunTaskExecutorService() {

        Runnable task = () -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task completed");
                System.out.println("Current thread :" + Thread.currentThread().getName());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        };
        Assertions.assertAll(
                () -> {
                    // Testing a 2 seconds timeout
                    assertThatNoException().isThrownBy(() -> TaskUtils.runTaskExecutor(task, 2, TimeUnit.SECONDS));
                },
                () -> {
                    // Testing a half second timeout, expecting TimeoutException
                    assertThatThrownBy(() -> TaskUtils.runTaskExecutor(task, 500, TimeUnit.MILLISECONDS))
                            .isExactlyInstanceOf(TimeoutException.class);
                }
        );
    }

    @Test
    @DisplayName("should run generic task executor service")
    void shouldRunGenericTaskExecutorService() throws TimeoutException {
        Callable<String> task = () -> {
            Thread.sleep(1000);
            return "Task completed";
        };

        // Testing a 2 seconds timeout
        assertThat(TaskUtils.runTaskExecutor(task, 2, TimeUnit.SECONDS)).isEqualTo(Optional
                .of("Task completed"));


        // Testing a half second timeout, expecting TimeoutException
        assertThatThrownBy(() -> TaskUtils.runTaskExecutor(task, 500, TimeUnit.MILLISECONDS))
                .isInstanceOf(TimeoutException.class)
                .hasMessageContaining("Timed out");
    }

}