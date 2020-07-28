package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
    带动态渐变色的标记方格
 */
public class FeViewMark extends FeView {

    private FeSectionCallback sectionCallback;

    //画笔
    private Paint paint;
    //颜色模式
    private FeTypeMark mark;
    //所在格
    private int xGrid, yGrid;
    //在地图中的位置
    private FeInfoGrid site;

    /*
        mark: 颜色模式
        xGird、yGird: 格子位置
     */
    public FeViewMark(Context context,
          FeTypeMark mark,
            int xGird, 
            int yGrid,
            FeSectionCallback sectionCallback)
    {
        super(context);
        this.mark = mark;
        this.xGrid = xGird;
        this.yGrid = yGrid;
        this.sectionCallback = sectionCallback;
        //画笔
        paint = new Paint();
        paint.setColor(Color.BLUE);
        //位置初始化
        site = new FeInfoGrid();
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
    }
    
    public void setXY(int xGrid, int yGrid){
        this.xGrid = xGrid;
        this.yGrid = yGrid;
    }

    public int getGridX(){
        return xGrid;
    }
    public int getGridY(){
        return yGrid;
    }

    public FeInfoGrid getSite(){
        return site;
    }

    public FeTypeMark getMark(){
        return mark;
    }

    public boolean checkHit(float x, float y){
        if(site.rect.contains((int)x, (int)y))
            return true;
        return false;
    }

    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            FeViewMark.this.invalidate();
        }
    });

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //求格子位置
        sectionCallback.getSectionMap().getRectByGrid(xGrid, yGrid, site);
        //按颜色取渲染
        if(mark == FeTypeMark.BLUE)
            paint.setShader(sectionCallback.getSectionShader().getShaderB());
        else if(mark == FeTypeMark.RED)
            paint.setShader(sectionCallback.getSectionShader().getShaderR());
        else
            paint.setShader(sectionCallback.getSectionShader().getShaderG());
        //画填充多边形
        canvas.drawPath(site.path, paint);
    }

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }
}
