package cn.xuesran.inaction.design.chapter13;

public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {

    /**
     * 往该Pipeline实例中添加一个Pipe实例。
     *
     * @param pipe Pipe实例
     */
    void addPipe(Pipe<?, ?> pipe);
}