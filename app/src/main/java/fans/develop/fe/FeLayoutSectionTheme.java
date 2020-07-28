package fans.develop.fe;

import android.graphics.*;
import android.view.*;

public class FeLayoutSectionTheme extends FeLayout{

    private FeData feData = null;
    // 存档槽位,从0数起
    private int sX = 0;
    // 存档数据
    private FeAssetsSX sxData = null;
    // 后台线程,加载数据
    private FeThread thread = null;
    // 运行标志
    private Boolean run = false;

    public FeLayoutSectionTheme(FeData feData, int sX){
        super(feData.context);
        this.feData = feData;
        this.sX = sX;
    }

    //动画心跳回调
    private FeHeartUnit heartAnim = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART_QUICK, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            if(run){
                //用矩阵动态处理图片
                ;
                //调用一次onDraw
                FeLayoutSectionTheme.this.invalidate();
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
        //没有初始化
        if(!run)
            return true;
        //等待数据加载完毕
        try{
            thread.join();
        }
        catch (InterruptedException e)
        {}
        //释放子view
        _removeViewAll(this);
        //解除心跳注册
        feData.removeHeartUnit(heartAnim);
        //清标志
        thread = null;
        return true;
    }
    public void onReload(){
        //正在运行
        if(run)
            return;
        //注册动画线程
        feData.addHeartUnit(heartAnim);
        //开始后台线程加载数据
        thread = new FeThread(new Runnable(){
            public void run(){
                //加载数据
                sxData = feData.assets.save.loadSx(sX);
            }
        });
        thread.start();
    }
}
