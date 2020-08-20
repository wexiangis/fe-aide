package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/*
    敌军攻击范围、特效范围标记
 */
public class FeViewMark extends FeView {

    private FeSectionCallback sectionCallback;

    //画笔,蓝色、红色、绿色,分别用于画移动范围、攻击范围、特效范围
    private Paint paintB, paintR, paintG;
    //颜色模式
    private int typeMark;
    //标记unit的id
    private int order = 0;
    //标记unit的mov
    private int mov;
    //人物的移动、攻击、特效范围
    private FeInfoSite[] siteMov;
    private FeInfoSite[] siteHit;
    private FeInfoSite[] siteSpecial;
    //id人物的位置,和新的位置进行比较确定是否更新
    private FeInfoSite siteUnit = null;
    //范围计算用
    private FeMark mark;
    //标记绘制范围
    private int[][] markMap;

    /*
        typeMark: 颜色模式
        order: 地图人物唯一order
     */
    public FeViewMark(Context context,
                      int typeMark,
                      int order,
                      int mov,
                      FeSectionCallback sectionCallback) {
        super(context);
        this.typeMark = typeMark;
        this.order = order;
        this.mov = mov;
        this.sectionCallback = sectionCallback;
        //画笔
        paintB = new Paint();
        paintR = new Paint();
        paintG = new Paint();
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
        //初始化标记绘制范围
        cleanMarkMap();
    }

    public void setTypeMark(int typeMark) {
        this.typeMark = typeMark;
    }

    public int getTypeMark() {
        return typeMark;
    }

    public void setMov(int mov) {
        this.mov = mov;
    }

    public int getMov() {
        return mov;
    }

    public int getOrder() {
        return order;
    }

    /*
        根据路径逐个减去消耗的移动力
        path[N][2]: N 为路径点数, [2]为x、y坐标
     */
    public void movReduceByPath(int[][] path) {
        FeInfoMap mapInfo = sectionCallback.getSectionMap().mapInfo;
        int typeProfession = sectionCallback.getSectionUnit().viewUnit.unit.professionType();
        for (int i = 1; i < path.length; i++) {
            //按照路径逐个格子减去移动力消耗
            mov -= mapInfo.movReduce(path[i][0], path[i][1], typeProfession);
            if (mov <= 0)
                break;
        }
        if (mov < 0)
            mov = 0;
    }

