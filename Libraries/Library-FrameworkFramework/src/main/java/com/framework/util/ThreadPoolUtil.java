package com.framework.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @author YobertJomi
 * className ThreadPoolUtil
 * created at  2017/6/13  12:55
 */
public class ThreadPoolUtil {
    /**
     * 线程最大不要超过这个
     * 总共多少任务（根据CPU个数决定创建活动线程的个数,这样取的好处就是可以让手机承受得住）
     */
    private static final int maxThreadsNum = Runtime.getRuntime().availableProcessors() * 3 + 2;
    private static ThreadPoolUtil instance;
    /**
     * 每次只执行一个任务的线程池
     */
    private static ExecutorService singleTaskExecutor = null;
    private static ExecutorService singleTaskExecutor2 = null;
    private static ExecutorService singleTaskExecutor3 = null;

    /**
     * 每次执行限定个数个任务的线程池
     */
    private static ExecutorService limitedTaskExecutor = null;

    /**
     * 所有任务都一次性开始的线程池
     */
    private static ExecutorService allTaskExecutor = null;

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行
     */
    private static ScheduledExecutorService scheduledTaskExecutor = null;

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式）
     */
    private static ExecutorService scheduledTaskFactoryExecutor = null;
    /**
     * 线程工厂初始化方式一
     */
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    /**
     * 任务是否被取消
     */
    private boolean isCancled = false;
    /**
     * 是否点击并取消任务标示符
     */
    private boolean isClick = false;

    //    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//    scheduledThreadPool.schedule(new Runnable() {
//
//        @Override
//        public void run() {
//            System.out.println("delay 3 seconds");
//        }
//    }, 3, TimeUnit.SECONDS);
    public static ThreadPoolUtil getInstance() {
        if (null == instance) {
            synchronized (ThreadPoolUtil.class) {
                if (null == instance) {
                    instance = new ThreadPoolUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 每次只执行一个线程任务的线程池
     * 限制最大线程数的线程池 一个
     * Executors.newSingleThreadExecutor()
     */
    public static ExecutorService getInstanceSingleTaskExecutor() {
        if (null == singleTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == singleTaskExecutor) {
                    singleTaskExecutor = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
                }
            }
        }
        return singleTaskExecutor;
    }

    public static ExecutorService getInstance2SingleTaskExecutor() {
        if (null == singleTaskExecutor2) {
            synchronized (ThreadPoolUtil.class) {
                if (null == singleTaskExecutor2) {
                    singleTaskExecutor2 = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
                }
            }
        }
        return singleTaskExecutor2;
    }

    public static ExecutorService getInstance3SingleTaskExecutor() {
        if (null == singleTaskExecutor3) {
            synchronized (ThreadPoolUtil.class) {
                if (null == singleTaskExecutor3) {
                    singleTaskExecutor3 = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
                }
            }
        }
        return singleTaskExecutor3;
    }

    /**
     * 每次执行限定个数个任务的线程池（不传默认为3个）
     * 限制最大线程数的线程池 nThreads
     * Executors.newFixedThreadPool(3)
     */
    public static ExecutorService getInstanceLimitedTaskExecutor(int nThreads) {
        if (null == limitedTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == limitedTaskExecutor) {
                    limitedTaskExecutor = Executors.newFixedThreadPool(nThreads);// 限制线程池大小为3的线程池
                }
            }
        }
        return limitedTaskExecutor;
    }

