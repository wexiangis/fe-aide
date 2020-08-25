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
    //头像图片源位置和输出位置
    private Rect rectSrcHead, rectDistHead;
    //头像图片,头像背景框
    private Bitmap bitmapHead, bitmapHeadBg;
    //头像背景框,头像,参数文字画笔
    private Paint paintHeadBg, paintHead, paintParam;
    //素材边框在屏幕中的尺寸(约占图片高度的2/38)
    private int edge;
    //背景框参数
    private int width, height, sizeHead;
    //像素比例
    private float pixelPowHead;
    //是否绘制了图片?没有则不参与 checkHit()
    private boolean drawHead = false;
    //缓存当前人物,若变动则重新加载参数和头像
    private FeViewUnit unitView = null;

    public FeViewUnitInfo(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;
        //图片资源
        bitmapHeadBg = sectionCallback.getAssets().menu.getMapHeader();
        bitmapHead = null;//等选中人物时再初始化
        //屏幕像素和图片像素比例
        pixelPowHead = sectionCallback.getSectionMap().yGridPixel * 2 / bitmapHeadBg.getHeight();
        //素材边框在屏幕中的尺寸(约占图片高度的2/38)
        edge = (int)(bitmapHeadBg.getHeight() * pixelPowHead * 2 / 38);
        //基本参数
        width = (int)(bitmapHeadBg.getWidth() * pixelPowHead);
        height = (int)(bitmapHeadBg.getHeight() * pixelPowHead);
        //计算图片源位置和输出位置
        rectSrcHeadBg = new Rect(0, 0, bitmapHeadBg.getWidth(), bitmapHeadBg.getHeight());
        rectDistHeadBg = new Rect(
                (int) (sectionCallback.getSectionMap().xGridPixel / 4),
                (int) (sectionCallback.getSectionMap().yGridPixel / 4),
                (int) (sectionCallback.getSectionMap().xGridPixel / 4 + width),
                (int) (sectionCallback.getSectionMap().yGridPixel / 4 + height));
        sizeHead = rectDistHeadBg.height() - edge * 2;
        rectSrcHead = new Rect(0, 0, 1, 1);//等具体选中人物了才知道
        rectDistHead = new Rect(//头像仅挨边框左上角,填充出一个方形
                rectDistHeadBg.left + edge,
                rectDistHeadBg.top + edge,
                rectDistHeadBg.left + edge + sizeHead,
                rectDistHeadBg.top + edge + sizeHead);
        //
        paintHeadBg = new Paint();
        paintHeadBg.setColor(0xE0FF0000);//半透明
        //
        paintHead = new Paint();
        paintHead.setColor(0xE00000FF);//半透明
        //
        paintParam = new Paint();
        paintParam.setTextSize(rectDistHeadBg.height() / 8);
//        paintParam.setStyle(Paint.Style.FILL_AND_STROKE);
//        paintParam.setStrokeWidth(2);
//        paintParam.setAntiAlias(true);
//        paintParam.setStrokeCap(Paint.Cap.ROUND);
        paintParam.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public boolean checkHit(float x, float y) {
        if (drawHead && rectDistHeadBg.contains((int) x, (int) y))
            return true;
        return false;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //移动中不绘制
        if (sectionCallback.onMapMove()) {
            drawHead = false;
            return;
        }

        //选中人物位置
        if (sectionCallback.getSectionUnit().viewUnit == null)
            return;
        FeViewUnit unitView = sectionCallback.getSectionUnit().viewUnit;
        FeInfoSite unitSite = unitView.site();

        //图像位置自动调整
        if (unitSite.rect.right > sectionCallback.getSectionMap().screenWidth / 2) { //放到左边
            rectDistHeadBg.left = (int) (sectionCallback.getSectionMap().xGridPixel / 4);
            rectDistHeadBg.right = (int) (sectionCallback.getSectionMap().xGridPixel / 4 + bitmapHeadBg.getWidth() * pixelPowHead);
        } else { //放到右边
            rectDistHeadBg.left = (int) (sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel / 4 - bitmapHeadBg.getWidth() * pixelPowHead);
            rectDistHeadBg.right = (int) (sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel / 4);
        }
        rectDistHeadBg.right = rectDistHeadBg.left + width;
        rectDistHeadBg.bottom = rectDistHeadBg.top + height;
        rectDistHead.left = rectDistHeadBg.left + edge;
        rectDistHead.top = rectDistHeadBg.top + edge;
        rectDistHead.right = rectDistHead.left + sizeHead;
        rectDistHead.bottom = rectDistHead.top + sizeHead;

        //非地图人物第一次选中状态,不画人物信息框
        if (!sectionCallback.onMapHit() ||
                unitSite.rect.left > sectionCallback.getSectionMap().screenWidth ||
                unitSite.rect.right < 0 ||
                unitSite.rect.top > sectionCallback.getSectionMap().screenHeight ||
                unitSite.rect.bottom < 0) {
            drawHead = false;
        }
        //画人物头像
        else {
            drawHead = true;
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//抗锯齿
            //画背景框
            //canvas.drawRect(rectDistHeadBg, paintHeadBg);
            canvas.drawBitmap(bitmapHeadBg, rectSrcHeadBg, rectDistHeadBg, paintHeadBg);
            //人物变动或第一次初始化
            if(unitView != this.unitView || bitmapHead == null){
                //更新人物
                this.unitView = unitView;
                //重新加载头像
                bitmapHead = unitView.unit.getHead();
                //确定头像源位置
                if(bitmapHead.getWidth() == bitmapHead.getHeight()){//杂兵的等比例头像,切掉底部一块使用
                    rectSrcHead.right = bitmapHead.getWidth();
                    rectSrcHead.bottom = 73;
                }
                else{//特殊人物的头像,需抠图
                    rectSrcHead.right = 95;
                    rectSrcHead.bottom = 79;
                }
                rectDistHead.right = rectDistHead.left + rectSrcHead.height() * rectSrcHead.width() / sizeHead;
            }
            //画头像
            //canvas.drawRect(rectDistHead, paintHead);
            canvas.drawBitmap(bitmapHead, rectSrcHead, rectDistHead, paintHead);
            //填人物参数
            canvas.drawText(
                    sectionCallback.getAssets().unit.getName(0),
                    rectDistHeadBg.left + rectDistHeadBg.width() / 4,
                    rectDistHeadBg.top + rectDistHeadBg.height() / 4,
                    paintParam);
            canvas.drawText(
                    sectionCallback.getAssets().unit.getSummary(0),
                    rectDistHeadBg.left + rectDistHeadBg.width() / 4,
                    rectDistHeadBg.top + rectDistHeadBg.height() / 2,
                    paintParam);
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        ;
    }
}
