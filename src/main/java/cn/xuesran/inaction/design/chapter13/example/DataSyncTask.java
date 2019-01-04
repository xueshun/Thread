package cn.xuesran.inaction.design.chapter13.example;

import cn.xuesran.inaction.design.chapter06.FTPUploader;
import cn.xuesran.inaction.design.chapter06.FTPUploaderPromisor;
import cn.xuesran.inaction.design.chapter09.ReEnqueueRejectedExecutionHandler;
import cn.xuesran.inaction.design.chapter13.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 数据同步定时任务
 *
 * @author xueshun
 */
public class DataSyncTask implements Runnable {
    private final Properties config;

    public DataSyncTask(Properties config) {
        this.config = config;
    }

    @Override
    public void run() {
        /*
         * 创建并初始化Pipeline实例。
         * SimplePipeline类的源码参见清单13-9。接口RecordSaveTask的源码见本书的配套下载。
         */
        SimplePipeline<RecordSaveTask, String> pipeline = buildPipeline();
        pipeline.init(pipeline.newDefaultPipelineContext());

        // 接口RecordSource的源码见本书的配套下载
        try {
            // 创建数据源
            RecordSource recordSource = makeRecordSource(this.config);

            // 使用Pipeline来处理数据记录
            processRecords(recordSource, pipeline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pipeline.shutdown(360, TimeUnit.SECONDS);
    }

    protected RecordSource makeRecordSource(Properties config)
            throws Exception {
        // DbRecordSource类的源码见本书的配套下载
        return new DbRecordSource(config);
    }

    private SimplePipeline<RecordSaveTask, String> buildPipeline() {
        /*
         * 线程池的本质是重复利用一定数量的线程，而不是针对每个任务都有一个专门的工作者线程。
         * 这里，各个Pipe的初始化话完全可以在上游Pipe初始化完毕后再初始化其后继Pipe，而不必多个Pipe同时初始化。
         * 因此，这个初始化的动作可以由一个线程来处理。该线程处理完各个Pipe的初始化后，可以继续处理之后可能产生的任务， 如错误处理。
         * 所以，上述这些先后产生的任务可以由线程池中的一个工作者线程从头到尾负责执行。
         *
         * 由于这里的几个Pipe都是处理I/O的，为了避免使用锁（以减少不必要的上下文切换） 但又能保证线程安全，故每个Pipe都采用单线程处理。
         * 若各个Pipe要改用线程池来处理，需要注意：1）线程安全 2）死锁
         */
        final ExecutorService helperExecutor = Executors.newSingleThreadExecutor();
        final SimplePipeline<RecordSaveTask, String> pipeline = new SimplePipeline<>(helperExecutor);

        /*
         * 根据数据库记录生成相应的数据文件。 Pipe接口的源码参见清单13-3。
         */
        Pipe<RecordSaveTask, File> stageSaveFile = createFileSaveStage();
        pipeline.addAsWorkerThreadBasedPipe(stageSaveFile, 1);

        // 将生成的数据文件传输到指定的主机上。
        Pipe<File, File> stageTransferFile = createFileTransferStage();
        pipeline.addAsWorkerThreadBasedPipe(stageTransferFile, 1);

        // 备份已经传输的数据文件
        Pipe<File, Void> stageBackupFile = createFileBackupStage();
        pipeline.addAsWorkerThreadBasedPipe(stageBackupFile, 1);

        return pipeline;
    }

    /**
     * 输入：  一组数据库记录（类型RecordSaveTask）
     * 处理： 将输入的一组数据库记录存入数据文件
     * 线程模型：    单工作者线程。若使用多线程模型，必然导致多线程征用一个数据文件（文件未写满），而者需要引入锁。
     * 使用单线程模型则可以避免锁的使用
     *
     * @return 若当前数据文件已写满，则输出该写满的文件（类型为File），否输入null
     */
    private Pipe<RecordSaveTask, File> createFileSaveStage() {
        Pipe<RecordSaveTask, File> ret;
        // AbstractPipe类的源码参见清单13-4
        ret = new AbstractPipe<RecordSaveTask, File>() {

            @Override
            protected File doProcess(RecordSaveTask task) throws PipeException {
                /*
                 * 将记录写入文件。 RecordSaveTask类的源码参见本书配套下载。
                 */
                File file;
                final RecordWriter recordWriter = RecordWriter.getInstance();
                final Record[] records = task.records;
                if (null == records) {
                    file = recordWriter.finishRecords(task.recordDay, task.targetFileIndex);
                } else {
                    try {
                        file = recordWriter.write(records, task.targetFileIndex);
                    } catch (IOException e) {
                        throw new PipeException(this, task, "Failed to save records.", e);
                    }
                }
                return file;
            }
        };
        return ret;
    }

    /**
     * 输入：  已写满的数据文件（File）
     * 处理：  将输入的数据文件以并发方式FTP传输值指定的多台FTP主机
     * 输出：  若文件传输成功，则输出以传输过的数据文件（File）；否则返回null
     * 线程模型：    单工作线程。该Pipe处理过程会用到一款非线程安全的开源FTP客户端组件，
     * 使用单线程在不引入锁的情况下确保使用该组件时的线程安全
     *
     * @return
     */
    protected Pipe<File, File> createFileTransferStage() {
        Pipe<File, File> ret;
        final String[][] ftpServerConfigs = retrieveFTPServConf();

        final ThreadPoolExecutor ftpExecutorService = new ThreadPoolExecutor(
                1,
                ftpServerConfigs.length,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ReEnqueueRejectedExecutionHandler());

        final String ftpServerDir = this.config.getProperty("ftp.serverdir");

        // AbstractParallelPipe类的源码参见清单13-7
        ret = new AbstractParallelPipe<File, File, File>(new SynchronousQueue<File>(), ftpExecutorService) {
            @SuppressWarnings("unchecked")
            final Future<FTPUploader>[] ftpClientUtilHolders = new Future[ftpServerConfigs.length];

            @Override
            public void init(PipeContext pipeCtx) {
                super.init(pipeCtx);
                String[] ftpServerConfig;
                for (int i = 0; i < ftpServerConfigs.length; i++) {
                    ftpServerConfig = ftpServerConfigs[i];
                    // FTPUploaderPromisor类的源码参见清单6-2
                    ftpClientUtilHolders[i] =
                            FTPUploaderPromisor.newFTPUploaderPromise(
                                    ftpServerConfig[0],
                                    ftpServerConfig[1],
                                    ftpServerConfig[2], ftpServerDir,
                                    ftpExecutorService);
                }
            }

            @Override
            protected List<Callable<File>> buildTasks(final File file) {
                // 创建一组并发任务，这些任务将指定的文件上传到FTP服务器上
                List<Callable<File>> tasks = new LinkedList<>();
                for (Future<FTPUploader> ftpClientUtilHolder : ftpClientUtilHolders) {
                    tasks.add(new FileTransferTask(ftpClientUtilHolder, file));
                }
                return tasks;
            }

            @Override
            protected File combineResults(List<Future<File>> subTaskResults) throws Exception {
                if (0 == subTaskResults.size()) {
                    return null;
                }
                File file = subTaskResults.get(0).get();
                return file;
            }

            @Override
            public void shutdown(long timeout, TimeUnit unit) {
                super.shutdown(timeout, unit);
                ftpExecutorService.shutdown();
                try {
                    ftpExecutorService.awaitTermination(timeout, unit);
                } catch (InterruptedException e1) {
                    ;
                }
                for (Future<FTPUploader> ftpClientUtilHolder
                        : ftpClientUtilHolders) {
                    try {
                        ftpClientUtilHolder.get().disconnect();
                    } catch (Exception e) {
                        ;
                    }
                } // end of for
            }// end of shutdown
        };
        return ret;
    }

    /**
     * 输入：  已传输成功的文件（File）
     * 处理：  将输入的文件移动到备份目录
     * 输出：  null
     * 线程模型：    单工作线程
     *
     * @return
     */
    private Pipe<File, Void> createFileBackupStage() {
        Pipe<File, Void> ret;
        ret = new AbstractPipe<File, Void>() {
            @Override
            protected Void doProcess(File transferedFile) throws PipeException {
                // 备份已传输完毕的文件
                RecordWriter.backupFile(transferedFile);
                return null;
            }

            @Override
            public void shutdown(long timeout, TimeUnit unit) {
                // 所有文件备份完毕后，清理掉空文件夹
                RecordWriter.purgeDir();
            }

        };
        return ret;
    }

    protected String[][] retrieveFTPServConf() {
        String serverList = this.config.getProperty("ftp.servers");
        String[] arr = serverList.split(";");
        String[][] ftpServerConfigs = new String[arr.length][];
        for (int i = 0; i < arr.length; i++) {
            String server = arr[i];
            String[] parts = server.split(",");
            ftpServerConfigs[i] = parts;
        }
        return ftpServerConfigs;
    }

    private void processRecords(RecordSource recordSource, Pipeline<RecordSaveTask, String> pipeline) throws Exception {
        Record record;
        Record[] records = new Record[Config.RECORD_SAVE_CHUNK_SIZE];
        int targetFileIndex = 0;
        int nextTargetFileIndex = 0;
        int recordCountInTheDay = 0;
        int recordCountInTheFile = 0;
        String recordDay = null;
        String lastRecordDay = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

        while (recordSource.hasNext()) {
            record = recordSource.next();
            lastRecordDay = recordDay;
            recordDay = sdf.format(record.getOperationTime());
            if (recordDay.equals(lastRecordDay)) {
                records[recordCountInTheFile] = record;
                recordCountInTheDay++;
            } else {
                // 实际已发生的不同日期记录文件切换
                if (null != lastRecordDay) {
                    if (recordCountInTheFile >= 1) {
                        pipeline.process(new RecordSaveTask(
                                Arrays.copyOf(records, recordCountInTheFile),
                                targetFileIndex));
                    } else {
                        pipeline.process(new RecordSaveTask(lastRecordDay,
                                targetFileIndex));
                    }

                    // 在此之前，先将records中的内容写入文件
                    records[0] = record;
                    recordCountInTheFile = 0;
                } else {
                    // 直接赋值
                    records[0] = record;
                }
                recordCountInTheDay = 1;
            }

            if (nextTargetFileIndex == targetFileIndex) {
                recordCountInTheFile++;
                if (0 == (recordCountInTheFile
                        % Config.RECORD_SAVE_CHUNK_SIZE)) {
                    pipeline.process(new RecordSaveTask(
                            Arrays.copyOf(records, recordCountInTheFile),
                            targetFileIndex));
                    recordCountInTheFile = 0;
                }
            }

            nextTargetFileIndex = (recordCountInTheDay)
                    / Config.MAX_RECORDS_PER_FILE;
            if (nextTargetFileIndex > targetFileIndex) {
                // 预测到将发生同日期记录文件切换
                if (recordCountInTheFile > 1) {
                    pipeline.process(new RecordSaveTask(
                            Arrays.copyOf(records, recordCountInTheFile),
                            targetFileIndex));
                } else {
                    pipeline.process(
                            new RecordSaveTask(recordDay, targetFileIndex));
                }
                recordCountInTheFile = 0;
                targetFileIndex = nextTargetFileIndex;
            } else if (nextTargetFileIndex < targetFileIndex) {
                // 实际已发生的异日期记录文件切换,recordCountInTheFile保持当前值
                targetFileIndex = nextTargetFileIndex;
            }
        } // end of while

        if (recordCountInTheFile > 0) {
            pipeline.process(new RecordSaveTask(
                    Arrays.copyOf(records, recordCountInTheFile),
                    targetFileIndex));
        }
    }
}