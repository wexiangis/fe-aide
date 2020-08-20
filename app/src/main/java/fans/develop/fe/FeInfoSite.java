package fans.develop.fe;

import android.graphics.Path;
import android.graphics.Rect;

/*
    格子在地图上的定位信息
 */
public class FeInfoSite {
    //方格梯形路径(用于画攻击范围)
    public Path path = new Path();
    //方格矩形(用于人物站位)
    public Rect rect = new Rect(0, 0, 0, 0);
    //方格横纵所在格数
    public int xGrid, yGrid;
}
