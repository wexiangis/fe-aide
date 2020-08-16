package fans.develop.fe;

import android.content.*;
import android.view.View;

/*
    过场动画图层(例如轮到一方阵营回合过场动画)
    动画进行中禁止触屏操作
 */
public class FeLayoutInterlude extends FeLayout {
    private FeSectionCallback sectionCallback;

    public FeLayoutInterlude(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */

    /*
        阵营切换动画
     */
    public void campSwitch(int camp) {
        sectionCallback.onTouchDisable(true);
        ;
        sectionCallback.onTouchDisable(false);
    }

    /*
        获得物品、物品损坏等自动结束弹窗
     */
    public void tips(String tips) {
        ;
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack() {
        return false;
    }

    public boolean onDestory() {
        //释放子view
        _removeViewAll(this);
        return true;
    }

    public void onReload() {
        ;
    }
}
