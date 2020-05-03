package com.mepeng.cn.pen.common.mq.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class BaseTransactionExecutorImpl extends TransactionSynchronizationAdapter implements TransactionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTransactionExecutorImpl.class);
    protected final ThreadLocal<List<Runnable>> RUNNABLES = new ThreadLocal();
    protected ThreadFactory namedThreadFactory = (new ThreadFactoryBuilder()).setNameFormat("mom-tran-pool-%d").build();
    protected ExecutorService executor;

    public BaseTransactionExecutorImpl() {
        this.executor = new ThreadPoolExecutor(20, 50, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(2048), this.namedThreadFactory);
    }

    @Override
    public void execute(Runnable command) {
        LOGGER.debug("Submitting new runnable {} to run around commit", command);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            LOGGER.debug("事务同步未激活. 直接开始执行线程 {}", command);
            this.executor.execute(command);
        } else {
            List<Runnable> threadRunnables = (List)this.RUNNABLES.get();
            if (threadRunnables == null) {
                threadRunnables = new ArrayList();
                this.RUNNABLES.set(threadRunnables);
                TransactionSynchronizationManager.registerSynchronization(this);
            }

            ((List)threadRunnables).add(command);
        }
    }
}
