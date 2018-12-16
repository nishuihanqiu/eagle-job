package com.lls.api.eagle.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/************************************
 * EagleThreadFactory
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
public class EagleThreadFactory implements ThreadFactory {


    private static final String DEFAULT_THREAD_NAME = "eagle";
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup threadGroup;
    private final AtomicInteger currentThreadNumber = new AtomicInteger(1);
    private final String threadPrefixName;
    private int priority = Thread.NORM_PRIORITY;
    private boolean isDaemon = false;

    public EagleThreadFactory() {
        this(DEFAULT_THREAD_NAME);
    }

    public EagleThreadFactory(String prefix) {
        this(prefix, false);
    }

    public EagleThreadFactory(String prefix, boolean isDaemon) {
        this(prefix, isDaemon, Thread.NORM_PRIORITY);
    }

    public EagleThreadFactory(String prefix, boolean isDaemon, int priority) {
        SecurityManager securityManager = System.getSecurityManager();
        this.threadGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.threadPrefixName = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
        this.isDaemon = isDaemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup, r, threadPrefixName + currentThreadNumber.getAndIncrement(), 0);
        thread.setDaemon(this.isDaemon);
        thread.setPriority(this.priority);
        return thread;
    }
}
