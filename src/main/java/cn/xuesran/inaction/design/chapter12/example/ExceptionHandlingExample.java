package cn.xuesran.inaction.design.chapter12.example;

import cn.xuesran.inaction.design.chapter12.RetryInfo;
import cn.xuesran.inaction.design.chapter12.SubTaskFailureException;
import cn.xuesran.inaction.design.util.Debug;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ExceptionHandlingExample {

    public void handle(ExecutionException e, Set<BigInteger> result) {
        Throwable cause = e.getCause();
        if (SubTaskFailureException.class.isInstance(cause)) {

            @SuppressWarnings("rawtypes")
            RetryInfo retryInfo = ((SubTaskFailureException) cause).retryInfo;

            Object subTask = retryInfo.subTask;
            Debug.info("retrying subtask:" + subTask);

            @SuppressWarnings("unchecked")
            Callable<Set<BigInteger>> redoCmd = retryInfo.redoCommand;
            try {
                result.addAll(redoCmd.call());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}