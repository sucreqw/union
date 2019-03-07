package com.sucre.myThread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程操作网络数据包! 定义为抽象类,方便回调子类的方法
 *
 * @author sucre..
 */
abstract public class Thread4Net implements Runnable {
    // 抽象方法,要求子类必须覆盖.定义目前id索引，任务名称。
    abstract public int doWork(int index);

    // 定义锁
    private final Lock lock = new ReentrantLock();
    // 定义列表的索引.
    private int index = 0;
    // 定义列表索引上限.
    private int uIndex = 0;
    //定义线程是否自动循环
    private boolean isCircle = false;
    //定义线程上否继续工作
    private boolean isWork = true;
    private String mission = "";

    // 构造器传递索引,起始数，上限，是否循环
    public Thread4Net(int l, int u, boolean isCircle) {
        uIndex = u;
        this.isCircle = isCircle;
        this.index = l;
        //this.mission=mission;
    }

    // 线程主方法
    public void run() {

        while (isWork) {
            lock.lock();
            int i = getIndex();
            lock.unlock();
            int p = doWork(i);

            //延时操作
            //MyUtil.sleeps(9000);
            //System.out.println(p);
        }
    }

    private int getIndex() {


        int i;
        i = index;
        index++;
        if (index > uIndex) {
            index = 0;
            //告知线程停止工作
            if (!isCircle) {
                isWork = false;
            }

        }

        return i;
    }

    public void exitWork() {
        isWork = false;
    }
}
