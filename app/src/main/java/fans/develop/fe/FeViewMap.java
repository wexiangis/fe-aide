package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/*
    地图绘制和全局触屏回调管理
 */
public class FeViewMap extends FeView {

    private FeSectionCallback sectionCallback;

    //地图移动格子数
    private int xGridErr = 0, yGridErr = 0;
    //画笔
    private Paint paintMap;

    public FeViewMap(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;
        //输入坐标求格子位置
        sectionCallback.getSectionMap().getRectByLocation(0, 0, sectionCallback.getSectionMap().selectSite);
        //画笔
        paintMap = new Paint();
        paintMap.setColor(Color.BLUE);
        //引入心跳
        sectionCallback.addHeartUnit(heartMapMov);
    }

    //动态挪动地图,x>0时地图往右移,y>0时地图往下移
    public void moveGrid(int xGrid, int yGrid){
        xGridErr -= xGrid;
        yGridErr -= yGrid;
    }

    //动态挪动地图,设置(x,y)所在格子为地图中心
    public void moveCenter(int xGrid, int yGrid){
        //先把挪动停止
        xGridErr = yGridErr = 0;
        //居中比较
        xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.centerX();
        yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.centerY();
    }

    //动态挪动地图,设置(x,y)所在格子到地图能包围到
    public void moveInclude(int xGrid, int yGrid){
        //先把挪动停止
        // xGridErr = yGridErr = 0;
        //把需要移动的量先记到xGridErr,yGridErr, 动画心跳回调会慢慢把这些差值吃掉
        if(xGrid < sectionCallback.getSectionMap().srcGridCenter.left)
            xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.left;
        else if(xGrid > sectionCallback.getSectionMap().srcGridCenter.right)
            xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.right;
        if(yGrid < sectionCallback.getSectionMap().srcGridCenter.top)
            yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.top;
        else if(yGrid > sectionCallback.getSectionMap().srcGridCenter.bottom)
            yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.bottom;
    }

    //设置(x,y)所在格子为地图中心
    public void setCenter(int xGrid, int yGrid){
        //先把挪动停止
        xGridErr = yGridErr = 0;
        //居中比较
        sectionCallback.getSectionMap().xGridErr += xGrid - sectionCallback.getSectionMap().srcGridCenter.centerX();
        sectionCallback.getSectionMap().yGridErr += yGrid - sectionCallback.getSectionMap().srcGridCenter.centerY();
        //防止把地图移出屏幕
        if (sectionCallback.getSectionMap().xGridErr < 0)
            sectionCallback.getSectionMap().xGridErr = 0;
        else if (sectionCallback.getSectionMap().xGridErr + sectionCallback.getSectionMap().screenXGrid > sectionCallback.getSectionMap().mapInfo.width)
            sectionCallback.getSectionMap().xGridErr = sectionCallback.getSectionMap().mapInfo.width - sectionCallback.getSectionMap().screenXGrid;
        if (sectionCallback.getSectionMap().yGridErr < 0)
            sectionCallback.getSectionMap().yGridErr = 0;
        else if (sectionCallback.getSectionMap().yGridErr + sectionCallback.getSectionMap().screenYGrid > sectionCallback.getSectionMap().mapInfo.height)
            sectionCallback.getSectionMap().yGridErr = sectionCallback.getSectionMap().mapInfo.height - sectionCallback.getSectionMap().screenYGrid;
    }

    //动画心跳回调
    private FeHeartUnit heartMapMov = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART_QUICK, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //需要挪图?
            if(xGridErr != 0 || yGridErr != 0)
            {
                //每次移动一格
                if(xGridErr > 0) {
                    xGridErr -= 1;
                    sectionCallback.getSectionMap().xGridErr += 1;
                }else if(xGridErr < 0) {
                    xGridErr += 1;
                    sectionCallback.getSectionMap().xGridErr -= 1;
                }
                if(yGridErr > 0) {
                    yGridErr -= 1;
                    sectionCallback.getSectionMap().yGridErr += 1;
                }else if(yGridErr < 0) {
                    yGridErr += 1;
                    sectionCallback.getSectionMap().yGridErr -= 1;
                }
                //防止地图移出屏幕
                if (sectionCallback.getSectionMap().xGridErr < 0){
                    sectionCallback.getSectionMap().xGridErr = 0;
                    xGridErr = 0;
                }else if (sectionCallback.getSectionMap().xGridErr + sectionCallback.getSectionMap().screenXGrid > sectionCallback.getSectionMap().mapInfo.width){
                    sectionCallback.getSectionMap().xGridErr = sectionCallback.getSectionMap().mapInfo.width - sectionCallback.getSectionMap().screenXGrid;
                    xGridErr = 0;
                }
                if (sectionCallback.getSectionMap().yGridErr < 0){
                    sectionCallback.getSectionMap().yGridErr = 0;
                    yGridErr = 0;
                }else if (sectionCallback.getSectionMap().yGridErr + sectionCallback.getSectionMap().screenYGrid > sectionCallback.getSectionMap().mapInfo.height){
                    sectionCallback.getSectionMap().yGridErr = sectionCallback.getSectionMap().mapInfo.height - sectionCallback.getSectionMap().screenYGrid;
                    yGridErr = 0;
                }
                //调用一次onDraw
                FeViewMap.this.invalidate();
            }
        }
    });

    //
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //相对布局位置偏移
        sectionCallback.getSectionMap().mapDist.left = (int)this.getTranslationX() - (int)(sectionCallback.getSectionMap().xGridErr*sectionCallback.getSectionMap().xGridPixel);
        sectionCallback.getSectionMap().mapDist.top = (int)this.getTranslationY() - (int)(sectionCallback.getSectionMap().yGridErr*sectionCallback.getSectionMap().yGridPixel);
        sectionCallback.getSectionMap().mapDist.right = sectionCallback.getSectionMap().mapDist.left + sectionCallback.getSectionMap().width;
        sectionCallback.getSectionMap().mapDist.bottom = sectionCallback.getSectionMap().mapDist.top + sectionCallback.getSectionMap().height;
        //更新梯形变换信息
        sectionCallback.getSectionMap().upgradeMatrix();
        //显示地图
        canvas.drawBitmap(sectionCallback.getSectionMap().bitmap, sectionCallback.getSectionMap().matrix, paintMap);
        //地图移动了,刷新其他信息
        sectionCallback.refresh();
    }

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartMapMov);
    }
}

