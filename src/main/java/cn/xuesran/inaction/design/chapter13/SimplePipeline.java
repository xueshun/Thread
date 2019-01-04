package cn.xuesran.inaction.design.chapter13;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @param <T>
 * @param <OUT>
 */
public class SimplePipeline<T, OUT> extends AbstractPipe<T, OUT> implements Pipeline<T, OUT> {
    private final static Logger logger = Logger.getLogger(SimplePipeline.class);
    private final Queue<Pipe<?, ?>> pipes = new LinkedList<Pipe<?, ?>>();

    private final ExecutorService helperExecutor;

    public SimplePipeline() {
        this(Executors.newSingleThreadExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "SimplePipeline-Helper");
                t.setDaemon(true);
                return t;
            }

        }));

    }

    public SimplePipeline(final ExecutorService helperExecutor) {
        super();
        this.helperExecutor = helperExecutor;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        Pipe<?, ?> pipe;

        while (null != (pipe = pipes.poll())) {
            pipe.shutdown(timeout, unit);
        }

        helperExecutor.shutdown();

    }

    @Override
    protected OUT doProcess(T input) throws PipeException {
        // 什么也不做
        return null;
    }

    @Override
    public void addPipe(Pipe<?, ?> pipe) {

        // Pipe间的关联关系在init方法中建立
        pipes.add(pipe);
    }

    public <INPUT, OUTPUT> void addAsWorkerThreadBasedPipe(
            Pipe<INPUT, OUTPUT> delegate, int workerCount) {
        addPipe(new WorkerThreadPipeDecorator<INPUT, OUTPUT>(delegate,
                workerCount));
    }

    public <INPUT, OUTPUT> void addAsThreadPoolBasedPipe(
            Pipe<INPUT, OUTPUT> delegate, ExecutorService executorSerivce) {
        addPipe(new ThreadPoolPipeDecorator<INPUT, OUTPUT>(delegate,
                executorSerivce));
    }

    @Override
    public void process(T input) throws InterruptedException {
        @SuppressWarnings("unchecked")
        Pipe<T, ?> firstPipe = (Pipe<T, ?>) pipes.peek();

        firstPipe.process(input);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void init(final PipeContext ctx) {
        LinkedList<Pipe<?, ?>> pipesList = (LinkedList<Pipe<?, ?>>) pipes;
        Pipe<?, ?> prevPipe = this;
        for (Pipe<?, ?> pipe : pipesList) {
            prevPipe.setNextPipe(pipe);
            prevPipe = pipe;
        }
        helperExecutor.submit(new PipeInitTask(ctx, (List) pipes));
    }

    static class PipeInitTask implements Runnable {
        final List<Pipe<?, ?>> pipes;
        final PipeContext ctx;

        public PipeInitTask(PipeContext ctx, List<Pipe<?, ?>> pipes) {
            this.pipes = pipes;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            try {
                for (Pipe<?, ?> pipe : pipes) {
                    pipe.init(ctx);
                }
            } catch (Exception e) {
                logger.error("Failed to init pipe", e);
            }
        }

    }

    public PipeContext newDefaultPipelineContext() {
        return new PipeContext() {
            @Override
            public void handleError(final PipeException exp) {
                helperExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        logger.error("", exp);
                    }
                });
            }

        };
    }
}