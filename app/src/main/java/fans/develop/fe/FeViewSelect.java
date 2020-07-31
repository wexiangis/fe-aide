package fans.develop.fe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;

/*
    光标动画
 */
public class FeViewSelect extends FeView {

    private FeSectionCallback sectionCallback;

    //选中框图片
    private Bitmap bitmapSelect;
    //源图片位置和输出位置矩阵
    private Rect rectSrcSelect, rectDistSelect;
    //帧位置
    private int bitmapSelectFrameHeight;
    //画笔
    private Paint paintSelct;

    public FeViewSelect(Context context, FeSectionCallback sectionCallback){
        super(context);
        this.sectionCallback = sectionCallback;
        //
        bitmapSelect = sectionCallback.getAssets().menu.getMapSelect();
        bitmapSelectFrameHeight = bitmapSelect.getWidth();
        rectDistSelect = new Rect(0, 0, 0, 0);
        rectSrcSelect = new Rect(0, 0, bitmapSelect.getWidth(), bitmapSelect.getHeight());
        //
        paintSelct = new Paint();
        paintSelct.setColor(Color.BLUE);
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
    }

    private boolean needRefresh;
    private int heartCount = 0;
    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //移动框图
            rectSrcSelect.left = 0;
            rectSrcSelect.right = bitmapSelect.getWidth();
            //
            needRefresh = true;
            //记数0,1,2,3,
            if(heartCount == 1)
                rectSrcSelect.top = bitmapSelectFrameHeight * 3;
            else if(heartCount == 0 || heartCount == 2)
                rectSrcSelect.top = bitmapSelectFrameHeight * 2;
            else {
                if(rectSrcSelect.top == bitmapSelectFrameHeight)
                    needRefresh = false;
                else
                    rectSrcSelect.top = bitmapSelectFrameHeight;
            }
            if(++heartCount > 6)
                heartCount = 0;
            //
            rectSrcSelect.bottom = rectSrcSelect.top + bitmapSelectFrameHeight;
            //调用一次onDrow
            if(needRefresh)
                FeViewSelect.this.invalidate();
        }
    });

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //移动中不绘制
        if(sectionCallback.onMapMove())
            return;

        //画选中框
        if(sectionCallback.onMapHit() && !sectionCallback.onUnitSelect()) {
            //选中地图位置
            FeInfoSite mapSite = sectionCallback.getSectionMap().selectSite;
            //计算输出位置
            rectDistSelect.left = mapSite.rect.left - mapSite.rect.width()/4;
            rectDistSelect.right = mapSite.rect.right + mapSite.rect.width()/4;
            rectDistSelect.top = mapSite.rect.top - mapSite.rect.height()/4;
            rectDistSelect.bottom = mapSite.rect.bottom + mapSite.rect.height()/4;
            //抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            //画图
//            canvas.drawPath(mapSite.path, paintSelct);
            canvas.drawBitmap(bitmapSelect, rectSrcSelect, rectDistSelect, paintSelct);
        }
        else if(sectionCallback.onUnitSelect() && sectionCallback.getSectionUnit().viewUnit != null){
            //选中人物位置
            FeInfoSite unitSite = sectionCallback.getSectionUnit().viewUnit.site();
            //计算输出位置
            rectDistSelect.left = unitSite.rect.left - unitSite.rect.width()/4;
            rectDistSelect.right = unitSite.rect.right + unitSite.rect.width()/4;
            rectDistSelect.top = unitSite.rect.top - unitSite.rect.height()/2;
            rectDistSelect.bottom = unitSite.rect.bottom;
            //使用大框
            rectSrcSelect.top = 0;
            rectSrcSelect.bottom = bitmapSelectFrameHeight;
            //抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            //画图
//            canvas.drawPath(mapSite.path, paintSelct);
            canvas.drawBitmap(bitmapSelect, rectSrcSelect, rectDistSelect, paintSelct);
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }
}
