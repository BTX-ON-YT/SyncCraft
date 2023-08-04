package systems.btx.Classes;

import java.util.Queue;
import java.util.function.Function;

public class Ticker implements Runnable {
    private int tickRate = 20;
    private int waitTime = 1000 / tickRate;
    private int tickCount = 0;
    // queue of items to be procesed with the next tick
    private Queue<Function> queue = new java.util.LinkedList<>();
    private Tick currentTick;

    public Ticker(int tickRate) {
        this.tickRate = tickRate;
    }

    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            currentTick = new Tick(queue);
            currentTick.run();
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            if (timeTaken < waitTime) {
                try {
                    Thread.sleep(waitTime - timeTaken);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add(Function function) {
        queue.add(function);
    }
}
