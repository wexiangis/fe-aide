package fans.develop.fe;

import android.content.*;
import android.graphics.*;

/*
 地图上的人物头像等信息框
 */
public class FeViewUnitMenu extends FeView {

    private FeSectionCallback sectionCallback;
		//画笔: 背景, 框图背景
		private Paint paintBg, paintBg2;
		//框图间隔
		private final int edge = 5;
		//输出范围: 背景, 头像框, 列表框, 主框
		private Rect rectDistBg, rectDistHead, rectDistList, rectDistMain;
    //缓存当前人物,若变动则重新加载参数和头像
    private FeUnit unit = null;
		//人物头像
		private Bitmap bitmapHead;

    public FeViewUnitMenu(Context context, FeSectionCallback sectionCallback, int order) {
        super(context);
        this.sectionCallback = sectionCallback;
				unit = new FeUnit(sectionCallback.getAssets(), sectionCallback.getAssetsSX(), order);
				bitmapHead = unit.getHead();
				//范围计算
				FeSectionMap sectionMap = sectionCallback.getSectionMap();
				rectDistBg = new Rect(0, 0, sectionMap.screenWidth, sectionMap.screenHeight);
				rectDistHead = new Rect(
				    edge, edge, 
				    rectDistBg.right - (int)(rectDistBg.width() / 3.5),
						rectDistBg.height() / 3);
				rectDistList = new Rect(
				    rectDistHead.right + edge,
						rectDistHead.top,
						rectDistBg.right - edge,
						rectDistBg.bottom - edge);
				rectDistMain = new Rect(
				    rectDistHead.left,
						rectDistHead.bottom + edge,
						rectDistHead.right,
						rectDistBg.bottom - edge);
				//半透明背景
				paintBg = new Paint();
				paintBg.setColor(0x80404040);
				paintBg2 = new Paint();
				paintBg2.setColor(0x80000080);
    }
		
		public void move(float xErr, float yErr){
				;
		}
		
		public void click(float x, float y){
				;
		}

		public void order(int order){
				//更新unit
				if(unit == null || unit.order() != order)
						unit = new FeUnit(sectionCallback.getAssets(), sectionCallback.getAssetsSX(), order);
		}
		
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
				//画背景
				canvas.drawRect(rectDistBg, paintBg);
				//根据阵营更换底色
				paintBg2.setColor(FePallet.color(0x80404080, unit.camp()));
				//画框图
				canvas.drawRect(rectDistHead, paintBg2);
				canvas.drawRect(rectDistList, paintBg2);
				canvas.drawRect(rectDistMain, paintBg2);
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        ;
    }
}
