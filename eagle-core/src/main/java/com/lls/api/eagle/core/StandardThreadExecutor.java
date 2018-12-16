package com.lls.api.eagle.core;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/************************************
 * StandardThreadExecutor
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class StandardThreadExecutor extends ThreadPoolExecutor {

    public static final int DEFAULT_MIN_THREADS = 20;
    public static final int DEFAULT_MAX_THREADS = 200;
    public static final int DEFAULT_MAX_IDLE_TIME = 60 * 1000;

    protected AtomicInteger submittedTasksCount; // 正在处理的任务数
    private int maxSubmittedTaskCount;           // 最大允许同时处理的任务数

    public StandardThreadExecutor() {
        this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
    }

    public StandardThreadExecutor(int corePoolSize, int maximumPoolSize) {
        this(corePoolSize, maximumPoolSize, maximumPoolSize);
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity) {
        this(coreThreads, maxThreads, queueCapacity, Executors.defaultThreadFactory());
    }

    public StandardThreadExecutor(int coreThread, int maxThreads, long keepAliveTime, TimeUnit unit) {
        this(coreThread, maxThreads, keepAliveTime, unit, maxThreads);
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, int queueCapacity, ThreadFactory threadFactory) {
        this(coreThreads, maxThreads, DEFAULT_MAX_IDLE_TIME, TimeUnit.MILLISECONDS, queueCapacity, threadFactory);
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit, int queueCapacity) {
        this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, Executors.defaultThreadFactory());
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
                                  int queueCapacity, ThreadFactory threadFactory) {
        this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, threadFactory, new AbortPolicy());
    }

    public StandardThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
                                  int queueCapacity, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(coreThreads, maxThreads, keepAliveTime, unit, new LinkedExecutorQueue(), threadFactory, handler);
        ((LinkedExecutorQueue) getQueue()).setStandardThreadExecutor(this);

        submittedTasksCount = new AtomicInteger(0);

        // 最大并发任务限制： 队列buffer数 + 最大线程数
        maxSubmittedTaskCount = queueCapacity + maxThreads;
    }

    @Override
    public void execute(Runnable command) {
        int count = submittedTasksCount.incrementAndGet();

        // 超过最大的并发任务限制，进行 reject
        // 依赖的LinkedTransferQueue没有长度限制，因此这里进行控制
        if (count > maxSubmittedTaskCount) {
            submittedTasksCount.decrementAndGet();
            getRejectedExecutionHandler().rejectedExecution(command, this);
        }

        try {
            super.execute(command);
        } catch (RejectedExecutionException e) {
            // there could have been contention around the queue
            if (!((LinkedExecutorQueue) getQueue()).force(command)) {
                submittedTasksCount.decrementAndGet();
                getRejectedExecutionHandler().rejectedExecution(command, this);
            }
        }
    }

    public int getSubmittedTasksCount() {
        return submittedTasksCount.get();
    }

    public int getMaxSubmittedTaskCount() {
        return maxSubmittedTaskCount;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        submittedTasksCount.decrementAndGet();
    }
}
