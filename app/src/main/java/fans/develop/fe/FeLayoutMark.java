package fans.develop.fe;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

/*
    地板标记图层,显示各个人物的行动范围
 */
public class FeLayoutMark extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;
    //着色器心跳启动标志
    private Boolean shaderHeartStartFlag = false;

    public FeLayoutMark(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
    }

    private void shaderHeartStart(){
        //只启动一次
        if(shaderHeartStartFlag)
            return;
        //初始化着色器列表
        sectionCallback.getSectionShader().shaderR = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x80FF8080, 0x80FF2020, 0x80FF8080},
                new float[] {0.25F, 0.5F, 7.5F },
                Shader.TileMode.REPEAT
        );
        sectionCallback.getSectionShader().shaderG = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x8060FF60, 0x8020FF20, 0x8060F60F},
                new float[] {0.25F, 0.5F, 7.5F },
                Shader.TileMode.REPEAT
        );
        sectionCallback.getSectionShader().shaderB = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x808080FF, 0x802020FF, 0x808080FF},
                new float[] {0.1F, 0.5F, 0.9F },
                Shader.TileMode.REPEAT
        );
        //引入心跳,让渐变色动起来
        sectionCallback.addHeartUnit(heartUnit);
        //只启动一次
        shaderHeartStartFlag = true;
    }
    
    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //让渐变色动起来
            if(sectionCallback.getSectionShader().shaderCount + 1 < sectionCallback.getSectionShader().shaderR.xCount())
                sectionCallback.getSectionShader().shaderCount += 1;
            else
                sectionCallback.getSectionShader().shaderCount = 0;
        }
    });

    /* ---------- function ---------- */
    
    public boolean checkHit(float x, float y){
        return false;
    }

    public void refresh(){
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        显示特定人物的mark范围
     */
    public void markUnit(int id){
        if(!shaderHeartStartFlag)
            shaderHeartStart();
        //计算范围
        ;
        //显示范围
        addView(new FeViewMark(context, 1, 10, 10, sectionCallback));
    }

    /*
        关闭特定人物的mark范围
     */
    public void cleanUnit(int id){
        ;
    }

    /*
        关闭全部人物的mark范围
     */
    public void cleanAllUnit(){
        this.removeAllViews();
    }

    /*
        接收点击事件
     */
    public void click(float x, float y){
        ;
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof FeView)
                ((FeView)v).onDestory();
        }
        //没有启动就没有关闭
        if(!shaderHeartStartFlag)
            return true;
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
        return true;
    }
    public void onReload(){
        ;
    }
}
