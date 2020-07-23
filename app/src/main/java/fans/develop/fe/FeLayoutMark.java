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
    //标记点击位置
    private FeViewMark hitViewMark = null;
    //标记当前中心
    private int certenXY[] = new int[2];

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
        FeViewMark viewMark;
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++) {
            viewMark = (FeViewMark)getChildAt(i);
            //只有蓝色格参与点击判定
            if (viewMark.getMark() == FeTypeMark.BLUE && viewMark.checkHit(x, y)) {
                hitViewMark = viewMark;
                return true;
            }
        }
        return false;
    }

    public void refresh(){
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        显示特定人物的mark范围
     */
    public void markUnit(){
        //未初始化FeLayoutMark
        if(!shaderHeartStartFlag)
            shaderHeartStart();
        //没有选中unit?
        FeSectionUnit sectionUnit = sectionCallback.getSectionUnit();
        if(sectionUnit == null || sectionUnit.viewUnit == null)
            return;
        //清移动范围
        removeAllViews();
        //获得移动力
        int mov = sectionUnit.mov;
        //中心坐标
        certenXY[0] = sectionUnit.viewUnit.getGridX();
        certenXY[1] = sectionUnit.viewUnit.getGridY();
        //开出一个矩阵以覆盖移动范围
        int gridSize = mov*2+1;
        int[][] grid = new int[gridSize][gridSize];
        //标记所有离中心距离小于等于mov的点
        for(int xCount = 0, x = -mov; xCount < gridSize; xCount++, x++){
            for(int yCount = 0, y = -mov; yCount < gridSize; yCount++, y++){
                int absSum = Math.abs(x) + Math.abs(y);
                if(absSum < mov)
                    grid[xCount][yCount] = 1;
                else if(absSum == mov)
                    grid[xCount][yCount] = 2;
            }
        }
        //左上角坐标
        int xStart = certenXY[0] - mov;
        int yStart = certenXY[1] - mov;
        //显示范围
        for(int xCount = 0; xCount < gridSize; xCount++){
            for(int yCount = 0; yCount < gridSize; yCount++){
                if(grid[xCount][yCount] == 1)
                    addView(new FeViewMark(context,
                        FeTypeMark.BLUE,
                        xCount + xStart,
                        yCount + yStart, sectionCallback));
                else if(grid[xCount][yCount] == 2)
                    addView(new FeViewMark(context,
                            FeTypeMark.RED,
                            xCount + xStart,
                            yCount + yStart, sectionCallback));
            }
        }
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
        removeAllViews();
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        //没有选中unit?
        FeSectionUnit sectionUnit = sectionCallback.getSectionUnit();
        if(sectionUnit == null || sectionUnit.viewUnit == null) {
            //清mark
            removeAllViews();
            return;
        }
        //
        if(!flag.checkFlag(FeFlagHit.HIT_MARK) || hitViewMark == null)
            return;
        //清mark
        removeAllViews();
        //己方人物?
        if (sectionUnit.viewUnit.getCamp() == FeTypeCamp.BLUE) {
            //移动人物
            sectionUnit.viewUnit.setGrid(hitViewMark.getGridX(), hitViewMark.getGridY());
            //削减移动力
            sectionUnit.mov -= Math.abs(certenXY[0] - hitViewMark.getGridX()) + Math.abs(certenXY[1] - hitViewMark.getGridY());
            //还有移动力剩余?
            if(sectionUnit.mov > 0)
                //更新移动范围
                markUnit();
            else
                //显示unitMenu
                ;
        }
        else{
            //非己方的移动范围,透传点击地图
            sectionCallback.onUnitSelect(false);
            FeLayoutMap layoutMap = sectionCallback.getLayoutMap();
            if(layoutMap != null)
                layoutMap.hitMap(hitViewMark.getSite().rect.left + 1, hitViewMark.getSite().rect.top + 1);
        }
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
