package fans.develop.fe;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/*
    章节运行关键参数之数据部分
 */
public class FeLayoutSection extends FeLayout{

    private FeData feData = null;
    // 存档槽位,从0数起
    private int sx = 0;
    // 存档数据
    private FeAssetsSX sxData = null;
    // 0/重新加载 1/中断继续
    private int startMode = 0;
    // 当前章节
    private int section = 0;
    // 参数集
    private FeSectionMap sectionMap = null;
    private FeSectionUnit sectionUnit = null;
    private FeSectionShader sectionShader = null;
    private FeSectionOperation sectionOperation = null;
    // 所有图层
    private FeLayoutMap layoutMap = null;
    private FeLayoutMarkEnemy layoutMarkEnemy = null;
    private FeLayoutMark layoutMark = null;
    private FeLayoutUnit layoutUnit = null;
    private FeLayoutMapInfo layoutMapInfo = null;
    private FeLayoutUnitMenu layoutUnitMenu = null;
    private FeLayoutSysMenu layoutSysMenu = null;
    private FeLayoutChat layoutChat = null;
    private FeLayoutInterlude layoutInterlude = null;
    private FeLayoutDebug layoutDebug = null;
    //debug
    private TextView dbTouchXY = null;
    private TextView dbTouchGridXY = null;

    //sx: 存档位置 startMode: 0/重新加载 1/中断继续
    public FeLayoutSection(FeData feData, int sx, int startMode){
        super(feData.context);
        this.feData = feData;
        this.sx = sx;
        this.startMode = startMode;
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
        this.reload();
    }

