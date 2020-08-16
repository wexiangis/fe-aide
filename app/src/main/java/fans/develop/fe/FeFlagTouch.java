package fans.develop.fe;

/*
    触屏使能flag
 */
public class FeFlagTouch extends FeFlag {

    public static final int DISABLE = 0;//完全禁止
    public static final int ONLY_VIEW = 1;//只允许拖动地图(敌军回合时)

    public FeFlagTouch() {
        //类型数量
        super(10);
    }
}