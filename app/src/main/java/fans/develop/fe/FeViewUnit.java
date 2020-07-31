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

    //每帧图片实际高度
    private int frameHeight = 56;
    //动画模式和颜色模式
    private FeTypeAnim anim = FeTypeAnim.STAY;
    //根据动画模式0~5,图像胶片上移帧数
    private final int[] frameSkipByAnimMode = new int[]{15, 12, 8, 4, 0, 0};
    //画图
    private Paint paint = new Paint();
    //原始图片
    private Bitmap bitmap = null;
    //用来翻转图片的矩阵(左跑和右跑用的同一个动画)
    private Matrix matrix = new Matrix();
    //扣取图片位置
    private Rect bitmapBody = new Rect(0,0,0,0);
    //当前人物在地图中的位置
    private FeInfoSite site;
    //动画输出位置
    private Rect bitmapDist = new Rect(0,0,0,0);
    //参数总集
    public FeUnit unit;

    /*
        order: 地图人物唯一order
        gridX, gridY: 所在格子
     */
    public FeViewUnit(Context context, int order, FeSectionCallback sectionCallback)
    {
        super(context);
        this.sectionCallback = sectionCallback;
        this.unit = new FeUnit(sectionCallback.getAssets(), sectionCallback.getAssetsSX(), order);
        //画笔初始化
        paint = new Paint();
        paint.setColor(Color.GREEN);
//        paint.setAntiAlias(true);
//        paint.setBitmapFilter(true);
        //图片加载和颜色变换
        bitmap = FePallet.replace(unit.getProfessionAnim(), unit.camp());
        matrix.postScale(-1, 1);
        //根据动画类型使用对应的心跳
        anim(FeTypeAnim.STAY);
        //胶片每帧高度
        frameHeight = bitmap.getWidth();
        //图片扣取位置计算
        bitmapBody.left = 0;
        bitmapBody.top = frameHeight*frameSkipByAnimMode[this.anim.ordinal()];
        bitmapBody.right = bitmap.getWidth();
        bitmapBody.bottom = bitmapBody.top + frameHeight;
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
        //地图中的位置信息管理结构
        site = new FeInfoSite();
    }

    //人物order
    public int order(){
        return unit.order();
    }

    //方格位置
    public void xy(int x, int y){
        unit.x(x);
        unit.y(y);
    }
    public int x(){
        return unit.x();
    }
    public int y(){
        return unit.y();
    }

    //阵营
    public FeTypeCamp camp(){
        return unit.camp();
    }

    //动画模式
    public void anim(FeTypeAnim anim){
        if(this.anim != anim){
            //镜像和恢复
            if(this.anim == FeTypeAnim.RIGHT || anim == FeTypeAnim.RIGHT) {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) bitmap.getWidth(), (int) bitmap.getHeight(), matrix, true);
                matrix.postScale(1, 1);
            }
            //缓存状态
            this.anim = anim;
            //更新动画顺序
            upgradeHeartType(anim);
            //马上重绘
            if(anim == FeTypeAnim.ACTIVITY)
                ;//heartUnit.task.run(2);//选中动画比较特殊,需从第2帧开始
            else
                heartUnit.task.run(0);
        }
        else {
            this.anim = anim;
            upgradeHeartType(anim);
        }
    }
    public FeTypeAnim anim(){
        return anim;
    }

    //根据动画模式,切换心跳类型
    private void upgradeHeartType(FeTypeAnim anim){
        if(anim == FeTypeAnim.STAY)
            heartUnit.type = FeHeart.TYPE_ANIM_STAY;
        else if(anim == FeTypeAnim.ACTIVITY)
            heartUnit.type = FeHeart.TYPE_ANIM_SELECT;
        else
            heartUnit.type = FeHeart.TYPE_ANIM_MOVE;
    }

    //位置信息
    public FeInfoSite site(){
        return site;
    }

    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_ANIM_STAY, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //移动框图(电影胶片)
            bitmapBody.left = 0;
            bitmapBody.top = frameHeight*(frameSkipByAnimMode[anim.ordinal()] + count);
            bitmapBody.right = bitmap.getWidth();
            bitmapBody.bottom = bitmapBody.top + frameHeight;
            //调用一次onDrow
            FeViewUnit.this.invalidate();
        }
    });

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //跟地图要位置
        sectionCallback.getSectionMap().getRectByGrid(unit.x(), unit.y(), site);
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

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }
}

