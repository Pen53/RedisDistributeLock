package com.mepeng.cn.pen.common.mq.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AfterCommitExecutorDefaultImpl extends BaseTransactionExecutorImpl  {
    private static final Logger LOGGER = LoggerFactory.getLogger(AfterCommitExecutorDefaultImpl.class);

    public AfterCommitExecutorDefaultImpl() {
    }

    public void afterCommit() {
        List<Runnable> threadRunnables = (List)this.RUNNABLES.get();
        LOGGER.debug("Transaction successfully committed, executing {} runnables", threadRunnables.size());

        for(int i = 0; i < threadRunnables.size(); ++i) {
            Runnable runnable = (Runnable)threadRunnables.get(i);
            LOGGER.debug("Executing runnable {}", runnable);

            try {
                this.executor.execute(runnable);
            } catch (RuntimeException var5) {
                LOGGER.error("Failed to execute runnable " + runnable, var5);
            }
        }

    }

    public void afterCompletion(int status) {
        LOGGER.debug("Transaction completed with status {}", status == 0 ? "COMMITTED" : "ROLLED_BACK");
        this.RUNNABLES.remove();
    }
}
