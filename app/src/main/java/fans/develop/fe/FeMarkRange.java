package fans.develop.fe;

/*
    移动范围计算和标记
 */
public class FeMarkRange {

    //中心点
    public int xGrid, yGrid;
    //矩阵左上角
    public int xGridStart, yGridStart;
    //输出矩阵
    public FeTypeMark[][] range;

    public FeMarkRange(int xGrid, int yGrid, FeInfoMap mapInfo, int mov, FeTypeProfession type){
        //最大宽高
        int max_wh = mov*2 + 1;
        //开拓一个足够容纳范围的矩形
        range = new FeTypeMark[max_wh][max_wh];
        //递归画图
        int[][] movMap = new int[max_wh][max_wh];
    }

    /*
        检查 movMap 在坐标 (xGrid,yGrid) 剩余移动力并填写, 再递归向上下左右格调用loop();
        出现移动力消耗完毕时, 结束当前递归;
        出现重复填写同一格子时, 以大数值的为准, 并结束当前递归.
     */
    private int loop(int xGrid, int yGrid, FeInfoMap mapInfo, int mov, FeTypeProfession type, int[][] movMap){
        return 0;
    }
}
