package fans.develop.fe;

/*
    注册心跳动画所需的单元,主要存放了注册动画类型和接口(回掉函数)
 */
public class FeHeartUnit {

    //接口(回掉函数)定义
    public interface TimeOutTask {
        //count: 根据type类型告诉当前动画应该切到第几帧
        void run(int count);
    }

    //心跳类型,可以随时切换,具体定义看FeHeart.TYPE_ANIM_XXX
    public int type = 0;
    //写true后该单元将自动在链表中移除(建议调用removeUnit()来移除)
    public boolean useless = false;
    //接口(回掉函数)
    public FeHeartUnit.TimeOutTask task = null;

    public FeHeartUnit(int t, FeHeartUnit.TimeOutTask ta) {
        type = t;
        task = ta;
    }
}
