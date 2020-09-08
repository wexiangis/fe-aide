package fans.develop.fe;

import android.content.*;
import android.graphics.*;

/*
 地图上的人物头像等信息框
 */
public class FeViewUnitMenu extends FeView {

    private FeSectionCallback sectionCallback;
		//画笔: 背景
		private Paint paintBg;
		//输出范围: 背景
		private Rect rectDistBg;
		//
		private int order;

    public FeViewUnitMenu(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;
				//范围计算
				FeSectionMap sectionMap = sectionCallback.getSectionMap();
				rectDistBg = new Rect(0, 0, sectionMap.screenWidth, sectionMap.screenHeight);
				//半透明背景
				paintBg = new Paint();
				paintBg.setColor(0x80404040);
    }
		
		public void move(float xErr, float yErr){
				;
		}
		
		public void click(float x, float y){
				;
		}

		public void order(int order){
				this.order = order;
		}
		
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

				//画背景
				canvas.drawRect(rectDistBg, paintBg);
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        ;
    }
}
