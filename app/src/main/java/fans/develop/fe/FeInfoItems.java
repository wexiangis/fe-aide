package fans.develop.fe;

/*
    解析人物装备可使用情况 及 攻击范围
 */
public class FeInfoItems {

    public enum USAGE{
        USELESS,//不可用
        USE,    //可用
        BROKEN, //损坏
    }

    //装备列表可用情况
    public USAGE usage[];
    
    //攻击距离
    public int hit = 0;
    //攻击空当
    public int hitSpace = 0;
    //特效距离
    public int special = 0;

    /*
        items: 装备列表
        skillLevel: 人物技能等级列表
        state: 人物状态(沉默、睡眠...)
     */
    public FeInfoItems(int[] items, int[] skillLevel, int state){
        //数据库
        FeAssetsParam param = FeData.assets.param;
        //可用结果
        usage = new USAGE[items.length];
        //遍历物品列表
        for(int i = 0; i < items.length; i++){
            //暂且认为不可用
            usage[i] = USAGE.USELESS;
            //没有物品
            if(items[i] == 0)
                continue;
            //物品id
            int id = items[i]%1000;
            //已使用次数
            int use = items[i]/1000;
            //超过使用次数?
            int capacity = param.getItemsCapacity(id);
            if(use >= capacity){
                usage[i] = USAGE.BROKEN;
                continue;
            }
            //物品类型
            int type = param.getItemsType(id);
            //这是普通物品?
            if(type >= skillLevel.length)
                usage[i] = USAGE.USE;
            //这是武器
            else{
                //当前技能等级达标? (等于0表示不可用)
                if(skillLevel[type] > 0 && skillLevel[type] >= param.getItemsLevel(id)){
                    //状态检查
                    ;

                    //可以使用
                    usage[i] = USAGE.USE;

                    //计算攻击范围
                    if(usage[i] == USAGE.USE){
                        int range = param.getItemsRange(id);
                        int rangeSpace = param.getItemsRangeSpace(id);
                        //这是杖?
                        if(param.getItemsType(id) == 7){
                            //使用更大的范围
                            if(this.special < range)
                                this.special = range;
                        }
                        //这是武器
                        else{
                            //使用更大的范围
                            if(this.hit < range)
                                this.hit = range;
                            //使用更小的空当
                            if(this.hitSpace > rangeSpace)
                                this.hitSpace = rangeSpace;
                        }
                    }
                }
            }
        }
    }
}
