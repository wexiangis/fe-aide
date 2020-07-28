package fans.develop.fe;

import android.graphics.*;
import android.view.*;

public class FeLayoutGameOver extends FeLayout{
    
    private FeData feData = null;

    public FeLayoutGameOver(FeData feData){
        super(feData.context);
        this.feData = feData;
    }

    //动画心跳回调
    private FeHeartUnit heartAnim = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART_QUICK, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            if(false){
                //用矩阵动态处理图片
                ;
                //调用一次onDraw
                FeLayoutGameOver.this.invalidate();
            }
        }
    });

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //图片显示
        ;
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        _removeViewAll(this);
        //解除心跳注册
        feData.removeHeartUnit(heartAnim);
        return true;
    }
    public void onReload(){
        //注册动画线程
        feData.addHeartUnit(heartAnim);
    }
}
