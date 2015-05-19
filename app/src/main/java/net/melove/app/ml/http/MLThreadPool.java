package net.melove.app.ml.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/3/23.
 */
public class MLThreadPool {

    private static MLThreadPool mlThreadPool = null;

    private static int THREAD_POLL_CORE = 5;
    private static int THREAD_POOL_MAX = 10;
    private static int THREAD_ALIVE_TIME = 10000;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    private static ThreadPoolExecutor threadPoolExecutor;

    /*线程工厂*/
    private static ThreadFactory threadFactory = new ThreadFactory() {
        private AtomicInteger integer = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MLThreadPool thread:" + integer.getAndIncrement());
        }
    };


    static {
        mlThreadPool = new MLThreadPool();
    }

    /*线程池构造方法*/
    protected MLThreadPool() {
        threadPoolExecutor = new ThreadPoolExecutor(THREAD_POLL_CORE, THREAD_POOL_MAX, THREAD_ALIVE_TIME,
                TimeUnit.SECONDS, workQueue, threadFactory);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        return threadPoolExecutor;
    }

    /*马上关闭线程池，并舍弃为完成的任务*/
    public void shutDownNow(){
        if(!threadPoolExecutor.isTerminated()){
            threadPoolExecutor.shutdownNow();
        }
    }

    /*等待线程池内所有任务都完成后,关闭线程池，*/
    public void shuttDown(){
        if(!threadPoolExecutor.isTerminated()){
            threadPoolExecutor.shutdown();
        }
    }

}
