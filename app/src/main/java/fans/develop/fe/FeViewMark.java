package fans.develop.fe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
    敌军攻击范围、特效范围标记
 */
public class FeViewMark extends FeView {

    private FeSectionCallback sectionCallback;

    //画笔,蓝色、红色、绿色,分别用于画移动范围、攻击范围、特效范围
    private Paint paintB, paintR, paintG;
    //颜色模式
    private FeTypeMark typeMark;
    //标记unit的id
    private int order = 0, id = 0;
    //标记unit的mov
    private int mov;
    //人物的移动、攻击、特效范围
    private FeInfoSite[] siteMov;
    private FeInfoSite[] siteHit;
    private FeInfoSite[] siteSpecial;
    //id人物的位置,和新的位置进行比较确定是否更新
    private FeInfoSite siteUnit = null;
    //范围计算用
    private FeMark mark;

    /*
        typeMark: 颜色模式
        order: 地图人物唯一order
     */
    public FeViewMark(Context context,
            FeTypeMark typeMark,
            int order,
            int mov,
            FeSectionCallback sectionCallback)
    {
        super(context);
        this.typeMark = typeMark;
        this.order = order;
        this.id = sectionCallback.getAssetsSX().saveCache.unit.getId(order);
        this.mov = mov;
        this.sectionCallback = sectionCallback;
        //画笔
        paintB = new Paint();
        paintR = new Paint();
        paintG = new Paint();
        //引入心跳
        sectionCallback.addHeartUnit(heartUnit);
    }

    public void setTypeMark(FeTypeMark typeMark){
        this.typeMark = typeMark;
    }

    public FeTypeMark getTypeMark(){
        return typeMark;
    }

    public void setMov(int mov){
        this.mov = mov;
    }

    public int getMov(){
        return mov;
    }

    public int getOrder(){
        return order;
    }

    public FeInfoSite checkHit(int xGrid, int yGrid){
        //非移动范围
        if(siteMov == null)
            return null;
        //遍历 siteMov
        for(int i = 0; i < siteMov.length; i++)
            if(siteMov[i].xGrid == xGrid && siteMov[i].yGrid == yGrid)
                return siteMov[i];
        return null;
    }

    //动画心跳回调
    private FeHeartUnit heartUnit = new FeHeartUnit(FeHeart.TYPE_FRAME_HEART, new FeHeartUnit.TimeOutTask(){
        public void run(int count){
            FeViewMark.this.invalidate();
        }
    });

    /*
        擦除自己画过的格子
     */
    private void cleanMarkMap(){
        int[][] markMap = sectionCallback.getSectionMap().markMap;
        for(int x = 0; x < markMap[0].length; x++)
            for(int y = 0; y < markMap.length; y++)
                if(markMap[y][x] == order)
                    markMap[y][x] = -1;
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
            mark = new FeMark(
                siteUnit.xGrid, siteUnit.yGrid,
                sectionCallback.getSectionMap().mapInfo,
                sectionCallback.getSectionMap().unitMap,
                sectionCallback.getLayoutUnit().getUnit(order));
        }

        //获取位置数组
        siteMov = mark.rangeMov.getGridInfo(sectionCallback.getSectionMap());
        siteHit = mark.rangeHit.getGridInfo(sectionCallback.getSectionMap());
        siteSpecial = mark.rangeSpecial.getGridInfo(sectionCallback.getSectionMap());

        if(siteHit == null)
            siteHit = siteMov;
        if(siteSpecial == null)
            siteSpecial = siteMov;

        //按颜色取渲染
        paintB.setShader(sectionCallback.getSectionShader().getShaderB());
        paintR.setShader(sectionCallback.getSectionShader().getShaderR());
        paintG.setShader(sectionCallback.getSectionShader().getShaderG());

        //擦除自己画过的格子(每一层)
        cleanMarkMap();
        int[][] markMap = sectionCallback.getSectionMap().markMap;

        //遍历 siteMov 数组,画格子
        for(int i = 0; i < siteMov.length; i++){
            canvas.drawPath(siteMov[i].path, paintB);
            //标记已画过
            markMap[siteMov[i].yGrid][siteMov[i].xGrid] = order;
        }

        //遍历 siteHit 数组,画格子
        if(typeMark == FeTypeMark.RED){
            for(int i = 0; i < siteHit.length; i++){
                //这个点刚才没有画过移动范围?
                if(markMap[siteHit[i].yGrid][siteHit[i].xGrid] != order)
                    canvas.drawPath(siteHit[i].path, paintR);
            }
        }
        //遍历 siteSpecial 数组,画格子
        else{
            for(int i = 0; i < siteSpecial.length; i++){
                //这个点刚才没有画过移动范围?
                if(markMap[siteSpecial[i].yGrid][siteSpecial[i].xGrid] != order)
                    canvas.drawPath(siteSpecial[i].path, paintR);
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
