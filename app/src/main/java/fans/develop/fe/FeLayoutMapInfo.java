package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    显示地图光标处的地形信息
 */
public class FeLayoutMapInfo extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;
    private Boolean onFlag = false;

    public FeLayoutMapInfo(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;

    }

    /* ---------- function ---------- */

    public boolean checkHit(float x, float y){
        return false;
    }

    public void refresh(){
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        启动 地图光标、地图信息、地图人物头像信息 显示
     */
    public void on(){
        if(onFlag)
            return;
        addView(new FeViewSelect(context, sectionCallback));
        addView(new FeViewMapInfo(context, sectionCallback));
        addView(new FeViewUnitInfo(context, sectionCallback));
        onFlag = true;
    }

    /*
        关闭 地图光标、地图信息、地图人物头像信息 显示
     */
    public void off(){
        removeAllViews();
        onFlag = false;
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        ;
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof FeView)
                ((FeView)v).onDestory();
        }
        return true;
    }
    public void onReload(){
        ;
    }
}
