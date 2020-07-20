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
	private FeViewUnit hitSelectView = null;

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
                hitSelectView = viewUnit;
                return true;
            }
        }
        return false;
    }

    /*
        人员增删
     */
    public void addUnit(int id, int y, int x, int camp){
        addView(new FeViewUnit(context, id, x, y, 0, camp, sectionCallback));
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
    public void setCamp(int id, int camp){
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
    public void setAnim(int id, int animMode){
        ;
    }

    /*
        清所有人物选中状态
     */
    public void setAnim(FeViewUnit target, int animMode){
        FeViewUnit viewUnit;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewUnit = (FeViewUnit)getChildAt(i);
            if(viewUnit == target)
                viewUnit.setAnimMode(animMode);
            else if (viewUnit.getAnimMode() != 0)
                viewUnit.setAnimMode(0);
        }
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, Boolean hitThis, int hitType){
        if(hitSelectView == null){
            sectionCallback.onUnitSelect(false);
            sectionCallback.onUnitMove(false);
            return;
        }
        //点击非己,清选中状态
        if(!hitThis){
			if(sectionCallback.getSectionUnit().selectView != null)
				sectionCallback.getSectionUnit().selectView.setAnimMode(0);
            sectionCallback.onUnitSelect(false);
            sectionCallback.onUnitMove(false);
            return;
        }
        //目标人物选中 或 和上次选中的不是同一个人
        if(!sectionCallback.onUnitSelect() ||
			hitSelectView != sectionCallback.getSectionUnit().selectView){
            setAnim(hitSelectView, 1);
            sectionCallback.onUnitSelect(true);
            sectionCallback.onUnitMove(false);
        }
        //二次选中
        else{
            setAnim(hitSelectView, 3);
            sectionCallback.onUnitMove(true);
        }
        //缓存当前选中
        sectionCallback.getSectionUnit().selectView = hitSelectView;
        //输入坐标求格子位置,更新人物选中点信息
        FeInfoGrid site = sectionCallback.getSectionUnit().selectView.getSite();
        sectionCallback.getSectionMap().getRectByLocation(x, y, site);
        //选中人物太过靠近边界,挪动地图
        if(!sectionCallback.getSectionMap().srcGridCenter.contains(site.point[0], site.point[1])){
            //移至居中
            // sectionCallback.getLayoutMap().moveCenter(site.point[0], site.point[1]);
            //移至包含
            sectionCallback.getLayoutMap().moveInclude(site.point[0], site.point[1]);
        }
        //刷新动画状态
        refresh();
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
