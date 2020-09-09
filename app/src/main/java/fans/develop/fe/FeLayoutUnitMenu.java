package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    地图上的人物菜单
 */
public class FeLayoutUnitMenu extends FeLayout {

    private FeSectionCallback sectionCallback;
    //人物菜单
    private FeViewUnitMenu viewUnitMenu;
    private boolean showUnitMenu = false;
    
    public FeLayoutUnitMenu(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */

    public boolean checkHit(float x, float y) {
        //屏霸模式
        if(showUnitMenu)
            return true;
        return false;
    }

    public void refresh() {
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }
    
    /*
        显示人物菜单
     */
    public void showUnit(int order){
        if(showUnitMenu)
            return;
        showUnitMenu = true;
        //初始化
        if(viewUnitMenu == null)
            viewUnitMenu = new FeViewUnitMenu(sectionCallback.getContext(), sectionCallback, order);
        viewUnitMenu.order(order);
        //移除其它人物菜单
        _removeViewAll(this);
        //开始显示
        addView(viewUnitMenu);
    }

    /*
     移动地图
     */
    public void move(float xErr, float yErr) {
        //人物菜单中
        if(showUnitMenu)
            viewUnitMenu.move(xErr, yErr);
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag) {
        //命中自己
        if(flag.checkFlag(FeFlagHit.HIT_UNIT_MENU)){
            //人物菜单中
            if(showUnitMenu)
                viewUnitMenu.click(x, y);
        }
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack() {
        //返回键时退出人物菜单
        if(showUnitMenu){
            showUnitMenu = false;
            _removeViewAll(this);
            return true;
        }
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
