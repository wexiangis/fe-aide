package fans.develop.fe;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/*
    全局动画统一心跳管理,在FeSave中实例化1个;
    通过注册接口(回掉函数)的方式实现切帧;
    使用addUnit()和removeUnit()来注册和移除动画心跳
 */
public class FeHeart {

    // ---------- 链表部分 ----------

    //本地定义一个链表头
    private FeChain<FeHeartUnit> chain = new FeChain<FeHeartUnit>(new FeHeartUnit(0, new FeHeartUnit.TimeOutTask() {
        public void run(int c) {
        }
    }));

    //添加链表单元的方式申请心跳
    public void addUnit(FeHeartUnit u) {
        FeChain<FeHeartUnit> tmp = chain;
        while (tmp.next != null)
            tmp = tmp.next;
        FeChain<FeHeartUnit> tmp2 = new FeChain<FeHeartUnit>(u);
        tmp2.previous = tmp;
        tmp.next = tmp2;
    }

    //移除单元
    public void removeUnit(FeHeartUnit u) {
        FeChain<FeHeartUnit> tmp = chain.next;
        while (tmp != null) {
            if (tmp.data == u) {
                FeChain<FeHeartUnit> tmp2 = tmp.next;
                tmp.previous.next = tmp2;
                if (tmp2 != null)
                    tmp2.previous = tmp.previous;
                break;
            }
            tmp = tmp.next;
        }
    }

    //根据type遍历链表,然后回掉接口函数,传参count为当前要播放第几帧
    private boolean scanChain(int type, int count) {
        FeChain<FeHeartUnit> tmp = chain.next;
        boolean hit = false;
        try {
            while (tmp != null) {
                FeHeartUnit u = tmp.data;
                tmp = tmp.next;
                //移除单元
                if (u.useless)
                    removeUnit(u);
                    //回调单元的方法
                else if (u.type == type) {
                    u.task.run(count);
                    hit = true;
                }
            }
        } catch (java.lang.RuntimeException e) {
            Log.e("Heart: scanChain()", "runtime error");
            return false;
        }
        return hit;
    }

    // ---------- 心跳类型 ----------

    //type定义
    public static final int TYPE_ANIM_STAY = 1;//人物原地待机或选中时动画,共3帧,钟摆式循环播放
    public static final int TYPE_ANIM_SELECT = 2;//人物原地待机或选中时动画,共3帧,钟摆式循环播放
    public static final int TYPE_ANIM_MOVE = 3;//人物移动时动画,共4帧,循环播放
    public static final int TYPE_FRAME_HEART = 4;//帧动画心跳,不限帧数,周期100ms
    public static final int TYPE_TOTAL = 4;

    //高速type定义
    public static final int TYPE_FRAME_HEART_QUICK = 11;//高速帧动画心跳,不限帧数,周期30ms
    public static final int TYPE_QUICK_TOTAL = 1;

    // ---------- 心跳间隔数组和计数 ----------

    //type 1 TYPE_ANIM_STAY
    private final int[] circleType1 = new int[]{5, 1, 5};//每帧延时
    private int circleType1_timerCount = 1, circleType1_count = 0;
    private boolean circleType1_dir = false;

    //type 2 TYPE_ANIM_SELECT
    private final int[] circleType2 = new int[]{5, 1, 5};//每帧延时
    private int circleType2_timerCount = 1, circleType2_count = 0;
    private boolean circleType2_dir = false;

    //type 3 TYPE_ANIM_MOVE
    private final int[] circleType3 = new int[]{1, 1, 1, 1};//每帧延时
    private int circleType3_timerCount = 1, circleType3_count = 0;

    //type 4 TYPE_FRAME_HEART
    private int circleType4_count = 0;

    //type 11 TYPE_FRAME_HEART_QUICK
    private int circleType11_count = 0;

    // ---------- 心跳定时器 ----------

