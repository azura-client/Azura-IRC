package best.azura.irc.utils.executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler implements Executor {
    private ScheduledThreadPoolExecutor executor;

    public Scheduler(String schedulerName, Integer poolSize) {
        executor = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, String.format("%s_%d", schedulerName, counter.incrementAndGet()));
                t.setDaemon(true);

                return t;
            }
        });
    }

    public Future<?> execute(Runnable task) {
            return executor.submit(task);
    }

    // Schedule Task to be executed in X miliseconds
    public ScheduledFuture<?> schedule(Runnable task, Long delay) {
        return executor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

}
