package fans.develop.fe;

import android.content.*;
import android.graphics.*;

/*
 地图上的人物头像等信息框
 */
public class FeViewUnitMenu extends FeView {

    private FeSectionCallback sectionCallback;
    //画笔: 背景, 框图背景, label
    private Paint paintBg, paintFrame, paintLabel, paintValue;
    //背景和屏幕间隔, 框和背景间隔
    private final int edgeBg = 5, edgeFrame = 8;
    //输出范围: 背景, 上框, 右列表框, 主框
    private Rect rectBg, rectTop, rectRight, rectMain;
    //输出范围: lv,exp和hp 攻击,回避,必杀等 头像图片
    private Rect rectHp, rectAk, rectHead;
    //缓存当前人物,若变动则重新加载参数和头像
    private FeUnit unit = null;
    //人物头像
    private Bitmap bitmapHead;

    public FeViewUnitMenu(Context context, FeSectionCallback sectionCallback, int order) {
        super(context);
        this.sectionCallback = sectionCallback;
        unit = new FeUnit(sectionCallback.getAssets(), sectionCallback.getAssetsSX(), order);
        bitmapHead = unit.getHead();

        FeSectionMap sectionMap = sectionCallback.getSectionMap();
        //范围计算
        rectBg = new Rect(0, 0, sectionMap.screenWidth, sectionMap.screenHeight);
        rectTop = new Rect(
            edgeBg, edgeBg, 
            rectBg.right - (int)(rectBg.width() / 3.5),
            rectBg.height() / 3);
        rectRight = new Rect(
            rectTop.right + edgeBg,
            rectTop.top,
            rectBg.right - edgeBg,
            rectBg.bottom - edgeBg);
        rectMain = new Rect(
            rectTop.left,
            rectTop.bottom + edgeBg,
            rectTop.right,
            rectBg.bottom - edgeBg);
        //范围计算
        rectHp = new Rect(
            rectTop.left + edgeFrame,
            rectTop.top + edgeFrame,
            rectTop.left + edgeFrame + rectTop.width() / 3,
            rectTop.bottom - edgeFrame);
        rectHead = new Rect(
            rectTop.right - edgeFrame - rectTop.height() - edgeFrame * 2,
            rectTop.top + edgeFrame,
            rectTop.right - edgeFrame,
            rectTop.bottom - edgeFrame);
        rectAk = new Rect(
            rectHp.right + edgeFrame,
            rectHp.top,
            rectHead.left - edgeFrame,
            rectHp.bottom);
        //半透明背景
        paintBg = new Paint();
        paintBg.setColor(0x80404040);

        paintFrame = new Paint();
        paintFrame.setColor(0x80000080);

        paintLabel = new Paint();
        paintLabel.setColor(0x80808080);
        paintLabel.setTextSize(rectHp.height() / 7);
        paintLabel.setTypeface(Typeface.SANS_SERIF);
        paintLabel.setTextAlign(Paint.Align.LEFT);

        paintValue = new Paint();
        paintValue.setColor(0x80FFFFFF);
        paintValue.setTextSize(rectHp.height() / 7);
        paintValue.setTypeface(Typeface.DEFAULT_BOLD);
        paintValue.setTextAlign(Paint.Align.CENTER);
    }

    public void move(float xErr, float yErr) {
        ;
    }
    
    public void click(float x, float y) {
        ;
    }

    public void order(int order) {
        //更新unit
        if(unit == null || unit.order() != order)
            unit = new FeUnit(sectionCallback.getAssets(), sectionCallback.getAssetsSX(), order);
    }

    private void drawTopFrame(Canvas canvas) {
        //label
        ;
        //value
        ;
    }
    private void drawMainFrame(Canvas canvas) {
        ;
    }
    private void drawRightFrame(Canvas canvas) {
        ;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        canvas.drawRect(rectBg, paintBg);
        //根据阵营更换底色
        paintFrame.setColor(FePallet.color(0x80404080, unit.camp()));
        //画框图
        canvas.drawRect(rectTop, paintFrame);
        canvas.drawRect(rectRight, paintFrame);
        canvas.drawRect(rectMain, paintFrame);
        //画框图
        canvas.drawRect(rectHp, paintFrame);
        canvas.drawRect(rectAk, paintFrame);
        canvas.drawRect(rectHead, paintFrame);

        drawTopFrame(canvas);
        drawMainFrame(canvas);
        drawRightFrame(canvas);
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        ;
    }
}
