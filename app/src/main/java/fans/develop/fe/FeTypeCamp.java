package fans.develop.fe;

/*
    阵营颜色对应
    0/原色 1/绿色 2/红色 3/灰色 4/橙色 5/紫色 6/不蓝不绿
 */
public class FeTypeCamp {
    public static final int BLUE = 0;
    public static final int GREEN = 1;
    public static final int RED = 2;
    public static final int GRAY = 3;
    public static final int ORANGE = 4;
    public static final int PURPLE = 5;
    public static final int BLUE_GREEN = 6;

    public static Boolean isFriendCamp(int camp1, int camp2) {
        if (camp1 == camp2)
            return true;
        return false;
    }
}