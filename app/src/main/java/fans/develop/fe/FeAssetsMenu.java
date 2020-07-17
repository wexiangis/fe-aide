package fans.develop.fe;

import android.graphics.Bitmap;

/*
    /assets/menu 文件夹资源管理器
 */
public class FeAssetsMenu {

    private FeReaderBitmap bitmapReader = new FeReaderBitmap();
    private static final String rootPath = "/menu/map/";

    void FeDataMenu(){
        //初始化一个图片读取工具
        bitmapReader = new FeReaderBitmap();
    }

    public Bitmap getMapCompare(){
        return bitmapReader.load_bitmap(rootPath, "compare.png");
    }
    public Bitmap getMapHeader(){
        return bitmapReader.load_bitmap(rootPath, "header.png");
    }
    public Bitmap getMapInfo(){
        return bitmapReader.load_bitmap(rootPath, "mapinfo.png");
    }
    public Bitmap getMapSelect(){
        return bitmapReader.load_bitmap(rootPath, "select.png");
    }
    public Bitmap getMapTarget(){
        return bitmapReader.load_bitmap(rootPath, "target.png");
    }

}
