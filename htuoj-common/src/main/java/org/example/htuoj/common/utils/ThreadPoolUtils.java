package org.example.htuoj.common.utils;

import java.util.concurrent.*;

public class ThreadPoolUtils {

    private static ExecutorService executorService;

    private static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private ThreadPoolUtils() {
        //手动创建线程池.
        executorService = new ThreadPoolExecutor(
                cpuNum, // 核心线程数
                cpuNum, // 最大线程数。最多几个线程并发。
                3,//当非核心线程无任务时，几秒后结束该线程
                TimeUnit.SECONDS,// 结束线程时间单位
                new LinkedBlockingDeque<>(2000 * cpuNum), //阻塞队列，限制等候线程数
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()); // 队列满了，将该任务返回给调用者线程来执行。
    }

    private static class PluginConfigHolder {
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getInstance() {
        return PluginConfigHolder.INSTANCE;
    }

    public ExecutorService getThreadPool() {
        return executorService;
    }

}