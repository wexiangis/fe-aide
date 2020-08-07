package fans.develop.fe;

import android.content.Context;

/*
    地板标记图层,显示各个人物的行动范围
 */
public class FeLayoutMark extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;
    //标记最近 checkHit() 点击位置
    private FeViewMark hitViewMark = null;
    private FeInfoSite hitSite = null;
    //标记当前中心
    private int certenXY[] = new int[2];

    public FeLayoutMark(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */
    
    public boolean checkHit(float x, float y){
        //转换为格子位置
        FeInfoSite site = sectionCallback.getSectionMap().getRectByLocation(x, y);
        //清选中
        hitViewMark = null;
        hitSite = null;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            hitViewMark = (FeViewMark)getChildAt(i);
            hitSite = hitViewMark.checkHit(site.xGrid, site.yGrid);
            if(hitSite != null)
                return true;
        }
        //清选中
        hitViewMark = null;
        hitSite = null;
        return false;
    }

    public void refresh(){
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        根据order定位view
     */
    public FeViewMark getViewMark(int order){
        FeViewMark viewMark;
        for (int i = 0; i < getChildCount(); i++){
            viewMark = (FeViewMark)getChildAt(i);
            if(viewMark.getOrder() == order)
                return viewMark;
        }
        return null;
    }

    /*
        显示特定人物的mark范围
     */
    public void markUnit(int order, int mov, int typeMark){
        //清掉其它,每次只mark一个人
        _removeViewAll(this);
        addView(new FeViewMark(context, typeMark, order, mov, sectionCallback));
    }

    /*
        关闭特定人物的mark范围
     */
    public void cleanUnit(int order){
        ;
    }

    /*
        关闭全部人物的mark范围
     */
    public void cleanAllUnit(){
        _removeViewAll(this);
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        //没有选中unit?
        FeSectionUnit sectionUnit = sectionCallback.getSectionUnit();
        if(sectionUnit == null || sectionUnit.viewUnit == null) {
            //清mark
            _removeViewAll(this);
            return;
        }
        //是否调用过 checkHit ?
        if(!flag.checkFlag(FeFlagHit.HIT_MARK) || hitSite == null)
            return;
        //己方人物?
        if (sectionUnit.viewUnit.camp() == FeTypeCamp.BLUE) {
            //计算移动路经
            int[][] movPath = hitViewMark.getPath(
                new int[]{sectionUnit.viewUnit.x(), sectionUnit.viewUnit.y()},
                new int[]{hitSite.xGrid, hitSite.yGrid});
            if(movPath != null) {
                //移动人物
                sectionUnit.viewUnit.move(movPath);
                //减去移动力消耗
                hitViewMark.movReduceByPath(movPath);
            }
        }
        else{
            //清mark
            _removeViewAll(this);
            //非己方的移动范围,透传点击地图
            sectionCallback.onUnitSelect(false);
            FeLayoutMap layoutMap = sectionCallback.getLayoutMap();
            if(layoutMap != null)
                layoutMap.hitMap(hitSite.rect.left + 1, hitSite.rect.top + 1);
        }
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
