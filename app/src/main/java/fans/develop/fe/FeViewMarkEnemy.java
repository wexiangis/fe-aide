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
    private int id;
    //人物的移动、攻击、特效范围
    private FeInfoGrid[] siteMov;
    private FeInfoGrid[] siteHit;
    private FeInfoGrid[] siteSpecial;
    //id人物的位置,和新的位置进行比较确定是否更新
    private FeInfoGrid siteUnit = null;

    /*
        typeMark: 颜色模式
        id: 人员
     */
    public FeViewMarkEnemy(Context context,
            FeTypeMark typeMark,
            int id,
            FeSectionCallback sectionCallback)
    {
        super(context);
        this.typeMark = typeMark;
        this.id = id;
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

    public int getId(){
        return id;
    }

    public boolean checkHit(float x, float y){
        return false;
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
        int[][][] markMap = sectionCallback.getSectionMap().markMap;
        for(int x = 0; x < markMap[0].length; x++)
            for(int y = 0; y < markMap.length; y++)
                for(int i = 0; i < markMap[0][0].length; i++)
                    if(markMap[y][x][i] == id)
                        markMap[y][x][i] = 0;
    }

    //绘图回调
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //没有unit图层?
        if(sectionCallback.getLayoutUnit() == null)
            return;
        //获得unit位置
        FeInfoGrid siteUnit = sectionCallback.getLayoutUnit().getUnitSite(id);
        //id人物没有绘制?
        if(siteUnit == null)
            return;

        //第一次初始化 或者 人物位置变动了, 更新range
        if(this.siteUnit == null
            || this.siteUnit.point[0] != siteUnit.point[0]
            || this.siteUnit.point[1] != siteUnit.point[1]){
            //更新位置
            this.siteUnit = siteUnit;
            //计算范围
            FeMark mark = new FeMark(
                siteUnit.point[0], siteUnit.point[1],
                sectionCallback.getSectionMap().mapInfo,
                sectionCallback.getAssets().unit.getProfessionAbilityMov(id),
                sectionCallback.getAssets().unit.getProfessionType(id),
                1, 0, 2);
            //获取位置数组
            siteMov = mark.rangeMov.getGridInfo(sectionCallback.getSectionMap());
            siteHit = mark.rangeHit.getGridInfo(sectionCallback.getSectionMap());
            siteSpecial = mark.rangeSpecial.getGridInfo(sectionCallback.getSectionMap());
        }

        //按颜色取渲染、取位置数组
        int _typeMark = 0;
        FeInfoGrid[] siteTarget = siteMov;
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

        int[][][] markMap = sectionCallback.getSectionMap().markMap;
        if(siteTarget != null){
            //遍历 siteTarget 数组,画格子
            for(int i = 0; i < siteTarget.length; i++){
                //没有画过这个格子?
                if(markMap[siteTarget[i].point[1]][siteTarget[i].point[0]][_typeMark] == 0){
                    //标记格子
                    markMap[siteTarget[i].point[1]][siteTarget[i].point[0]][_typeMark] = id;
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
