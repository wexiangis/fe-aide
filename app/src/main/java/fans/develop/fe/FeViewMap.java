package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
    地图绘制和全局触屏回调管理
 */
public class FeViewMap extends FeView {

    private FeSectionCallback sectionCallback;

    //地图移动格子数
    private float xGridErr = 0.0f, yGridErr = 0.0f;
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
    public void moveGrid(int xGrid, int yGrid) {
        xGridErr = Math.round(xGridErr - xGrid);
        yGridErr = Math.round(yGridErr - yGrid);
    }

    /*
        像素移动,x>0时地图往右移,y>0时地图往下移
        注意xy为格子数，例如: x = 3.5 表示向右移动3.5格
     */
    public void move(float x, float y) {
        xGridErr -= x;
        yGridErr -= y;
    }

    //动态挪动地图,设置(x,y)所在格子为地图中心
    public void moveCenter(int xGrid, int yGrid) {
        //居中比较
        xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.centerX();
        yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.centerY();
    }

    //动态挪动地图,设置(x,y)所在格子到地图能包围到
    public void moveInclude(int xGrid, int yGrid) {
        //把需要移动的量先记到xGridErr,yGridErr, 动画心跳回调会慢慢把这些差值吃掉
        if (xGrid < sectionCallback.getSectionMap().srcGridCenter.left)
            xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.left;
        else if (xGrid > sectionCallback.getSectionMap().srcGridCenter.right)
            xGridErr = xGrid - sectionCallback.getSectionMap().srcGridCenter.right;
        if (yGrid < sectionCallback.getSectionMap().srcGridCenter.top)
            yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.top;
        else if (yGrid > sectionCallback.getSectionMap().srcGridCenter.bottom)
            yGridErr = yGrid - sectionCallback.getSectionMap().srcGridCenter.bottom;
    }

    //设置(x,y)所在格子为地图中心
    public void setCenter(int xGrid, int yGrid) {
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
    private FeHeartUnit heartMapMov = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART_QUICK, new FeHeartUnit.TimeOutTask() {
        public void run(int count) {
            float div = 0.25f;
            //需要挪图?
            if (Math.abs(xGridErr) >= div || Math.abs(yGridErr) >= div) {
                FeSectionMap sectionMap = sectionCallback.getSectionMap();
                //倍数移动
                ;
                //每次移动1/4格
                if (xGridErr >= div) {
                    xGridErr -= div;
                    sectionMap.xGridErr -= div;
                } else if (xGridErr <= -div) {
                    xGridErr += div;
                    sectionMap.xGridErr += div;
                }
                if (yGridErr >= div) {
                    yGridErr -= div;
                    sectionMap.yGridErr -= div;
                } else if (yGridErr <= -div) {
                    yGridErr += div;
                    sectionMap.yGridErr += div;
                }
                //防止地图移出屏幕
                if (sectionMap.xGridErr > 0) {
                    sectionMap.xGridErr = 0;
                    xGridErr = 0;
                } else if (sectionMap.xGridErr + sectionMap.mapInfo.width < sectionMap.screenXGrid) {
                    sectionMap.xGridErr = sectionMap.screenXGrid - sectionMap.mapInfo.width;
                    xGridErr = 0;
                }
                if (sectionMap.yGridErr > 0) {
                    sectionMap.yGridErr = 0;
                    yGridErr = 0;
                } else if (sectionMap.yGridErr + sectionMap.mapInfo.height < sectionMap.screenYGrid) {
                    sectionMap.yGridErr = sectionMap.screenYGrid - sectionMap.mapInfo.height;
                    yGridErr = 0;
                }
                //置状态
                sectionCallback.onMapMove(true);
                //调用一次onDraw
                FeViewMap.this.invalidate();
            } else if (!sectionCallback.onTouchMov()) {
                //剩余的一点差值丢弃
                if (Math.abs(xGridErr) > 0) {
                    xGridErr = 0;
                    //sectionCallback.getSectionMap().xGridErr = Math.round(sectionCallback.getSectionMap().xGridErr);
                }
                if (Math.abs(yGridErr) > 0) {
                    yGridErr = 0;
                    //sectionCallback.getSectionMap().yGridErr = Math.round(sectionCallback.getSectionMap().yGridErr);
                }
                //结束移动时，地图偏移量必须为整数格
                if (sectionCallback.getSectionMap().xGridErr % 1 != 0)
                    sectionCallback.getSectionMap().xGridErr = Math.round(sectionCallback.getSectionMap().xGridErr);
                if (sectionCallback.getSectionMap().yGridErr % 1 != 0)
                    sectionCallback.getSectionMap().yGridErr = Math.round(sectionCallback.getSectionMap().yGridErr);
                //清状态
                sectionCallback.onMapMove(false);
            }
        }
    });

    //
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        FeSectionMap sectionMap = sectionCallback.getSectionMap();
        //相对布局位置偏移
        sectionMap.mapDist.left = (int) (sectionMap.xGridErr * sectionMap.xGridPixel);
        sectionMap.mapDist.top = (int) (sectionMap.yGridErr * sectionMap.yGridPixel);
        sectionMap.mapDist.right = sectionMap.mapDist.left + sectionMap.width;
        sectionMap.mapDist.bottom = sectionMap.mapDist.top + sectionMap.height;
        //更新梯形变换信息
        sectionMap.upgradeMatrix();
        //显示地图
        canvas.drawBitmap(sectionMap.bitmap, sectionMap.matrix, paintMap);
        //地图移动了,刷新其他信息
        sectionCallback.refresh();
        sectionCallback.debug("reduceGrid", 0xFFFF0000, String.valueOf(sectionMap.reduceGrid));
        sectionCallback.debug("xStart", 0xFFFF0000, String.valueOf(sectionMap.trapGrid.xStart()));
        sectionCallback.debug("yStart", 0xFFFF0000, String.valueOf(sectionMap.trapGrid.yStart()));
        sectionCallback.debug("xErr", 0xFFFF0000, String.valueOf(sectionMap.xGridErr));
        sectionCallback.debug("yErr", 0xFFFF0000, String.valueOf(sectionMap.yGridErr));
        sectionCallback.debug2("xErrRed", 0xFFFF0000, String.valueOf(sectionMap.xGridErrRed));
        sectionCallback.debug2("yErrRed", 0xFFFF0000, String.valueOf(sectionMap.yGridErrRed));
    }

    /* ---------- abstract interface ---------- */

    public void onDestory() {
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartMapMov);
    }
}

