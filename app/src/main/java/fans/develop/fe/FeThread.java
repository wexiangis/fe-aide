package fans.develop.fe;

import android.os.Handler;

/*
    批量异步线程操作
    使用:
    FeThread t = new FeThread(xxx);
    t.start();
    t.join();//如果需要
 */
public class FeThread extends Thread {

    private Runnable[] runnables;
    private Handler handler;
    private int[] delay;

    /*
        UI线程
     */
    public FeThread(int[] delay, Runnable... runnables) {
        this.handler = new Handler();
        this.delay = delay;
        this.runnables = new Runnable[runnables.length];
        for (int i = 0; i < runnables.length && i < delay.length; i++)
            this.runnables[i] = runnables[i];
    }

    /*
        普通线程
     */
    public FeThread(Runnable... runnables) {
        this.handler = null;
        this.runnables = new Runnable[runnables.length];
        for (int i = 0; i < runnables.length; i++)
            this.runnables[i] = runnables[i];
    }

    @Override
    public void run() {
        if (delay != null) {
            for (int i = 0; i < runnables.length; i++)
                handler.postDelayed(runnables[i], delay[i]);
        } else {
            Thread[] threads = new Thread[runnables.length];
            //start
            for (int i = 0; i < runnables.length; i++) {
                threads[i] = new Thread(runnables[i]);
                threads[i].start();
            }
            //使用join()等待所有任务结束
            for (int i = 0; i < runnables.length; i++) {
                try {
                    threads[i].join();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }
}