    /**
     * 每次执行限定个数个任务的线程池（不传默认为3个）
     * 限制最大线程数的线程池3
     * Executors.newFixedThreadPool(3)
     */
    public static ExecutorService getInstanceLimitedTaskExecutor() {
        if (null == limitedTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == limitedTaskExecutor) {
                    limitedTaskExecutor = Executors.newFixedThreadPool(3);// 限制线程池大小为3的线程池
                }
            }
        }
        return limitedTaskExecutor;
    }

    /**
     * 所有任务都一次性开始的线程池
     * 没有限制最大线程数的线程池
     * Executors.newCachedThreadPool()
     */
    public static ExecutorService getInstanceAllTaskExecutor() {
        if (null == allTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == allTaskExecutor) {
                    allTaskExecutor = Executors.newCachedThreadPool(); // 一个没有限制最大线程数的线程池
                }
            }
        }
        return allTaskExecutor;
    }

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行
     * 限制最大线程数的线程池 nThreads
     * Executors.newScheduledThreadPool(3)
     */
    public static ScheduledExecutorService getInstanceScheduledTaskExecutor(int nThreads) {
        if (null == scheduledTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == scheduledTaskExecutor) {
                    scheduledTaskExecutor = Executors.newScheduledThreadPool(nThreads);// 一个可以按指定时间可周期性的执行的线程池
                }
            }
        }
        return scheduledTaskExecutor;
    }

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行
     * 限制最大线程数的线程池 默认为3
     * Executors.newScheduledThreadPool(3)
     */
    public static ScheduledExecutorService getInstanceScheduledTaskExecutor() {
        if (null == scheduledTaskExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == scheduledTaskExecutor) {
                    scheduledTaskExecutor = Executors.newScheduledThreadPool(3);// 一个可以按指定时间可周期性的执行的线程池
                }
            }
        }
        return scheduledTaskExecutor;
    }

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式）
     * 限制最大线程数的线程池 nThreads
     * Executors.newFixedThreadPool(3,new MyThreadFactory())
     */
    public static ExecutorService getInstanceScheduledTaskFactoryExecutor(int nThreads) {
        if (null == scheduledTaskFactoryExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == scheduledTaskFactoryExecutor) {
                    scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(nThreads,
                            new MyThreadFactory());// 按指定工厂模式来执行的线程池
//                    scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(3,
//                            threadFactory);// 按指定工厂模式来执行的线程池
                }
            }
        }
        return scheduledTaskFactoryExecutor;
    }

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式）
     * 限制最大线程数的线程池 默认为3
     * Executors.newFixedThreadPool(3,new MyThreadFactory())
     */
    public static ExecutorService getInstanceScheduledTaskFactoryExecutor() {
        if (null == scheduledTaskFactoryExecutor) {
            synchronized (ThreadPoolUtil.class) {
                if (null == scheduledTaskFactoryExecutor) {
                    scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(3,
                            new MyThreadFactory());// 按指定工厂模式来执行的线程池
//                    scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(3,
//                            threadFactory);// 按指定工厂模式来执行的线程池
                }
            }
        }
        return scheduledTaskFactoryExecutor;
    }

    /**
     * submit任务及取得结果
     *
     * @param result 传入任务的泛型
     *               return  Future<T>;
     */
    public static <T> Future<T> submit(Runnable task, T result) {
        if (null == singleTaskExecutor) {
            Future<T> future = singleTaskExecutor.submit(task, result);
            return future;
        }
        if (null == limitedTaskExecutor) {
            Future<T> future = limitedTaskExecutor.submit(task, result);
            return future;
        }
        if (null == allTaskExecutor) {
            Future<T> future = allTaskExecutor.submit(task, result);
            return future;
        }
        if (null == scheduledTaskExecutor) {
            Future<T> future = scheduledTaskExecutor.submit(task, result);
            return future;
        }
        if (null == scheduledTaskFactoryExecutor) {
            Future<T> future = scheduledTaskFactoryExecutor.submit(task, result);
            return future;
        }
        return null;
    }

    /**
     * submit任务及取得结果
     *
     * @param checkIsComplete 是否需要返回任务是否成功
     * @param result          传入任务的泛型
     *                        return  boolean;是否任务完成
     */
    public static <T> boolean submit(Runnable task, T result, boolean checkIsComplete) {
        if (checkIsComplete) {
            try {
                if (null == singleTaskExecutor) {
                    Future<T> future = singleTaskExecutor.submit(task, result);
                    return (null == future.get());
                }
                if (null == limitedTaskExecutor) {
                    Future<T> future = limitedTaskExecutor.submit(task, result);
                    return (null == future.get());
                }
                if (null == allTaskExecutor) {
                    Future<T> future = allTaskExecutor.submit(task, result);
                    return (null == future.get());
                }
                if (null == scheduledTaskExecutor) {
                    Future<T> future = scheduledTaskExecutor.submit(task, result);
                    return (null == future.get());
                }
                if (null == scheduledTaskFactoryExecutor) {
                    Future<T> future = scheduledTaskFactoryExecutor.submit(task, result);
                    return (null == future.get());
                }
            } catch (InterruptedException e) {
                return false;
            } catch (ExecutionException e) {
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * submit任务不需要返回结果
     * return  void;
     */
    public static void submit(Runnable task) {
        if (null == singleTaskExecutor) {
            singleTaskExecutor.submit(task);
            return;
        }
        if (null == limitedTaskExecutor) {
            limitedTaskExecutor.submit(task);
            return;
        }
        if (null == allTaskExecutor) {
            allTaskExecutor.submit(task);
            return;
        }
        if (null == scheduledTaskExecutor) {
            scheduledTaskExecutor.submit(task);
            return;
        }
        if (null == scheduledTaskFactoryExecutor) {
            scheduledTaskFactoryExecutor.submit(task);
            return;
        }
    }

    /**
     * shutdown
     * return  void;
     */
    public static void shutdown() {
        if (null == singleTaskExecutor) {
            singleTaskExecutor.shutdown();
        }
        if (null == limitedTaskExecutor) {
            limitedTaskExecutor.shutdown();
        }
        if (null == allTaskExecutor) {
            allTaskExecutor.shutdown();
        }
        if (null == scheduledTaskExecutor) {
            scheduledTaskExecutor.shutdown();
        }
        if (null == scheduledTaskFactoryExecutor) {
            scheduledTaskFactoryExecutor.shutdown();
        }
    }

    /**
     * shutdown
     * return  void;
     */
    public static void shutdownNow() {
        if (null == singleTaskExecutor) {
            singleTaskExecutor.shutdownNow();
        }
        if (null == limitedTaskExecutor) {
            limitedTaskExecutor.shutdownNow();
        }
        if (null == allTaskExecutor) {
            allTaskExecutor.shutdownNow();
        }
        if (null == scheduledTaskExecutor) {
            scheduledTaskExecutor.shutdownNow();
        }
        if (null == scheduledTaskFactoryExecutor) {
            scheduledTaskFactoryExecutor.shutdownNow();
        }
    }

    /**
     * 线程工厂初始化方式二
     */
    private static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("MyThreadFactory");
            thread.setDaemon(true); // 将用户线程变成守护线程,默认false
            return thread;
        }
    }
}
