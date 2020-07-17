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
    //
    private Rect rectSrcSelect, rectDistSelect;
    //
    private int bitmapSelectFrameHeight;
    //
    private Paint paintSelct;

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }

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
        if(sectionCallback.onMapHit() &&
                !sectionCallback.onUnitSelect()) {
            //计算输出位置
            rectDistSelect.left = sectionCallback.getSectionMap().selectSite.rect.left - sectionCallback.getSectionMap().selectSite.rect.width()/4;
            rectDistSelect.right = sectionCallback.getSectionMap().selectSite.rect.right + sectionCallback.getSectionMap().selectSite.rect.width()/4;
            rectDistSelect.top = sectionCallback.getSectionMap().selectSite.rect.top - sectionCallback.getSectionMap().selectSite.rect.height()/4;
            rectDistSelect.bottom = sectionCallback.getSectionMap().selectSite.rect.bottom + sectionCallback.getSectionMap().selectSite.rect.height()/4;
            //抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            //画图
//            canvas.drawPath(sectionCallback.getSectionMap().selectSite.path, paintSelct);
            canvas.drawBitmap(bitmapSelect, rectSrcSelect, rectDistSelect, paintSelct);
        }
        else if(sectionCallback.onUnitSelect()){
            //计算输出位置
            rectDistSelect.left = sectionCallback.getSectionUnit().selectSite.rect.left - sectionCallback.getSectionUnit().selectSite.rect.width()/4;
            rectDistSelect.right = sectionCallback.getSectionUnit().selectSite.rect.right + sectionCallback.getSectionUnit().selectSite.rect.width()/4;
            rectDistSelect.top = sectionCallback.getSectionUnit().selectSite.rect.top - sectionCallback.getSectionUnit().selectSite.rect.height()/2;
            rectDistSelect.bottom = sectionCallback.getSectionUnit().selectSite.rect.bottom;
            //使用大框
            rectSrcSelect.top = 0;
            rectSrcSelect.bottom = bitmapSelectFrameHeight;
            //抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            //画图
//            canvas.drawPath(sectionCallback.getSectionMap().selectSite.path, paintSelct);
            canvas.drawBitmap(bitmapSelect, rectSrcSelect, rectDistSelect, paintSelct);
        }
    }
}