    /*
        给出起始点和终止点,解路经
        返回: int[N][2], 即N个(x,y)坐标, N-1 为移动力消耗
     */
    public int[][] getPath(int[] start, int[] end) {
        //起始或结束坐标 不在地图范围
        if (start[0] < 0 || start[0] >= markMap[0].length
                || start[1] < 0 || start[1] >= markMap.length
                || end[0] < 0 || end[0] >= markMap[0].length
                || end[1] < 0 || end[1] >= markMap.length)
            return null;
        //起始或结束坐标 不在移动范围内?
        if (markMap[start[1]][start[0]] != FeTypeMark.BLUE
                || markMap[end[1]][end[0]] != FeTypeMark.BLUE)
            return null;
        //递归获得两点间路径
        int[][] result = new int[mov + 1][2];//mov+1是为了包含 start 和 end 点
        int[][] current = new int[mov + 1][2];
        //清空
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result[0].length; j++)
                result[i][j] = current[i][j] = -1;
        //递归
        getPathLoop(start[0], start[1], mov, 0, markMap, end, current, 0, result);
        //检查结果数组并得到实际长度
        int resultLen = result.length;
        for (int i = 0; i < result.length; i++) {
            if (result[i][0] < 0 || result[i][1] < 0) {
                resultLen = i;
                break;
            }
        }
        //组建返回数组
        if (resultLen < 1)
            return null;
        int[][] retArray = new int[resultLen][2];
        for (int i = 0; i < retArray.length; i++) {
            retArray[i][0] = result[i][0];
            retArray[i][1] = result[i][1];
        }
        return retArray;
    }

    /*
        x, y: 当前递归坐标
        mov: 剩余移动力
        dir: 上一次从哪来, 0/投放点 1/上 2/下 3/左 4/右
        map[height][width]: 画了移动范围的矩阵
        target[2]: 到达目标点
        current[N][2]: 当前路径结果(记录了前面所经过的格子)
        count: current.length 计数, 当前数组写到哪个位置
        result[N][2]: 最终结果

        关于"[N][2]": N表示最多N个点, [2]表示xy坐标
     */
    private void getPathLoop(int x, int y, int mov, int dir, int[][] map, int[] target, int[][] current, int count, int[][] result) {
        //当前点不在移动范围内?
        if (x < 0 || x >= map[0].length || y < 0 || y >= map.length || map[y][x] != FeTypeMark.BLUE)
            return;
        //数组长度剩余不足
        if (count >= current.length)
            return;
        //当前点已走过
        if (_checkPoint(x, y, current, count))
            return;
        //标记当前格
        current[count][0] = x;
        current[count][1] = y;
        //这是目标坐标?
        if (x == target[0] && y == target[1]) {
            //检查结果,并结束递归
            _loopEnd(current, count, result);
            return;
        }
        //移动力削减(投放点不算入移动力削减)
        if(dir != 0)
            mov -= mark.movReduce(x, y);
        //剩余移动力不足
        if(mov <= 0)
            return;
        //引入乱数,打乱每次递归上下左右的顺序,以实现每次行走路径不同
        int[] order = new int[4];
        FeRandom.getArray2(order, 1);//不重复的把[1,4]中的整数填到数组中
        //递归上下左右的点
        for(int i = 0; i < order.length; i++){
            if(dir != order[i]){
                if(order[i] == 2)
                    getPathLoop(x, y - 1, mov, 1, map, target, current, count + 1, result);
                else if(order[i] == 1)
                    getPathLoop(x, y + 1, mov, 2, map, target, current, count + 1, result);
                else if(order[i] == 4)
                    getPathLoop(x - 1, y, mov, 3, map, target, current, count + 1, result);
                else if(order[i] == 3)
                    getPathLoop(x + 1, y, mov, 4, map, target, current, count + 1, result);
            }
        }
        // if (dir != 2)
        //     getPathLoop(x, y - 1, mov, 1, map, target, current, count + 1, result);
        // if (dir != 1)
        //     getPathLoop(x, y + 1, mov, 2, map, target, current, count + 1, result);
        // if (dir != 4)
        //     getPathLoop(x - 1, y, mov, 3, map, target, current, count + 1, result);
        // if (dir != 3)
        //     getPathLoop(x + 1, y, mov, 4, map, target, current, count + 1, result);
    }

    /*
        检查点是否已递归过了
        count: 检查 current 范围 [0, count)
     */
    private Boolean _checkPoint(int x, int y, int[][] current, int count) {
        for (int c = 0; c < count; c++) {
            if (current[c][0] == x && current[c][1] == y)
                return true;
        }
        return false;
    }

    /*
        递归循环结束,结果检查
        count: current最后一个点的位置
     */
    private void _loopEnd(int[][] current, int count, int[][] result) {
        //获得 result 结束位置
        int countResult = result.length - 1;
        //result 数组已被写过
        if (result[0][0] >= 0 && result[0][1] >= 0) {
            //找 countResult
            for (int i = 0; i < result.length; i++) {
                //没有写过的位置
                if (result[i][0] < 0 && result[i][1] < 0)
                    break;
                countResult = i;
            }
        }
        //result现存的路径更短?
        if (countResult < count)
            return;
        //否则拷贝当前
        int c = 0;
        for (; c <= count; c++) {
            result[c][0] = current[c][0];
            result[c][1] = current[c][1];
        }
        //清空后面
        for (; c < result.length; c++) {
            result[c][0] = -1;
            result[c][1] = -1;
        }
    }

    /*
        数组新建并拷贝
     */
    private int[][] _arrayCopy(int[][] src) {
        int[][] ret = new int[src.length][src[0].length];
        for (int y = 0; y < ret.length; y++)
            for (int x = 0; x < ret[0].length; x++)
                ret[y][x] = src[y][x];
        return ret;
    }

    public FeInfoSite checkHit(int xGrid, int yGrid) {
        //非移动范围
        if (siteMov == null
                || xGrid < 0 || xGrid >= markMap[0].length
                || yGrid < 0 || yGrid >= markMap.length)
            return null;
        //该格子在移动范围内?
        if (markMap[yGrid][xGrid] == FeTypeMark.BLUE)
            return sectionCallback.getSectionMap().getRectByGrid(xGrid, yGrid);
        return null;
    }

    //动画心跳回调(网格光条变动效果)
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask() {
        public void run(int count) {
            FeViewMark.this.invalidate();
        }
    });

    /*
        擦除自己画过的格子
     */
    private void cleanMarkMap() {
        //第一次初始化
        if(markMap == null){
            FeInfoMap mapInfo = sectionCallback.getSectionMap().mapInfo;
            markMap = new int[mapInfo.height][mapInfo.width];
        }
        //清数组
        for (int x = 0; x < markMap[0].length; x++)
            for (int y = 0; y < markMap.length; y++)
                markMap[y][x] = -1;
    }

    //绘图回调
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //没有unit图层?
        if (sectionCallback.getLayoutUnit() == null)
            return;

        //人物正在移动
        if (sectionCallback.onUnitMoveing())
            return;

        //获得unit位置
        FeInfoSite siteUnit = sectionCallback.getLayoutUnit().getUnitSite(order);
        //order人物没有绘制?
        if (siteUnit == null)
            return;

        //第一次初始化 或者 人物位置变动了, 更新range
        if (this.siteUnit == null
                || this.siteUnit.xGrid != siteUnit.xGrid
                || this.siteUnit.yGrid != siteUnit.yGrid) {
            //更新位置
            if (this.siteUnit == null)
                this.siteUnit = new FeInfoSite();
            sectionCallback.getSectionMap().getRectByGrid(siteUnit.xGrid, siteUnit.yGrid, this.siteUnit);
            //计算范围
            mark = new FeMark(
                    this.siteUnit.xGrid, this.siteUnit.yGrid, mov,
                    sectionCallback.getSectionMap().mapInfo,
                    sectionCallback.getSectionMap().unitMap,
                    sectionCallback.getLayoutUnit().getUnit(order));
        }

        //获取位置数组
        siteMov = mark.rangeMov.getGridInfo(sectionCallback.getSectionMap());
        siteHit = mark.rangeHit.getGridInfo(sectionCallback.getSectionMap());
        siteSpecial = mark.rangeSpecial.getGridInfo(sectionCallback.getSectionMap());

        //按颜色取渲染
        paintB.setShader(sectionCallback.getSectionShader().getShaderB());
        paintR.setShader(sectionCallback.getSectionShader().getShaderR());
        paintG.setShader(sectionCallback.getSectionShader().getShaderG());

        //擦除自己画过的格子(每一层)
        cleanMarkMap();

        //遍历 siteMov 数组,画格子
        for (int i = 0; i < siteMov.length; i++) {
            canvas.drawPath(siteMov[i].path, paintB);
            //标记已画过
            markMap[siteMov[i].yGrid][siteMov[i].xGrid] = FeTypeMark.BLUE;
        }

        //遍历 siteHit 数组,画格子
        if (typeMark == FeTypeMark.RED) {
            if (siteHit != null) {
                for (int i = 0; i < siteHit.length; i++) {
                    //这个点刚才没有画过移动范围?
                    if (markMap[siteHit[i].yGrid][siteHit[i].xGrid] != FeTypeMark.BLUE) {
                        //标记已画过
                        markMap[siteHit[i].yGrid][siteHit[i].xGrid] = FeTypeMark.RED;
                        canvas.drawPath(siteHit[i].path, paintR);
                    }
                }
            }
        }
        //遍历 siteSpecial 数组,画格子
        else {
            if (siteSpecial != null) {
                for (int i = 0; i < siteSpecial.length; i++) {
                    //这个点刚才没有画过移动范围?
                    if (markMap[siteSpecial[i].yGrid][siteSpecial[i].xGrid] != FeTypeMark.BLUE) {
                        //标记已画过
                        markMap[siteSpecial[i].yGrid][siteSpecial[i].xGrid] = FeTypeMark.GREEN;
                        canvas.drawPath(siteSpecial[i].path, paintR);
                    }
                }
            }
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        //擦除自己画过的格子(每一层)
        cleanMarkMap();
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }
}
