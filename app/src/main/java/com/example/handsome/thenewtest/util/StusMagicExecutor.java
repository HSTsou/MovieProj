package com.example.handsome.thenewtest.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * the code from "http://stackoverflow.com/questions/1435903/specifying-threadpoolexecutor-problem"
 * Created by handsome on 2015/11/18.
 */
public class StusMagicExecutor extends ThreadPoolExecutor {
    private BlockingQueue<Runnable> secondaryQueue = new LinkedBlockingQueue<Runnable>();  //capacity is Integer.MAX_VALUE.

    public StusMagicExecutor() {
        super(10, 200, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(true), new RejectionHandler());
    }
    public void queueRejectedTask(Runnable task) {
        try {
            secondaryQueue.put(task);
        } catch (InterruptedException e) {
            // do something
        }
    }
    public Future submit(Runnable newTask) {
        //drain secondary queue as rejection handler populates it
        Collection<Runnable> tasks = new ArrayList<Runnable>();
        secondaryQueue.drainTo(tasks);

        tasks.add(newTask);

        for (Runnable task : tasks)
            super.submit(task);

        return null; //does not return a future!
    }
}

class RejectionHandler implements RejectedExecutionHandler {
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        ((StusMagicExecutor)executor).queueRejectedTask(runnable);
    }
}
