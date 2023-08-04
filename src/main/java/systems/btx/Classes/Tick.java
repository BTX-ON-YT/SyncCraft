package systems.btx.Classes;

import java.util.Queue;
import java.util.function.Function;

public class Tick implements Runnable {
    private Queue<Function> queue;

    public Tick(Queue<Function> queue) {
        this.queue = queue;
    }

    public void run() {
        while (!queue.isEmpty()) {
            Function function = queue.remove();
            function.apply(null);
        }
    }
}
