package fans.develop.fe;

import android.graphics.Path;
import android.graphics.Rect;

/*
    格子在地图上的定位信息
 */
public class FeInfoSite {
    //方格梯形
    public Path path = new Path();
    //方格
    public Rect rect = new Rect(0,0,0,0);
    //方格横纵格数
    public int xGrid, yGrid;

    public void clean(){
        path.reset();
    }
}