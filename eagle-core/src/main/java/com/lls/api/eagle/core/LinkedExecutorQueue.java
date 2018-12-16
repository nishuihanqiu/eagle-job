package com.lls.api.eagle.core;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionException;

/************************************
 * LinkedExecutorQueue
 * LinkedTransferQueue 能保证更高性能，相比与LinkedBlockingQueue有明显提升
 * 不过LinkedTransferQueue的缺点是没有队列长度控制，需要在外层协助控制
 *
 * @author liliangshan
 * @date 2018/12/15
 ************************************/
class LinkedExecutorQueue extends LinkedTransferQueue<Runnable> {

    private static final long serialVersionUID = -2652345676751004839L;

    private StandardThreadExecutor standardThreadExecutor;

    public LinkedExecutorQueue() {
        super();
    }

    public void setStandardThreadExecutor(StandardThreadExecutor threadPoolExecutor) {
        this.standardThreadExecutor = threadPoolExecutor;
    }

    public boolean force(Runnable runnable) {
        if (standardThreadExecutor.isShutdown()) {
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        }
        // forces the item onto the queue, to be used if the task is rejected
        return super.offer(runnable);
    }

    public boolean offer(Runnable runnable) {
        int poolSize = standardThreadExecutor.getPoolSize();

        if (poolSize == standardThreadExecutor.getMaximumPoolSize()) {
            return super.offer(runnable);
        }

        if (standardThreadExecutor.getSubmittedTasksCount() <= poolSize) {
            return super.offer(runnable);
        }
        // if we have less threads than maximum force creation of a new
        // thread
        if (poolSize < standardThreadExecutor.getMaximumPoolSize()) {
            return false;
        }
        return super.offer(runnable);
    }
}
