package fans.develop.fe;

import android.graphics.LinearGradient;
import android.graphics.RectF;
import android.graphics.Shader;

/*
    针对FE封装的LinearGradient列表, 
    通过不断切换列表中的shader实现“渐变色”+“移动”的效果
    如: getLinearGradient(x++, y++)
 */
public class FeShader {

    private int xLength, yLength;
    private LinearGradient[][] linearGradient = null;

    public LinearGradient getLinearGradient(int xCount, int yCount) {
        if (xCount < 0 || xCount > xLength)
            return linearGradient[0][0];
        if (yCount < 0 || yCount > yLength)
            return linearGradient[0][0];
        return linearGradient[xCount][yCount];
    }

    public int xCount() {
        return xLength;
    }

    public int yCount() {
        return yLength;
    }

    /*
        rect: 填充区间
        xCount: 横向移动量
        yCount: 纵向移动量
        step: 步长
        colors[]: 颜色列表, 例如 new int[]{0xFF00FF00, 0xFFFF0000, 0xFF0000FF}
        positions[]: 指定colors[]里的颜色出现位置0.0~1.0, 例如 new float[]{0.25, 0.5, 0.75}
     */
    public FeShader(RectF rect, int xCount, int yCount, int step, int colors[], float positions[], Shader.TileMode tile) {

        xLength = xCount;
        yLength = yCount;

        linearGradient = new LinearGradient[xCount][yCount];

        for (int x = 0, xStep = 0; x < xCount; x++) {
            for (int y = 0, yStep = 0; y < yCount; y++) {
                linearGradient[x][y] = new LinearGradient(
                        rect.left + xStep,
                        rect.top + yStep,
                        rect.right + xStep,
                        rect.bottom + yStep,
                        colors,
                        positions,
                        tile
                );
                yStep += step;
            }
            xStep += step;
        }
    }
}
