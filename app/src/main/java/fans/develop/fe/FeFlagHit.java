package fans.develop.fe;

/*
    触屏命中flag
 */
public class FeFlagHit extends FeFlag {

    public static final int HIT_MAP = 0;//选中地图
    public static final int HIT_MARK_ENEMY = 1;//选中移动范围
    public static final int HIT_MARK = 3;//选中移动范围
    public static final int HIT_UNIT = 4;//选中人物
    public static final int HIT_MAP_INFO = 5;//选中地图信息
    public static final int HIT_UNIT_MENU = 6;//选中人物菜单
    public static final int HIT_CHAT = 7;//选中对话信息
    public static final int HIT_SYS_MENU = 8;//选中系统菜单

    public FeFlagHit(){
        //类型数量
        super(10);
    }
}