    public void reload(){

        sectionOperation = null;
        this._removeViewAll(this);

        //显示loading界面
        this.addView(new FeLayoutLoading(feData.context, 0, this,
                new FeLayoutLoading.DoInBackground<FeLayoutSection>() {
                    @Override
                    public String run(FeLayoutSection obj, FeLayoutLoading layoutLoading) {
                        try {
                            Thread.sleep(100);

                            //从文件加载章节存档数据
                            if(startMode == 1)
                                sxData = feData.assets.save.recoverSx(sx);
                            else
                                sxData = feData.assets.save.loadSx(sx);

                            layoutLoading.setPercent(10);//百分比进度

                            //章节解析
                            if(sxData != null)
                                section = sxData.info.getSection();
                            else
                                return "章节数据加载失败！！";

                            layoutLoading.setPercent(15);//百分比进度

                            //初始化参数集
                            sectionCallback.refreshSectionMap(section);//sectionMap
                            sectionUnit = new FeSectionUnit();
                            sectionShader = new FeSectionShader(sectionCallback);

                            layoutLoading.setPercent(20);//百分比进度

                            //地图图层
                            layoutMap = new FeLayoutMap(feData.context, sectionCallback);

                            layoutLoading.setPercent(25);//百分比进度

                            //敌军标记格图层
                            layoutMarkEnemy = new FeLayoutMarkEnemy(feData.context, sectionCallback);

                            //标记格图层
                            layoutMark = new FeLayoutMark(feData.context, sectionCallback);

                            layoutLoading.setPercent(30);//百分比进度

                            //人物动画图层
                            layoutUnit = new FeLayoutUnit(feData.context, sectionCallback);

                            layoutLoading.setPercent(35);//百分比进度

                            //地图地形信息
                            layoutMapInfo = new FeLayoutMapInfo(feData.context, sectionCallback);

                            layoutLoading.setPercent(40);//百分比进度

                            //人物操作菜单图层
                            layoutUnitMenu = new FeLayoutUnitMenu(feData.context, sectionCallback);

                            layoutLoading.setPercent(45);//百分比进度

                            // 系统菜单图层
                            layoutSysMenu = new FeLayoutSysMenu(feData.context, sectionCallback);

                            layoutLoading.setPercent(50);//百分比进度

                            //人物对话图层
                            layoutChat = new FeLayoutChat(feData.context, sectionCallback);

                            layoutLoading.setPercent(55);//百分比进度

                            //过场动画图层
                            layoutInterlude = new FeLayoutInterlude(feData.context, sectionCallback);

                            layoutLoading.setPercent(60);//百分比进度

                            //debug图层
                            layoutDebug = new FeLayoutDebug(feData.context, sectionCallback);

                            layoutLoading.setPercent(65);//百分比进度

                            //加载地图
                            layoutMap.loadMap(section);

                            layoutLoading.setPercent(70);//百分比进度

                            //人物加载
                            for(int i = 0; i < sxData.saveCache.unit.total(); i++){
                                layoutUnit.addUnit(
                                    sxData.saveCache.unit.getId(i),
                                    sxData.saveCache.unit.getX(i),
                                    sxData.saveCache.unit.getY(i),
                                    FeTypeCamp.values()[sxData.saveCache.unit.getCamp(i)]);
                            }

                            layoutLoading.setPercent(75);//百分比进度

                            //启动地图信息显示
                            layoutMapInfo.on();

                            layoutLoading.setPercent(80);//百分比进度

                            //debug
                            dbTouchXY = layoutDebug.addInfo(0x80800000, false);
                            dbTouchGridXY = layoutDebug.addInfo(0x80000080, false);

                            layoutLoading.setPercent(100);//百分比进度

                            Thread.sleep(100);

                        } catch (java.lang.InterruptedException e) { }
                        return null;
                    }
                },
                new FeLayoutLoading.DoInFinal<FeLayoutSection>() {
                    @Override
                    public void run(FeLayoutSection obj, String result) {

                        //移除loading界面
                        obj._removeViewAll(obj);

                        //初始化失败
                        if(result != null){
                            Toast.makeText(feData.activity, result, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        /* ----- 数据就绪,加载图层 ----- */

                        //地图图层
                        obj.addView(layoutMap);
                        //标记格图层
                        obj.addView(layoutMarkEnemy);
                        //标记格图层
                        obj.addView(layoutMark);
                        //人物动画图层
                        obj.addView(layoutUnit);
                        //地图地形信息
                        obj.addView(layoutMapInfo);
                        //人物操作菜单图层
                        obj.addView(layoutUnitMenu);
                        //人物对话图层
                        obj.addView(layoutChat);
                        // 系统菜单图层
                        obj.addView(layoutSysMenu);
                        //过场动画图层
                        obj.addView(layoutInterlude);
                        //debug图层
                        obj.addView(layoutDebug);

                        /* ----- 开始 ----- */

                        sectionOperation = new FeSectionOperation(sectionCallback);
                    }
                }
        ));
    }

    /* ---------- 触屏回调,提取事件 ---------- */

    public boolean onTouchEvent(MotionEvent event) {
        if(sectionOperation != null)
            return sectionOperation.onTouchEvent(event);
        return false;
    }

    /* ---------- 控件事件回调 ---------- */

    public FeSectionCallback sectionCallback = new FeSectionCallback()
    {
        public void addHeartUnit(FeHeartUnit heartUnit){
            feData.addHeartUnit(heartUnit);
        }
        public void removeHeartUnit(FeHeartUnit heartUnit){
            feData.removeHeartUnit(heartUnit);
        }

        /* ------------------------------- */

        public void refreshSectionMap(int section){
            //不重复初始化
            if(sectionMap != null)
                if(sectionMap.section == section)
                    return;
            //获取屏幕宽高信息
            DisplayMetrics dm = new DisplayMetrics();
            feData.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            //重新初始化map
            sectionMap = new FeSectionMap(section, feData.assets.map.getMap(section), dm.widthPixels, dm.heightPixels);
        }
        public void refresh(){
            //更新地图
            layoutMap.refresh();
            //更新标记格
            layoutMarkEnemy.refresh();
            //更新标记格
            layoutMark.refresh();
            //更新人物动画
            layoutUnit.refresh();
            //更新地形信息
            layoutMapInfo.refresh();
            //更新人物菜单
            layoutUnitMenu.refresh();
            //更新对话
            layoutChat.refresh();
            //更新系统菜单
            layoutSysMenu.refresh();
            //debug图层
            // layoutDebug.refresh();
        }
        public FeFlagHit checkHit(float x, float y){
            FeFlagHit flag = new FeFlagHit();
            //先清空
            flag.cleanFlagAll();
            //点击:系统菜单中?
            if(layoutSysMenu.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_SYS_MENU);
            //点击:正在对话?
            if(layoutChat.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_CHAT);
            //点击:人物菜单中?
            if(layoutUnitMenu.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_UNIT_MENU);
            //点击:选中人物?
            if(layoutUnit.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_UNIT);
            //点击:标记格
            if(layoutMark.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_MARK);
            //点击:敌军标记格
            if(layoutMarkEnemy.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_MARK_ENEMY);
            //点击:地图信息?
            if(layoutMapInfo.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_MAP_INFO);
            //点击:地图?
            if(layoutMap.checkHit(x, y))
                flag.setFlag(FeFlagHit.HIT_MAP);
            //debug
            // dbTouchXY.setText(String.format("Touch XY: %.2f, %.2f", x, y));
            // dbTouchGridXY.setText(String.format("Touch Grid XY: %d, %d", sectionMap.selectSite.xGrid, sectionMap.selectSite.yGrid));
            return flag;
        }

        /* ------------------------------- */

        public FeAssets getAssets(){
            return feData.assets;
        }
        public Context getContext(){
            return feData.context;
        }
        public FeSectionMap getSectionMap(){
            return sectionMap;
        }
        public FeSectionUnit getSectionUnit(){
            return sectionUnit;
        }
        public FeSectionShader getSectionShader(){
            return sectionShader;
        }

        /* ------------------------------- */

        public FeLayoutMap getLayoutMap(){
            return layoutMap;
        }
        public FeLayoutMarkEnemy getLayoutMarkEnemy(){
            return layoutMarkEnemy;
        }
        public FeLayoutMark getLayoutMark(){
            return layoutMark;
        }
        public FeLayoutUnit getLayoutUnit(){
            return layoutUnit;
        }
        public FeLayoutMapInfo getLayoutMapInfo(){
            return layoutMapInfo;
        }
        public FeLayoutUnitMenu getLayoutUnitMenu(){
            return layoutUnitMenu;
        }
        public FeLayoutChat getLayoutChat(){
            return layoutChat;
        }
        public FeLayoutSysMenu getLayoutSysMenu(){
            return layoutSysMenu;
        }
        public FeLayoutInterlude getLayoutInterlude(){
            return layoutInterlude;
        }
        public FeLayoutDebug getLayoutDebug(){
            return layoutDebug;
        }

        /* ------------------------------- */

        private Boolean onTouchDisable = false;
        public void onTouchDisable(Boolean disable){
            onTouchDisable = disable;
        }
        public Boolean onTouchDisable(){
            return onTouchDisable;
        }

        private Boolean onMapMove = false;
        public void onMapMove(Boolean on){
            onMapMove = on;
        }
        public Boolean onMapMove(){
            return onMapMove;
        }

        private Boolean onMapHit = false;
        public void onMapHit(Boolean on){
            onMapHit = on;
        }
        public Boolean onMapHit(){
            return onMapHit;
        }

        private Boolean onUnitSelect = false;
        public void onUnitSelect(Boolean on){
            onUnitSelect = on;
        }
        public Boolean onUnitSelect(){
            return onUnitSelect;
        }

        private Boolean onUnitMove = false;
        public void onUnitMove(Boolean on){
            onUnitMove = on;
        }
        public Boolean onUnitMove(){
            return onUnitMove;
        }

        private Boolean onUnitMenu = false;
        public void onUnitMenu(Boolean on){
            onUnitMenu = on;
        }
        public Boolean onUnitMenu(){
            return onUnitMenu;
        }

        private Boolean onSysMenu = false;
        public void onSysMenu(Boolean on){
            onSysMenu = on;
        }
        public Boolean onSysMenu(){
            return onSysMenu;
        }
    };
}
