package fans.develop.fe;

import android.graphics.Bitmap;

/*
    bitmap调色板
 */
public class FePallet {

    //camp: 0/原色 1/红色 2/绿色 3/灰色 4/橙色 5/紫色 6/不蓝不绿
    public static Bitmap replace(Bitmap oldBitmap, FeTypeCamp camp){
        if(camp == FeTypeCamp.BLUE)   //使用原色
            return oldBitmap;
        //转换格式
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int r, g, b, color, tcolor, tmp;

        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                color = mBitmap.getPixel(j, i);
                tcolor = color&0xff000000;
                r = (color&0x00ff0000)>>16;
                g = (color&0x0000ff00)>>8;
                b = color&0x000000ff;
                if(b > g && b > r) {
                    if (camp == FeTypeCamp.RED) {          //蓝色 换 红色
                        tcolor |= (int) (b * 1.0) << 16;
                        tcolor |= (int) (g * 0.5) << 8;
                        tcolor |= (int) (r * 0.5) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }else if (camp == FeTypeCamp.GREEN) {    //蓝色 换 绿色
                        tcolor |= (int) (r * 0.5) << 16;
                        tcolor |= (int) (b * 0.9) << 8;
                        tcolor |= (int) (g * 0.5) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }else if (camp == FeTypeCamp.GRAY) {    //蓝色 换 黑色
                        tmp = r + g + b;
                        r = g = b = tmp/4;
                        tcolor |= (int) (r * 1.0) << 16;
                        tcolor |= (int) (g * 1.0) << 8;
                        tcolor |= (int) (b * 1.0) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }else if (camp == FeTypeCamp.ORANGE) {    //蓝色 换 橙色
                        tcolor |= (int) (b * 1.0) << 16;
                        tcolor |= (int) (g * 1.0) << 8;
                        tcolor |= (int) (r * 0.5) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }else if (camp == FeTypeCamp.PURPLE) {    //蓝色 换 红紫色
                        tcolor |= (int) (b * 0.8) << 16;
                        tcolor |= (int) (r * 0.5) << 8;
                        tcolor |= (int) (g * 1.0) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }else if (camp == FeTypeCamp.BLUE_GREEN) {    //蓝色 换 不蓝不绿
                        tcolor |= (int) (r * 0.8) << 16;
                        tcolor |= (int) (b * 0.8) << 8;
                        tcolor |= (int) (b * 0.8) << 0;
                        mBitmap.setPixel(j, i, tcolor);
                    }
                }
            }
        }
        return mBitmap;
    }
}
