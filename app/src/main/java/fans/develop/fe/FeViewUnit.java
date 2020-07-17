package fans.develop.fe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;

/*
    电影胶片式播放动画,针对地图人物动画管理而封装;
    统一管理人物的待机、选中、上下左右移动时的动画
 */
public class FeViewUnit extends FeView {

    private FeSectionCallback sectionCallback;

    //动画相对地图的偏移量
    private float leftMargin = 0, topMargin = 0;

    //每帧图片实际高度
    private int frameHeight = 56;
    //人物唯一id
    private int id = 0;
    //动画模式和颜色模式
    private int animMode = 0, colorMode = 0;
    //当前人物所在格子
    public int gridX = 0, gridY = 0;

    //根据动画模式0~5,图像胶片上移帧数
    private final int[] frameSkipByAnimMode = new int[]{15, 12, 8, 4, 0, 0};

    //画图
    private Paint paint = new Paint();

    //原始图片
    private Bitmap bitmap = null;
    private Matrix matrix = new Matrix();

    //扣取图片位置
    private Rect bitmapBody = new Rect(0,0,0,0);

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }

    /*
        id: 人物唯一id
        animMode: 0/待机 1/选中 2,3,4,5/上,下,左,右
        colorMode: 0/原色 1/绿色 2/红色 3/灰色 4/橙色 5/紫色 6/不蓝不绿
     */
    public FeViewUnit(Context context, 
        int id, int gridX, int gridY, 
        int animMode, int colorMode,
        FeSectionCallback sectionCallback)
    {
        super(context);
        this.sectionCallback = sectionCallback;
        //画笔初始化
        paint = new Paint();
        paint.setColor(Color.GREEN);
//        paint.setAntiAlias(true);
//        paint.setBitmapFilter(true);
        //图片加载和颜色变换
        bitmap = FePallet.replace(sectionCallback.getAssets().unit.getProfessionAnim(id), colorMode);
        matrix.postScale(-1, 1);
        //根据动画类型使用对应的心跳
        setAnimMode(animMode);
        //参数备份
        this.colorMode = colorMode;
        this.id = id;
        //
        frameHeight = bitmap.getWidth();
        //
        moveGridTo(gridX, gridY);
        //图片扣取位置计算
        bitmapBody.left = 0;
        bitmapBody.top = frameHeight*frameSkipByAnimMode[this.animMode];
        bitmapBody.right = bitmap.getWidth();
        bitmapBody.bottom = bitmapBody.top + frameHeight;
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
        //地图中的位置信息管理结构
        site = new FeInfoGrid();
    }

    //人物id
    public int id(){
        return id;
    }

    //移动方格
    public void setGrid(int x, int y){
        gridX += x;
        gridY += y;
        leftMargin += x*sectionCallback.getSectionMap().xGridPixel;
        topMargin += y*sectionCallback.getSectionMap().yGridPixel;
    }

    //移动到方格
    public void moveGridTo(int x, int y){
        gridX = x;
        gridY = y;
        leftMargin = x*sectionCallback.getSectionMap().xGridPixel;
        topMargin = y*sectionCallback.getSectionMap().yGridPixel;
    }

    //颜色模式: 0/原色 1/绿色 2/红色 3/灰色 4/橙色 5/紫色 6/不蓝不绿
    public void setColorMode(int colorMode){
        if(this.colorMode != colorMode) {
            synchronized (paint) {
                bitmap.recycle();
                bitmap = FePallet.replace(sectionCallback.getAssets().unit.getProfessionAnim(id), colorMode);
                this.colorMode = colorMode;
            }
        }
    }
    
    //读颜色模式
    public int getColorMode(){
        return colorMode;
    }

    //设置动画模式: 0/待机 1/选中 2/上 3/下 4/左 5/右
    public void setAnimMode(int animMode){
        if(this.animMode != animMode){
            //镜像和恢复
            if(this.animMode == 5 || animMode == 5) {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) bitmap.getWidth(), (int) bitmap.getHeight(), matrix, true);
                matrix.postScale(1, 1);
            }
            //范围限制
            if(animMode < 0 || animMode > 5)
                animMode = 0;
            //缓存状态
            this.animMode = animMode;
            //更新动画顺序
            upgradeHeartType(animMode);
            //马上重绘
            if(animMode == 1)
                ;//heartUnit.task.run(2);//选中动画比较特殊,需从第2帧开始
            else
                heartUnit.task.run(0);
        }
        else {
            this.animMode = animMode;
            upgradeHeartType(animMode);
        }
    }

    //读动画模式
    public int getAnimMode(){
        return animMode;
    }

    //根据动画模式,切换心跳类型
    private void upgradeHeartType(int animMode){
        if(animMode == 0)
            heartUnit.type = FeHeart.TYPE_ANIM_STAY;
        else if(animMode == 1)
            heartUnit.type = FeHeart.TYPE_ANIM_SELECT;
        else
            heartUnit.type = FeHeart.TYPE_ANIM_MOVE;
    }

    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_ANIM_STAY, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //移动框图(电影胶片)
            bitmapBody.left = 0;
            bitmapBody.top = frameHeight*(frameSkipByAnimMode[animMode] + count);
            bitmapBody.right = bitmap.getWidth();
            bitmapBody.bottom = bitmapBody.top + frameHeight;
            //调用一次onDrow
            FeViewUnit.this.invalidate();
        }
    });

    //临时参数
    public FeInfoGrid site;//当前人物在地图中的位置
    private Rect bitmapDist = new Rect(0,0,0,0);//动画输出位置

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //跟地图要位置
        sectionCallback.getSectionMap().getRectByGrid(gridX, gridY, site);
        //扩大矩阵的上、左、右边界
        bitmapDist.left = site.rect.left - site.rect.width()/2;
        bitmapDist.right = site.rect.right + site.rect.width()/2;
        bitmapDist.top = site.rect.bottom - site.rect.width()*2;
        bitmapDist.bottom = site.rect.bottom;
        //绘图
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//抗锯齿
        canvas.drawBitmap(bitmap, bitmapBody, bitmapDist, paint);
    }

    //检查坐标是否在当前人物上
    public boolean checkHit(float x, float y){
        if(site.rect.contains((int)x, (int)y))
            return true;
        return false;
    }

}

