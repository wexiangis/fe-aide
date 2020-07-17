package fans.develop.fe;

import android.graphics.LinearGradient;

/*
    各个图层画图所需着色器都从这里取
 */
public class FeSectionShader {

    // ----------- 渐变色着色器 -----------

    //3种颜色的渐变色着色器列表
    public FeShader shaderR, shaderG, shaderB;
    //着色器当前位置
    public int shaderCount = 0;
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