    //定时器
    private final int TIMER_PERIOD = 100;//定时器周期ms
    private Timer[] timer = new Timer[TYPE_TOTAL];
    private TimerTask[] timerTask = new TimerTask[TYPE_TOTAL];
    //高速定时器
    private final int TIMER_QUICK_PERIOD = 30;//高速定时器周期ms
    private Timer[] timerQuick = new Timer[TYPE_QUICK_TOTAL];
    private TimerTask[] timerTaskQuick = new TimerTask[TYPE_QUICK_TOTAL];

    public void start() {
        /* ----- type 1 TYPE_ANIM_STAY ----- */
        timer[0] = new Timer();
        timerTask[0] = new TimerTask() {
            @Override
            public void run() {
                if (++circleType1_timerCount > circleType1[circleType1_count]) {
                    circleType1_timerCount = 1;
                    //
                    if (circleType1_dir) {
                        if (--circleType1_count <= 0) {
                            circleType1_count = 0;
                            circleType1_dir = !circleType1_dir;
                        }
                    } else {
                        if (++circleType1_count >= circleType1.length - 1) {
                            circleType1_count = circleType1.length - 1;
                            circleType1_dir = !circleType1_dir;
                        }
                    }
                    //
                    scanChain(TYPE_ANIM_STAY, circleType1_count);
                }
            }
        };
        /* ----- type 2 TYPE_ANIM_SELECT ----- */
        timer[1] = new Timer();
        timerTask[1] = new TimerTask() {
            @Override
            public void run() {
                if (++circleType2_timerCount > circleType2[circleType2_count]) {
                    circleType2_timerCount = 1;
                    //
                    if (circleType2_dir) {
                        if (--circleType2_count <= 0) {
                            circleType2_count = 0;
                            circleType2_dir = !circleType2_dir;
                        }
                    } else {
                        if (++circleType2_count >= circleType2.length - 1) {
                            circleType2_count = circleType2.length - 1;
                            circleType2_dir = !circleType2_dir;
                        }
                    }
                    //
                    if (!scanChain(TYPE_ANIM_SELECT, circleType2_count)) {
                        //随时就绪,下次有人被选中时从第一帧开始播放
                        circleType2_dir = false;
                        circleType2_count = 1;
                    }
                }
            }
        };
        /* ----- type 3 TYPE_ANIM_MOVE ----- */
        timer[2] = new Timer();
        timerTask[2] = new TimerTask() {
            @Override
            public void run() {
                if (++circleType3_timerCount > circleType3[circleType3_count]) {
                    circleType3_timerCount = 1;
                    //
                    if (++circleType3_count >= circleType3.length)
                        circleType3_count = 0;
                    //
                    scanChain(TYPE_ANIM_MOVE, circleType3_count);
                }
            }
        };
        /* ----- type 4 TYPE_FRAME_HEART ----- */
        timer[3] = new Timer();
        timerTask[3] = new TimerTask() {
            @Override
            public void run() {
                circleType4_count += 1;
                scanChain(TYPE_FRAME_HEART, circleType4_count);
            }
        };
        /* ----- type 1 TYPE_FRAME_HEART_QUICK ----- */
        timerQuick[0] = new Timer();
        timerTaskQuick[0] = new TimerTask() {
            @Override
            public void run() {
                circleType11_count += 1;
                scanChain(TYPE_FRAME_HEART_QUICK, circleType11_count);
            }
        };
        // all initialize
        for (int i = 0; i < timer.length; i++)
            timer[i].schedule(timerTask[i], 200, TIMER_PERIOD);
        for (int i = 0; i < timerQuick.length; i++)
            timer[i].schedule(timerTaskQuick[i], 200, TIMER_QUICK_PERIOD);
    }

    public void stop() {
        if (timer != null) {
            // all cancel
            for (int i = 0; i < timer.length; i++) {
                timerTask[i].cancel();
                timerTask[i] = null;
                timer[i].cancel();
                timer[i] = null;
            }
            for (int i = 0; i < timerQuick.length; i++) {
                timerTaskQuick[i].cancel();
                timerTaskQuick[i] = null;
                timerQuick[i].cancel();
                timerQuick[i] = null;
            }
        }
    }
}
