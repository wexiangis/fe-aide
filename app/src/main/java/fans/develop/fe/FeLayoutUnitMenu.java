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

    public boolean checkHit(float x, float y) {
        return false;
    }

    public void refresh() {
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }
		
		public void showUnit(int order){
				;
		}

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag) {
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
