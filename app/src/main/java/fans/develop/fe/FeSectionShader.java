package fans.develop.fe;

import android.graphics.*;

/*
    各个图层画图所需着色器都从这里取
 */
public class FeSectionShader {

    private FeSectionCallback sectionCallback;
    //3种颜色的渐变色着色器列表
    private FeShader shaderR, shaderG, shaderB;
    //着色器当前位置
    private int shaderCount = 0;

    public FeSectionShader(FeSectionCallback sectionCallback){
        this.sectionCallback = sectionCallback;
        //初始化着色器列表
        shaderR = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x80FF8080, 0x80FF2020, 0x80FF8080},
                new float[] {0.25F, 0.5F, 7.5F },
                Shader.TileMode.REPEAT
        );
        shaderG = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x8060FF60, 0x8020FF20, 0x8060F60F},
                new float[] {0.25F, 0.5F, 7.5F },
                Shader.TileMode.REPEAT
        );
        shaderB = new FeShader(
                new RectF(0, 0, sectionCallback.getSectionMap().xGridPixel, sectionCallback.getSectionMap().yGridPixel),
                (int)(sectionCallback.getSectionMap().xGridPixel/10), 1,
                20,
                new int[]{0x808080FF, 0x802020FF, 0x808080FF},
                new float[] {0.1F, 0.5F, 0.9F },
                Shader.TileMode.REPEAT
        );
        //引入心跳,让渐变色动起来
        sectionCallback.addHeartUnit(heartUnit);
    }
    
    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            //让渐变色光条循环动起来
            if(shaderCount + 1 < shaderR.xCount())
                shaderCount += 1;
            else
                shaderCount = 0;
        }
    });

    /*
        释放时记得移除心跳
     */
    public void release(){
        sectionCallback.removeHeartUnit(heartUnit);
    }

    // ----------- 渐变色着色器 -----------

    //从 shaderX (LinearGradient数组) 中获取当前着色器 LinearGradient
    public LinearGradient getShaderR(){
        return shaderR.getLinearGradient(shaderCount, 0);
    }
    public LinearGradient getShaderG(){
        return shaderG.getLinearGradient(shaderCount, 0);
    }
    public LinearGradient getShaderB(){
        return shaderB.getLinearGradient(shaderCount, 0);
    }

}
