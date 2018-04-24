package org.bytedeco.javacv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Parallel {
    public static final String NUM_THREADS = "org.bytedeco.javacv.numthreads";
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public interface Looper {
        void loop(int i, int i2, int i3);
    }

    public static int getNumThreads() {
        try {
            String s = System.getProperty(NUM_THREADS);
            if (s != null) {
                return Integer.valueOf(s).intValue();
            }
            return getNumCores();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setNumThreads(int numThreads) {
        System.setProperty(NUM_THREADS, Integer.toString(numThreads));
    }

    public static int getNumCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void run(Runnable... runnables) {
        int i = 0;
        if (runnables.length == 1) {
            runnables[0].run();
            return;
        }
        int length;
        Future[] futures = new Future[runnables.length];
        for (int i2 = 0; i2 < runnables.length; i2++) {
            futures[i2] = threadPool.submit(runnables[i2]);
        }
        Throwable error = null;
        try {
            for (Future f : futures) {
                if (!f.isDone()) {
                    f.get();
                }
            }
        } catch (Throwable t) {
            error = t;
        }
        if (error != null) {
            length = futures.length;
            while (i < length) {
                futures[i].cancel(true);
                i++;
            }
            throw new RuntimeException(error);
        }
    }

    public static void loop(int from, int to, Looper looper) {
        loop(from, to, getNumThreads(), looper);
    }

    public static void loop(int from, int to, int numThreads, final Looper looper) {
        int i = to - from;
        if (numThreads <= 0) {
            numThreads = getNumCores();
        }
        int numLoopers = Math.min(i, numThreads);
        Runnable[] runnables = new Runnable[numLoopers];
        for (int i2 = 0; i2 < numLoopers; i2++) {
            final int subFrom = (((to - from) * i2) / numLoopers) + from;
            final int subTo = (((to - from) * (i2 + 1)) / numLoopers) + from;
            final int looperID = i2;
            runnables[i2] = new Runnable() {
                public void run() {
                    looper.loop(subFrom, subTo, looperID);
                }
            };
        }
        run(runnables);
    }
}
