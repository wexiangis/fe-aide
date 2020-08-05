package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
    敌军攻击范围、特效范围标记
 */
public class FeViewMarkEnemy extends FeView {

    private FeSectionCallback sectionCallback;

    //画笔
    private Paint paint;
    //颜色模式
    private FeTypeMark typeMark;
    //标记unit的id
    private int order = 0, id = 0;
    //人物的移动、攻击、特效范围
    private FeInfoSite[] siteMov;
    private FeInfoSite[] siteHit;
    private FeInfoSite[] siteSpecial;
    //id人物的位置,和新的位置进行比较确定是否更新
    private FeInfoSite siteUnit = null;

    /*
        typeMark: 颜色模式
        order: 地图人物唯一order
     */
    public FeViewMarkEnemy(Context context,
            FeTypeMark typeMark,
            int order,
            FeSectionCallback sectionCallback)
    {
        super(context);
        this.typeMark = typeMark;
        this.order = order;
        this.id = sectionCallback.getAssetsSX().saveCache.unit.getId(order);
        this.sectionCallback = sectionCallback;
        //画笔
        paint = new Paint();
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
    }

    public void setTypeMark(FeTypeMark typeMark){
        this.typeMark = typeMark;
    }

    public FeTypeMark getTypeMark(){
        return typeMark;
    }

    public int getOrder(){
        return order;
    }

    public FeInfoSite checkHit(int xGrid, int yGrid){
        return null;
    }

    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            FeViewMarkEnemy.this.invalidate();
        }
    });

    /*
        擦除自己画过的格子(每一层)
     */
    private void cleanMarkMap(){
        int[][][] markEnemyMap = sectionCallback.getSectionMap().markEnemyMap;
        for(int x = 0; x < markEnemyMap[0].length; x++)
            for(int y = 0; y < markEnemyMap.length; y++)
                for(int i = 0; i < markEnemyMap[0][0].length; i++)
                    if(markEnemyMap[y][x][i] == order)
                        markEnemyMap[y][x][i] = -1;
    }

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //没有unit图层?
        if(sectionCallback.getLayoutUnit() == null)
            return;
        //获得unit位置
        FeInfoSite siteUnit = sectionCallback.getLayoutUnit().getUnitSite(order);
        //order人物没有绘制?
        if(siteUnit == null)
            return;

        //第一次初始化 或者 人物位置变动了, 更新range
        if(this.siteUnit == null
            || this.siteUnit.xGrid != siteUnit.xGrid
            || this.siteUnit.yGrid != siteUnit.yGrid){
            //更新位置
            this.siteUnit = siteUnit;
            //计算范围
            FeMark mark = new FeMark(
                siteUnit.xGrid, siteUnit.yGrid,
                sectionCallback.getSectionMap().mapInfo,
                sectionCallback.getLayoutUnit().getUnit(order));
            //获取位置数组
            siteMov = mark.rangeMov.getGridInfo(sectionCallback.getSectionMap());
            siteHit = mark.rangeHit.getGridInfo(sectionCallback.getSectionMap());
            siteSpecial = mark.rangeSpecial.getGridInfo(sectionCallback.getSectionMap());
        }

        //按颜色取渲染、取位置数组
        int _typeMark = 0;
        FeInfoSite[] siteTarget = siteMov;
        if(typeMark == FeTypeMark.BLUE){
            paint.setShader(sectionCallback.getSectionShader().getShaderB());
            siteTarget = siteMov;
            _typeMark = 0;
        }
        else if(typeMark == FeTypeMark.RED){
            paint.setShader(sectionCallback.getSectionShader().getShaderR());
            siteTarget = siteHit;
            _typeMark = 1;
        }
        else{
            paint.setShader(sectionCallback.getSectionShader().getShaderG());
            siteTarget = siteSpecial;
            _typeMark = 2;
        }

        //擦除自己画过的格子(每一层)
        cleanMarkMap();

        int[][][] markEnemyMap = sectionCallback.getSectionMap().markEnemyMap;
        if(siteTarget != null){
            //遍历 siteTarget 数组,画格子
            for(int i = 0; i < siteTarget.length; i++){
                //没有画过这个格子?
                if(markEnemyMap[siteTarget[i].yGrid][siteTarget[i].xGrid][_typeMark] == -1){
                    //标记格子
                    markEnemyMap[siteTarget[i].yGrid][siteTarget[i].xGrid][_typeMark] = order;
                    //画格子
                    canvas.drawPath(siteTarget[i].path, paint);
                }
            }
        }
    }

    /* ---------- abstract interface ---------- */

    public void onDestory(){
        //擦除自己画过的格子(每一层)
        cleanMarkMap();
        //解除心跳注册
        sectionCallback.removeHeartUnit(heartUnit);
    }
}
