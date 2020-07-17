package fans.develop.fe;

/*
    触屏命中flag
 */
public class FeFlagHit extends FeFlag {

    public static final int HIT_MAP = 0;//选中地图
    public static final int HIT_MARK = 1;//选中移动范围
    public static final int HIT_UNIT = 2;//选中人物
    public static final int HIT_MAP_INFO = 3;//选中地图信息
    public static final int HIT_UNIT_MENU = 4;//选中人物菜单
    public static final int HIT_CHAT = 5;//选中对话信息
    public static final int HIT_SYS_MENU = 6;//选中系统菜单

    public FeFlagHit(){
        //类型数量
        super(10);
    }
}