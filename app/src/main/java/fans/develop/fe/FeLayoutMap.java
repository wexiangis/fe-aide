package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    加载和管理地图及地形动画
 */
public class FeLayoutMap extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;

    // 同时只显示一张地图
    private FeViewMap viewMap = null;
    // 同时只显示一张背景
    private View viewBackground = null;
    // 同时只显示一张地图背景
    // private FeViewMap viewMapBackground = null;

    public FeLayoutMap(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */
    
    public boolean checkHit(float x, float y){
        if(viewMap != null)
            return true;
        return false;
    }
    
    /*
        刷新,一般在移动地图之后
     */
    public void refresh(){
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        加载地图
     */
    public void loadMap(int section){
        //只容存在一张地图
        if(viewMap != null)
            _removeView(this, viewMap);
        //更换了地图,重新初始化参数
        sectionCallback.refreshSectionMap(section);
        //添加地图view
        viewMap = new FeViewMap(context, sectionCallback);
        addView(viewMap);
    }

    /*
        加载背景,将遮挡住地图,用于战斗、对话剧情等
     */
    public void loadBackground(){
        //只容存在一张背景
        if(viewBackground != null)
            _removeView(this, viewBackground);
        //添加view
        viewBackground = new View(context);
        addView(viewBackground);
    }

    /*
        捕捉,保持指定人物或地点在地图显示的中心
        id = -1 释放
     */
    public void catchUnit(int id){
        ;
    }
    public void catchPoint(int xGrid, int yGrid){
        ;
    }

    //动态挪动地图,x>0时地图往右移,y>0时地图往下移
    public void moveGrid(int xGrid, int yGrid){
        if(viewMap == null)
            return;
        viewMap.moveGrid(xGrid, yGrid);
    }

    //动态挪动地图,设置(x,y)所在格子为地图中心
    public void moveCenter(int xGrid, int yGrid){
        if(viewMap == null)
            return;
        viewMap.moveCenter(xGrid, yGrid);
    }

    //动态挪动地图,设置(x,y)所在格子到地图能包围到
    public void moveInclude(int xGrid, int yGrid){
        if(viewMap == null)
            return;
        viewMap.moveInclude(xGrid, yGrid);
    }

    //设置(x,y)所在格子为地图中心
    public void setCenter(int xGrid, int yGrid){
        if(viewMap == null)
            return;
        viewMap.setCenter(xGrid, yGrid);
    }

    /*
        移动地图
     */
    public void move(int xGridErr, int yGridErr){
        if(viewMap != null){
            FeSectionMap sectionMap = sectionCallback.getSectionMap();
            //累积差值,该差值会在 FeViewMap 的心跳函数内慢慢吃掉,最后恢复为0
            sectionMap.xGridErr += xGridErr;
            sectionMap.yGridErr += yGridErr;
            //防止把地图移出屏幕
            if (sectionMap.xGridErr < 0)
                sectionMap.xGridErr = 0;
            else if (sectionMap.xGridErr + sectionMap.screenXGrid > sectionMap.mapInfo.width)
                sectionMap.xGridErr = sectionMap.mapInfo.width - sectionMap.screenXGrid;
            if (sectionMap.yGridErr < 0)
                sectionMap.yGridErr = 0;
            else if (sectionMap.yGridErr + sectionMap.screenYGrid > sectionMap.mapInfo.height)
                sectionMap.yGridErr = sectionMap.mapInfo.height - sectionMap.screenYGrid;
            //输入坐标求格子位置,更新地图选中点信息
            sectionCallback.getSectionMap().getRectByGrid(xGridErr, yGridErr, sectionCallback.getSectionMap().selectSite);
            //置标记
            sectionCallback.onMapMove(true);
        }
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        //点击对象不是自己 或 点击的unit不是第一次点击操作
        if(!flag.checkFlag(FeFlagHit.HIT_MAP) &&
            (flag.checkFlag(FeFlagHit.HIT_UNIT) || sectionCallback.onUnitMove()))
            return;
        if(viewMap != null)
            hitMap(x, y);
    }

    public void hitMap(float x, float y){
        //输入坐标求格子位置,更新地图选中点信息
        sectionCallback.getSectionMap().getRectByLocation(x, y, sectionCallback.getSectionMap().selectSite);
        //置标记
        sectionCallback.onMapHit(true);
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        _removeViewAll(this);
        return true;
    }
    public void onReload(){
        ;
    }
}
