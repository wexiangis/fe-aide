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
    private FeViewMapInfo viewMapInfo;
    private FeViewSelect viewSelect;
    private FeViewUnitInfo viewUnitInfo;

    public FeLayoutMapInfo(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;

    }

    /* ---------- function ---------- */

    public boolean checkHit(float x, float y) {
        //点击头像?
        if(viewUnitInfo != null && viewUnitInfo.checkHit(x, y))
            return true;
        return false;
    }

    public void refresh() {
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        启动 地图光标、地图信息、地图人物头像信息 显示
     */
    public void on() {
        if (onFlag)
            return;
        if(viewSelect == null)
            viewSelect = new FeViewSelect(context, sectionCallback);
        if(viewMapInfo == null)
            viewMapInfo = new FeViewMapInfo(context, sectionCallback);
        if(viewUnitInfo == null){
            viewUnitInfo = new FeViewUnitInfo(context, sectionCallback);
            addView(viewUnitInfo);
        }
        addView(viewMapInfo);
        addView(viewSelect);
        onFlag = true;
    }

    /*
        关闭 地图光标、地图信息、地图人物头像信息 显示
     */
    public void off() {
        _removeView(this, viewMapInfo);
        _removeView(this, viewMapInfo);
        onFlag = false;
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag) {
        //命中自己
        if(flag.checkFlag(FeFlagHit.HIT_MAP_INFO)) {
            //点击头像,展开人物信息菜单
            FeLayoutUnitMenu layoutUnitMenu = sectionCallback.getLayoutUnitMenu();
            if(layoutUnitMenu != null)
                layoutUnitMenu.showUnit(viewUnitInfo.order());
        }
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
