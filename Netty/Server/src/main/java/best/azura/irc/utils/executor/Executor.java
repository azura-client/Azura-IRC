package best.azura.irc.utils.executor;

import java.util.concurrent.Future;

public interface Executor {
    Future<?> execute(Runnable task);
}
