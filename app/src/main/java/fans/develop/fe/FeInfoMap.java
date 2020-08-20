package fans.develop.fe;

import android.graphics.Bitmap;

/*
    地图基本信息
 */
public class FeInfoMap {

    public int section;

    //图片: map.jpg
    public Bitmap bitmap = null;

    //基本参数: size.txt
    public int width = 10, height = 10;//横纵向格子数
    public int pixelPerGrid = 104;//每个格子推荐的宽高像素
    public int transferGrid = 0;//梯形变换缩进格子数(0表示不变换)

    //矩阵: grid.txt
    public short[][] grid;//地图矩阵,记录了每个格子的地形序号

    //方格类型信息: grid_info.txt
    //根据上面矩阵中的序号,到下面数组中取值,可得到地形的各项参数
    public String[] name;
    public short[] defend;
    public short[] avoid;
    public short[] plus;
    public short[] mov;
    public int[] type;
    public int[] typeFirst;
    public String[] info;

    /*
        光初始化是得不到地图数据的
        要配合使用 FeReaderMap 的 getFeMapInfo 方法
     */
    public FeInfoMap(int section) {
        this.section = section;
    }

    /*
        更具坐标获取各种地图信息
     */
    public String name(int xGrid, int yGrid) {
        return name[grid[yGrid][xGrid]];
    }

    public short defend(int xGrid, int yGrid) {
        return defend[grid[yGrid][xGrid]];
    }

    public short avoid(int xGrid, int yGrid) {
        return avoid[grid[yGrid][xGrid]];
    }

    public short plus(int xGrid, int yGrid) {
        return plus[grid[yGrid][xGrid]];
    }

    public short mov(int xGrid, int yGrid) {
        return mov[grid[yGrid][xGrid]];
    }

    public int type(int xGrid, int yGrid) {
        return type[grid[yGrid][xGrid]];
    }

    public int typeFirst(int xGrid, int yGrid) {
        return typeFirst[grid[yGrid][xGrid]];
    }

    public String info(int xGrid, int yGrid) {
        return info[grid[yGrid][xGrid]];
    }

    /*
        移动力减损计算
     */
    public int movReduce(int xGrid, int yGrid, int typeProfession) {
        //超出地图范围
        if (xGrid < 0 || yGrid < 0 || xGrid >= width || yGrid >= height)
            return 9999;
        //地图本身的移动力减损量
        int reduce = mov(xGrid, yGrid);
        //是否地图禁进入typeProfession?
        if ((type(xGrid, yGrid) & (0x00000001 << typeProfession)) != 0)
            return 9999;
        //是否优势通过?
        if ((typeFirst(xGrid, yGrid) & (0x00000001 << typeProfession)) != 0) {
            //这里预留可能出现负值的情况
            if (reduce > 1)
                reduce = 1;
        }
        //否则返回mov削减量
        return reduce;
    }
}
