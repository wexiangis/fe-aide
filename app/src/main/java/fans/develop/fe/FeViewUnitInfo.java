package fans.develop.fe;

import android.content.*;
import android.graphics.*;

/*
    地图上的人物头像等信息框
 */
public class FeViewUnitInfo extends FeView {

    private FeSectionCallback sectionCallback;

    //头像背景框图片源位置和输出位置
    private Rect rectSrcHeadBg, rectDistHeadBg;
    //头像图片源位置和输出位置
    private Rect rectSrcHead, rectDistHead;
    //名称和参数文字书写范围
    private Rect rectDistName, rectDistParam;
		//血条框
		private Rect rectDistHp;
    //头像图片,头像背景框
    private Bitmap bitmapHead, bitmapHeadBg;
    //头像背景框,头像,名称文字,参数文字,背景血条的画笔
    private Paint paintHeadBg;
    private Paint paintHead;
    private Paint paintName;
    private Paint paintParam;
    private Paint paintHp;
    //素材边框在屏幕中的尺寸(约占图片高度的2/38)
    private int edge;
    //背景框参数
    private int width, height;
    //头像在背景框中的高
    private int headHeight;
    //名称和参数在背景框中的书写范围的宽
    private int textWdith, nameHeight, paramHeight;
    //像素比例
    private float pixelPowBitmap;
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
        pixelPowBitmap = sectionCallback.getSectionMap().yGridPixel * 2 / bitmapHeadBg.getHeight();
        //素材边框在屏幕中的尺寸(约占图片高度的2/38)
        edge = (int)(bitmapHeadBg.getHeight() * pixelPowBitmap * 2 / 38);
        //基本参数
        width = (int)(bitmapHeadBg.getWidth() * pixelPowBitmap);
        height = (int)(bitmapHeadBg.getHeight() * pixelPowBitmap);
        //计算图片源位置和输出位置
        rectSrcHeadBg = new Rect(0, 0, bitmapHeadBg.getWidth(), bitmapHeadBg.getHeight());
        rectDistHeadBg = new Rect(
                (int) (sectionCallback.getSectionMap().xGridPixel / 4),
                (int) (sectionCallback.getSectionMap().yGridPixel / 4),
                (int) (sectionCallback.getSectionMap().xGridPixel / 4 + width),
                (int) (sectionCallback.getSectionMap().yGridPixel / 4 + height));
        headHeight = rectDistHeadBg.height() - edge * 2;
        rectSrcHead = new Rect(0, 0, 1, 1);//等具体选中人物了才知道
        rectDistHead = new Rect(//头像仅挨边框左上角,填充出一个方形
                rectDistHeadBg.left + edge,
                rectDistHeadBg.top + edge,
                rectDistHeadBg.left + edge + headHeight,
                rectDistHeadBg.top + edge + headHeight);
        //
        textWdith = (int)(42 * pixelPowBitmap);
        nameHeight = paramHeight = headHeight / 2;
        //
        rectDistName = new Rect(0, 0, 1, 1);
        rectDistParam = new Rect(0, 0, 1, 1);
				rectDistHp = new Rect(0, 0, 1, 1);
        //
        paintHeadBg = new Paint();
        paintHeadBg.setColor(0xE0FF0000);//半透明
        //
        paintHead = new Paint();
        //
        paintName = new Paint();
        paintName.setColor(Color.WHITE);
        paintName.setTextSize(headHeight / 4);
        paintName.setTypeface(Typeface.DEFAULT_BOLD);
        paintName.setTextAlign(Paint.Align.CENTER);
        //
        paintParam = new Paint();
        paintParam.setTextSize(headHeight / 4);
        paintParam.setTypeface(Typeface.DEFAULT_BOLD);
        paintParam.setTextAlign(Paint.Align.CENTER);
        //
        paintHp = new Paint();
        paintHp.setColor(0x40404040);//半透明
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

        //有选中人物
        if (sectionCallback.getSectionUnit().viewUnit == null)
            return;
        FeViewUnit unitView = sectionCallback.getSectionUnit().viewUnit;
        FeInfoSite unitSite = unitView.site();

