package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    地图上的人物菜单
 */
public class FeLayoutUnitMenu extends FeLayout {

    public FeLayoutUnitMenu(Context context, FeSectionCallback sectionCallback) {
        super(context);
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
        接收点击事件
     */
    public void click(float x, float y){
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
