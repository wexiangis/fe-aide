package fans.develop.fe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;

/*
    地图上的人物头像等信息框
 */
public class FeViewUnitInfo extends FeView {

    private FeSectionCallback sectionCallback;

    //头像背景框图片源位置和输出位置
    private Rect rectSrcHeadBg, rectDistHeadBg;
    //头像图片,头像背景框
    private Bitmap bitmapHead, bitmapHeadBg;
    //头像背景框,头像,参数文字画笔
    private Paint paintHeadBg, paintHead, paintParam;
    //像素比例
    private float pixelPowHead;
    //是否绘制了图片?没有则不参与 checkHit()
    private boolean drawHead = false;

    public FeViewUnitInfo(Context context, FeSectionCallback sectionCallback){
        super(context);
        this.sectionCallback = sectionCallback;
        //
        bitmapHeadBg = sectionCallback.getAssets().menu.getMapHeader();
        //
        pixelPowHead = sectionCallback.getSectionMap().yGridPixel*2/bitmapHeadBg.getHeight();
        //
        rectSrcHeadBg = new Rect(0, 0, bitmapHeadBg.getWidth(), bitmapHeadBg.getHeight());
        rectDistHeadBg = new Rect(
                (int)(sectionCallback.getSectionMap().xGridPixel/4),
                (int)(sectionCallback.getSectionMap().yGridPixel/4),
                (int)(sectionCallback.getSectionMap().xGridPixel/4 + bitmapHeadBg.getWidth()*pixelPowHead),
                (int)(sectionCallback.getSectionMap().yGridPixel/4 + bitmapHeadBg.getHeight()*pixelPowHead));
        //
        paintHeadBg = new Paint();
        paintHeadBg.setColor(0xE00000FF);//半透明
        //
        paintHead = new Paint();
        paintHead.setColor(0xE00000FF);//半透明
        //
        paintParam = new Paint();
        paintParam.setTextSize(rectDistHeadBg.height()/8);
//        paintParam.setStyle(Paint.Style.FILL_AND_STROKE);
//        paintParam.setStrokeWidth(2);
//        paintParam.setAntiAlias(true);
//        paintParam.setStrokeCap(Paint.Cap.ROUND);
        paintParam.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public boolean checkHit(float x, float y){
        if(drawHead && rectDistHeadBg.contains((int)x, (int)y))
            return true;
        return false;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //移动中不绘制
        if(sectionCallback.onMapMove()){
            drawHead = false;
            return;
        }

        //选中人物位置
        if(sectionCallback.getSectionUnit().viewUnit == null)
            return;
        FeInfoSite unitSite = sectionCallback.getSectionUnit().viewUnit.site();

        //图像位置自动调整
        if(unitSite.rect.right > sectionCallback.getSectionMap().screenWidth/2){ //放到左边
            rectDistHeadBg.left = (int)(sectionCallback.getSectionMap().xGridPixel/4);
            rectDistHeadBg.right = (int)(sectionCallback.getSectionMap().xGridPixel/4 + bitmapHeadBg.getWidth()*pixelPowHead);
        }else{ //放到右边
            rectDistHeadBg.left = (int)(sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel/4 - bitmapHeadBg.getWidth()*pixelPowHead);
            rectDistHeadBg.right = (int)(sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel/4);
        }

        //画人物头像
        if(!sectionCallback.onMapHit() ||
            unitSite.rect.left > sectionCallback.getSectionMap().screenWidth ||
            unitSite.rect.right < 0 ||
            unitSite.rect.top > sectionCallback.getSectionMap().screenHeight ||
            unitSite.rect.bottom < 0){
            drawHead = false;
        }else {
            drawHead = true;
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//抗锯齿
            canvas.drawBitmap(bitmapHeadBg, rectSrcHeadBg, rectDistHeadBg, paintHeadBg);
            //填信息
            canvas.drawText(
                sectionCallback.getAssets().unit.getName(0),
                rectDistHeadBg.left + rectDistHeadBg.width()/4,
                rectDistHeadBg.top + rectDistHeadBg.height()/4,
                paintParam);
            canvas.drawText(
                    sectionCallback.getAssets().unit.getSummary(0),
                    rectDistHeadBg.left + rectDistHeadBg.width()/4,
                    rectDistHeadBg.top + rectDistHeadBg.height()/2,
                    paintParam);
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        ;
    }
}
