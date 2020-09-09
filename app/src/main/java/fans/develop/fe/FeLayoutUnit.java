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
    //viewUnit数组,方便通过order快速定位view
    private FeChain2<FeViewUnit> array = new FeChain2<FeViewUnit>(-1, null);
    //本次绘制子view顺序数组,长度为[子view总数],值为yyyxxxooo,其中y、x为坐标,o为调换过的子view序号
    private int[] drawOrder;

    public FeLayoutUnit(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
        //启用手动控制绘图顺序,即启用重写 getChildDrawingOrder 方法
        setChildrenDrawingOrderEnabled(true);
    }

    /* ---------- function ---------- */

    public void refresh() {
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    public boolean checkHit(float x, float y) {
        FeViewUnit viewUnit;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewUnit = (FeViewUnit) getChildAt(i);
            if (viewUnit.checkHit(x, y)) {
                hitViewUnit = viewUnit;
                return true;
            }
        }
        return false;
    }

    /*
        根据order找人物
     */
    public FeViewUnit getViewUnit(int order) {
        return array.find(order);
    }

    /*
        根据order找人物位置
     */
    public FeInfoSite getUnitSite(int order) {
        FeViewUnit viewUnit = getViewUnit(order);
        if (viewUnit == null)
            return null;
        return viewUnit.site();
    }

    /*
        根据order找人物参数
     */
    public FeUnit getUnit(int order) {
        FeViewUnit viewUnit = getViewUnit(order);
        if (viewUnit == null)
            return null;
        return viewUnit.unit;
    }

    /*
        人员增删
     */
    public void addUnit(int order, int y, int x) {
        FeViewUnit viewUnit = new FeViewUnit(context, order, sectionCallback);
        array.add(order, viewUnit);
        addView(viewUnit);
    }

    public void removeUnit(int order) {
        _removeView(this, array.find(order));
        array.remove(order);
    }

    /*
        选中人物
     */
    public void unitSelect(int order) {
        ;
    }

    /*
        人物按轨迹行走
        path: 格子位置(x，y),组成的路径,注意两个节点之间只能x或y一个值变动,
            例如: [0,0] -> [1,1] 是错误的, 要改为 [0,0] -> [0,1] -> [1,1]
        sectionCallback: 移动结束后要做什么(一般把人物 setStay())
     */
    public void unitWalk(int order, Path path, Runnable sectionCallback) {
        ;
    }

    /*
        释放选中
     */
    public void unitRelease(int order) {
        ;
    }

    /*
        人员阵营
     */
    public void setCamp(int order, int camp) {
        ;
    }

    /*
        人员可行动
     */
    public void setActivity(int order) {
        ;
    }

    /*
        人员待机
     */
    public void setStay(int order) {
        ;
    }

    /*
        人员异常状态
     */
    public void setError(int order, int errorType) {
        ;
    }

    /*
        人员动画
        animMode: 0/待机 1/选中 2,3,4,5/上,下,左,右
     */
    public void setAnim(int order, FeTypeAnim anim) {
        ;
    }

    /*
        清所有人物选中状态
     */
    public void setAnim(FeViewUnit target, FeTypeAnim anim) {
        FeViewUnit viewUnit;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewUnit = (FeViewUnit) getChildAt(i);
            if (viewUnit == target)
                viewUnit.anim(anim);
            else if (viewUnit.anim() != FeTypeAnim.STAY)
                viewUnit.anim(FeTypeAnim.STAY);
        }
    }

    /*
        第一次点击人物, 己方人物时选中效果
     */
    public void hitFirst() {
        if (hitViewUnit == null)
            return;
        //自己阵营?
        if (hitViewUnit.camp() == FeTypeCamp.BLUE)
            setAnim(hitViewUnit, FeTypeAnim.ACTIVITY);
        else
            setAnim(hitViewUnit, FeTypeAnim.STAY);
        //置标志
        sectionCallback.onUnitSelect(true);
        sectionCallback.onUnitMove(false);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //透传点击地图
        FeLayoutMap layoutMap = sectionCallback.getLayoutMap();
        if (layoutMap != null)
            layoutMap.hitMap(hitViewUnit.site().rect.left + 1, hitViewUnit.site().rect.top + 1);
    }

    /*
        第二次点击人物, 显示移动范围
     */
    public void hitSecond() {
        if (hitViewUnit == null)
            return;
        //自己阵营?
        if (hitViewUnit.camp() == FeTypeCamp.BLUE) {
            setAnim(hitViewUnit, FeTypeAnim.DOWN);
            //移至居中
            sectionCallback.getLayoutMap().moveCenter(
                    hitViewUnit.site().xGrid, hitViewUnit.site().yGrid);
        } else
            setAnim(hitViewUnit, FeTypeAnim.STAY);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //置标志
        sectionCallback.onUnitMove(true);
        //显示移动范围
        FeLayoutMark layoutMark = sectionCallback.getLayoutMark();
        if (layoutMark != null)
            layoutMark.markUnit(hitViewUnit.order(), hitViewUnit.unit.mov(), FeTypeMark.RED);
    }

    /*
        第三次点击人物, 关闭显示范围(非己方)
     */
    public void hitThird() {
        if (hitViewUnit == null)
            return;
        //自己阵营?
        if (hitViewUnit.camp() == FeTypeCamp.BLUE) {
            setAnim(hitViewUnit, FeTypeAnim.ACTIVITY);
            //移动结束,显示unitMenu
            ;
        } else
            setAnim(hitViewUnit, FeTypeAnim.STAY);
        //置标志
        sectionCallback.onUnitMove(false);
        //缓存当前选中
        sectionCallback.getSectionUnit().viewUnit = hitViewUnit;
        //关闭移动范围
        FeLayoutMark layoutMark = sectionCallback.getLayoutMark();
        if (layoutMark != null)
            layoutMark._removeViewAll(layoutMark);
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag) {
        //不曾选中过,就不存在点击事件
        if (hitViewUnit == null) {
            //清标记
            sectionCallback.getSectionUnit().viewUnit = null;
            sectionCallback.onUnitSelect(false);
            sectionCallback.onUnitMove(false);
            return;
        }
        //点击非己
        if (!flag.checkFlag(FeFlagHit.HIT_UNIT)) {
            //点击了移动范围
            if (flag.checkFlag(FeFlagHit.HIT_MARK))
                ;
            else {
                //清选中状态
                if (sectionCallback.getSectionUnit().viewUnit != null)
                    sectionCallback.getSectionUnit().viewUnit.anim(FeTypeAnim.STAY);
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

    /*
        手动控制人物绘制顺序,以实现前排人物不会被后排遮挡的效果
        total: 当前ViewGroup的子view总数
        count: 系统会依次传入0,1,2...,(total-1)总调用total次
        返回: 本次绘制的子view序号,系统默认返回count
     */
    @Override
    protected int getChildDrawingOrder(int total, int count){
        //新的一轮绘制已开始,更新顺序数组
        if(count == 0)
            refreshDrawOrder();
        //取yyyxxxooo中的序号o
        return drawOrder[count] % 1000;
    }
    
    /*
        更新绘制顺序数组drawOrder
     */
    private void refreshDrawOrder(){
        int len = getChildCount();
        if(drawOrder == null || drawOrder.length < len)
            drawOrder = new int[len];
        //排序
        FeChain2<FeViewUnit> a = array.next;//遍历链表,链表头不参与
        int n = 0;//遍历到第几个了
        while(a != null){
            //位置数值,越小的排在前面
            int yyyxxxooo = a.data.unit.y() * 1000000;
            //当前人物为选中状态,则他在同一行人物中最突出
            if(a.data.anim() != FeTypeAnim.STAY)
                yyyxxxooo += 999 * 1000;
            else
                yyyxxxooo += a.data.unit.x() * 1000;
            yyyxxxooo += n;
            for(int i = 0; i < n; i++){
                if(drawOrder[i] > yyyxxxooo){
                    //交换
                    int t = drawOrder[i];
                    drawOrder[i] = yyyxxxooo;
                    yyyxxxooo = t;
                }
            }
            //把最大的放到最后面
            drawOrder[n] = yyyxxxooo;
            //取下一个
            a = a.next;
            n += 1;
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
