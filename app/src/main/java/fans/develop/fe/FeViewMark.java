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
    private Paint paint;
	
    private int colorMode;
	private int xGrid, yGrid;
	private FeInfoGrid gridInfo;

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }

    /*
        colorMode: 0/蓝色 1/红色 2/绿色
     */
    public FeViewMark(Context context, 
			int colorMode, 
			int xGird, 
			int yGrid,
			FeSectionCallback sectionCallback)
	{
        super(context);
        this.colorMode = colorMode;
		this.xGrid = xGird;
		this.yGrid = yGrid;
        this.sectionCallback = sectionCallback;
        //画笔
        paint = new Paint();
        paint.setColor(Color.BLUE);
		//
		gridInfo = new FeInfoGrid();
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
    }
	
	public void setColorMode(int colorMode){
		this.colorMode = colorMode;
	}
	
	public void setXY(int xGrid, int yGrid){
		this.xGrid = xGrid;
		this.yGrid = yGrid;
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

		sectionCallback.getSectionMap().getRectByGrid(xGrid, yGrid, gridInfo);

        if(colorMode == 0)
            paint.setShader(sectionCallback.getSectionShader().getShaderB());
        else if(colorMode == 1)
            paint.setShader(sectionCallback.getSectionShader().getShaderR());
        else
            paint.setShader(sectionCallback.getSectionShader().getShaderG());

        canvas.drawPath(gridInfo.path, paint);
    }
}
