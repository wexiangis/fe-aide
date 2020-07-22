package fans.develop.fe;

import android.content.Context;
import android.view.View;
import android.graphics.Path;

/*
    地图中的人物动画管理
 */
public class FeLayoutUnit extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;
	//缓存checkHit选中
	private FeViewUnit hitViewUnit = null;

    public FeLayoutUnit(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */

    public void refresh(){
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    public boolean checkHit(float x, float y){
        FeViewUnit viewUnit;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewUnit = (FeViewUnit)getChildAt(i);
            if (viewUnit.checkHit(x, y)) {
                hitViewUnit = viewUnit;
                return true;
            }
        }
        return false;
    }

    /*
        人员增删
     */
    public void addUnit(int id, int y, int x, FeCamp camp){
        addView(new FeViewUnit(context, id, x, y, camp, sectionCallback));
    }
    public void removeUnit(int id){
        ;
    }

    /*
        选中人物
     */
    public void unitSelect(int id){
        ;
    }

    /*
        人物按轨迹行走
        path: 格子位置(x，y),组成的路径,注意两个节点之间只能x或y一个值变动,
            例如: [0,0] -> [1,1] 是错误的, 要改为 [0,0] -> [0,1] -> [1,1]
        sectionCallback: 移动结束后要做什么(一般把人物 setStay())
     */
    public void unitWalk(int id, Path path, Runnable sectionCallback){
        ;
    }

    /*
        释放选中
     */
    public void unitRelease(int id){
        ;
    }

    /*
        人员阵营
     */
    public void setCamp(int id, FeCamp camp){
        ;
    }

    /*
        人员可行动
     */
    public void setActivity(int id){
        ;
    }

    /*
        人员待机
     */
    public void setStay(int id){
        ;
    }

    /*
        人员异常状态
     */
    public void setError(int id, int errorType){
        ;
    }

    /*
        人员动画
        animMode: 0/待机 1/选中 2,3,4,5/上,下,左,右
     */
    public void setAnim(int id, FeAnim anim){
        ;
    }

    /*
        清所有人物选中状态
     */
    public void setAnim(FeViewUnit target, FeAnim anim){
        FeViewUnit viewUnit;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewUnit = (FeViewUnit)getChildAt(i);
            if(viewUnit == target)
                viewUnit.setAnim(anim);
            else if (viewUnit.getAnim() != FeAnim.STAY)
                viewUnit.setAnim(FeAnim.STAY);
        }
    }

    /*
        第一次点击人物, 己方人物时选中效果
     */
    public void hitFirst(){
        if(hitViewUnit == null)
            return;
        //自己阵营?
        if(hitViewUnit.getCamp() == FeCamp.BLUE)
            setAnim(hitViewUnit, FeAnim.ACTIVITY);
        else
            setAnim(hitViewUnit, FeAnim.STAY);
        //置标志
        sectionCallback.onUnitSelect(true);
        sectionCallback.onUnitMove(false);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //透传点击地图
        FeLayoutMap layoutMap = sectionCallback.getLayoutMap();
        if(layoutMap != null)
            layoutMap.hitMap(hitViewUnit.getSite().rect.left + 1, hitViewUnit.getSite().rect.top + 1);
    }

    /*
        第二次点击人物, 显示移动范围
     */
    public void hitSecond(){
        if(hitViewUnit == null)
            return;
        //自己阵营?
        if(hitViewUnit.getCamp() == FeCamp.BLUE) {
            setAnim(hitViewUnit, FeAnim.DOWN);
            //移至居中
            sectionCallback.getLayoutMap().moveCenter(
                hitViewUnit.getSite().point[0], hitViewUnit.getSite().point[1]);
        }
        else
            setAnim(hitViewUnit, FeAnim.STAY);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //置标志
        sectionCallback.onUnitMove(true);
        //更新移动范围
        sectionCallback.getSectionUnit().mov =
            sectionCallback.getAssets().unit.getProfessionAbilityMov(hitViewUnit.getId());
        //显示移动范围
        FeLayoutMark layoutMark = sectionCallback.getLayoutMark();
        if(layoutMark != null)
            layoutMark.markUnit();
    }

    /*
        第三次点击人物, 关闭显示范围(非己方)
     */
    public void hitThird(){
        if(hitViewUnit == null)
            return;
        //自己阵营?
        if(hitViewUnit.getCamp() == FeCamp.BLUE) {
            setAnim(hitViewUnit, FeAnim.ACTIVITY);
            //移动结束,显示unitMenu
            ;
        }
        else
            setAnim(hitViewUnit, FeAnim.STAY);
        //置标志
        sectionCallback.onUnitMove(false);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //关闭移动范围
        FeLayoutMark layoutMark = sectionCallback.getLayoutMark();
        if(layoutMark != null)
            layoutMark.removeAllViews();
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        //不曾选中过,就不存在点击事件
        if(hitViewUnit == null){
            //清标记
            sectionCallback.getSectionUnit().viewUnit = null;
            sectionCallback.onUnitSelect(false);
            sectionCallback.onUnitMove(false);
            return;
        }
        //点击非己
        if(!flag.checkFlag(FeFlagHit.HIT_UNIT)){
            //点击了移动范围
            if(flag.checkFlag(FeFlagHit.HIT_MARK))
                ;
            else {
                //清选中状态
                if (sectionCallback.getSectionUnit().viewUnit != null)
                    sectionCallback.getSectionUnit().viewUnit.setAnim(FeAnim.STAY);
                //请标记
                sectionCallback.getSectionUnit().viewUnit = hitViewUnit = null;
                sectionCallback.onUnitSelect(false);
                sectionCallback.onUnitMove(false);
            }
        }
        //点击人物
        else {
            //一次选中, 目标人物选中 或 和上次选中的不是同一个人
            if (!sectionCallback.onUnitSelect() ||
                hitViewUnit != sectionCallback.getSectionUnit().viewUnit)
                hitFirst();
            //二次选中, 显示移动范围
            else if (!sectionCallback.onUnitMove())
                hitSecond();
            //三次选中, 人物菜单(己方阵营), 关闭移动范围(其它阵营)
            else
                hitThird();
            //刷新动画状态
            refresh();
        }
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
