package com.mepeng.cn.pen.common.mq.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PreCommitExecutorDefaultImpl  extends BaseTransactionExecutorImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreCommitExecutorDefaultImpl.class);

    public PreCommitExecutorDefaultImpl() {
    }

    public void beforeCommit(boolean readOnly) {
        List<Runnable> threadRunnables = (List)this.RUNNABLES.get();
        LOGGER.debug("Transaction beforeCommit, executing {} runnables", threadRunnables.size());

        for(int i = 0; i < threadRunnables.size(); ++i) {
            Runnable runnable = (Runnable)threadRunnables.get(i);
            LOGGER.debug("Executing runnable {}", runnable);
            runnable.run();
        }

    }

    public void beforeCompletion() {
        LOGGER.debug("Transaction beforeCompletion");
        this.RUNNABLES.remove();
    }
}
