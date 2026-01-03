package org.example.htuoj.common.utils;

import java.util.concurrent.*;

public class MyThreadPool {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private TimeUnit unit;
    private BlockingQueue<Runnable> workQueue;
    private ThreadFactory threadFactory;
    private RejectedExecutionHandler handler;

    public MyThreadPool(Integer corePoolSize, Integer maximumPoolSize, Long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }


    public void execute(Runnable command) {

    }
}
