package cn.xuesran.inaction.design.chapter13;

public interface PipeContext {
    /**
     * 用于对处理阶段抛出的异常进行处理.
     *
     * @param exp
     */
    void handleError(PipeException exp);
}