        //图像位置自动调整
        if (unitSite.rect.right > sectionCallback.getSectionMap().screenWidth / 2) { //放到左边
            rectDistHeadBg.left = (int) (sectionCallback.getSectionMap().xGridPixel / 4);
            rectDistHeadBg.right = (int) (sectionCallback.getSectionMap().xGridPixel / 4 + bitmapHeadBg.getWidth() * pixelPowBitmap);
        } else { //放到右边
            rectDistHeadBg.left = (int) (sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel / 4 - bitmapHeadBg.getWidth() * pixelPowBitmap);
            rectDistHeadBg.right = (int) (sectionCallback.getSectionMap().screenWidth - sectionCallback.getSectionMap().xGridPixel / 4);
        }
        rectDistHeadBg.right = rectDistHeadBg.left + width;
        rectDistHeadBg.bottom = rectDistHeadBg.top + height;
        //更新头像位置
        rectDistHead.left = rectDistHeadBg.left + edge;
        rectDistHead.top = rectDistHeadBg.top + edge;
        rectDistHead.right = rectDistHead.left + headHeight;
        rectDistHead.bottom = rectDistHead.top + headHeight;
        //更新name位置
        rectDistName.top = rectDistHeadBg.top - edge;
        rectDistName.right = rectDistHeadBg.right - edge;
        rectDistName.bottom = rectDistName.top + nameHeight;
        rectDistName.left = rectDistName.right - textWdith;
        //更新param位置
        rectDistParam.bottom = rectDistHeadBg.bottom - edge;
        rectDistParam.right = rectDistHeadBg.right - edge;
        rectDistParam.top = rectDistParam.bottom - paramHeight;
        rectDistParam.left = rectDistParam.right - textWdith;
				//更新血条框
				rectDistHp.left = rectDistHeadBg.left + edge;
				rectDistHp.top = rectDistHeadBg.top + edge;
				rectDistHp.right = rectDistHeadBg.right - edge;
				rectDistHp.bottom = rectDistHeadBg.bottom - edge;

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
            //抗锯齿
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            //人物变动或第一次初始化
            if(unitView != this.unitView || bitmapHead == null){
                //更新人物
                this.unitView = unitView;
                //重新加载头像
                bitmapHead = unitView.unit.getHead();
                //确定头像源位置
                if(bitmapHead.getWidth() == bitmapHead.getHeight()){//杂兵的等比例头像,切掉底部一块使用
                    rectSrcHead.right = bitmapHead.getWidth();
										rectSrcHead.left = 0;
                    rectSrcHead.bottom = 73;
                }
                else{//特殊人物的头像,需抠图
                    rectSrcHead.right = 95;
										rectSrcHead.left = 7;
                    rectSrcHead.bottom = 79;
                }
                rectDistHead.right = rectDistHead.left + rectSrcHead.height() * rectSrcHead.width() / headHeight;
            }
            //画背景框
            //canvas.drawRect(rectDistHeadBg, paintHeadBg);
            canvas.drawBitmap(bitmapHeadBg, rectSrcHeadBg, rectDistHeadBg, paintHeadBg);
            //画血条
            int hpRes = 15;//unitView.unit.hpRes();
            int hpTotal = unitView.unit.hp();
            float precent = (float)hpRes / hpTotal;
            if(precent <= 0.2)
                paintParam.setColor(0xB0FF0000);//20%红色
            else if(precent <= 0.5)
                paintParam.setColor(0xB0FFFF00);//50%黄色
            else
                paintParam.setColor(0xB0FFFFFF);//白色
            rectDistHp.top = (int)(rectDistHp.bottom - rectDistHp.height() * precent);
            canvas.drawRect(rectDistHp, paintHp);
            //画头像
            //canvas.drawRect(rectDistHead, paintHead);
            canvas.drawBitmap(bitmapHead, rectSrcHead, rectDistHead, paintHead);
            //填人物名称
            canvas.drawText(
                    sectionCallback.getAssets().unit.getName(0),
                    rectDistName.left + rectDistName.width() / 2,
                    rectDistName.top + rectDistName.height() / 2 + paintName.getTextSize(),
                    paintName);
            //填人物参数
            canvas.drawText(
                    String.format("%d / %d", hpRes, hpTotal),
                    rectDistParam.left + rectDistParam.width() / 2,
                    rectDistParam.top + rectDistParam.height() / 2 + paintParam.getTextSize() / 2,
                    paintParam);
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        ;
    }
}
