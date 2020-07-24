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
    public int xGrid = 10;
    public int yGrid = 10;
    public int pixelPerGrid = 104;
    public int transferGrid = 0;

    //矩阵: grid.txt
    public short[][] grid;

    //方格类型信息: grid_info.txt
    public String[] name;
    public short[] defend;
    public short[] avoid;
    public short[] plus;
    public short[] mov;
    public int[] type;
    public String[] info;

    public FeInfoMap(int section){
        this.section = section;
    }

    /*
        移动力减损计算
     */
    public int movReduce(int xGrid, int yGrid, FeTypeProfession type){
        //超出地图范围
        if(xGrid < 0 || yGrid < 0 || xGrid > this.xGrid || yGrid > this.yGrid)
            return 9999;
        //
        return 0;
    }
}
