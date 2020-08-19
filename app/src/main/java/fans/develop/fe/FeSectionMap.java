package fans.develop.fe;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;

/*
    加载地图、地图移动和变形后所产生的一系列关键参数, 各个
    图层在绘图时获取某个方格位置、方格和地图长宽都从这里来.
 */
public class FeSectionMap {

    //----- 关键结构 -----

    //对应章节数
    public int section;
    //地图缩放、梯形变换矩阵
    public Matrix matrix = new Matrix();
    //地图信息
    public FeInfoMap mapInfo;
    //地图
    public Bitmap bitmap = null;

    //----- mark记录 -----

    //敌军mark点覆盖情况,用于避免重复标记同一格
    //数组: [mapHeigth][mapEidth][3]: [0]/移动范围, [1]/攻击范围, [2]/特效范围, 值为order, -1表示空
    //用于累积各个敌军的攻击、移动、特效范围
    public int[][][] markEnemyMap;

    //地图中人物站位情况
    //数组: [mapHeigth][mapEidth][2]: [0]/填写order, [1]/填写阵营camp, -1表示空
    //用于绘制人物移动范围时,考虑其它人物站位影响
    public int[][][] unitMap;

    //----- 地图基本信息 -----

    //屏幕实际宽高
    public int screenWidth = 1920, screenHeight = 1080;
    //地图左上角偏移格子数,一般都为负值
    public float xGridErr = 0.0f, yGridErr = 0.0f;
    //地图实际显示宽高像素
    public float mapWidth = 1920, mapHeight = 1080;
    //屏幕横纵向格数
    public int screenXGrid = 20, screenYGrid = 10;

    //横纵向每格像素
    public float xGridPixel = 96, yGridPixel = 108;
    //横纵向动画方块像素大小
    public int xAnimGridPixel = 192, yAnimGridPixel = 216;
    //横纵向动画初始偏移
    public int xAnimOffsetPixel = 48, yAnimOffsetPixel = 54;

    public FeSectionMap(int section, FeInfoMap mapInfo, int screenWidth, int screenHeight) {
        this.section = section;
        this.mapInfo = mapInfo;
        //屏幕长、高格子数适配屏幕分辨率,得到地图实际显示长高(width、height)
        init(screenWidth, screenHeight, mapInfo.width, mapInfo.height, mapInfo.pixelPerGrid);
        //xp、yp分别为xy缩放比例(原始长、高缩放到实际显示长、高)
        float xp = (float) mapWidth / mapInfo.bitmap.getWidth();
        float yp = (float) mapHeight / mapInfo.bitmap.getHeight();
        //用缩放后的矩阵初始化bitmap,缩放效果较好
        matrix.postScale(xp, yp);
        bitmap = Bitmap.createBitmap(mapInfo.bitmap, 0, 0,
                (int) mapInfo.bitmap.getWidth(), (int) mapInfo.bitmap.getHeight(), matrix, true);

        //数组初始化
        markEnemyMap = new int[mapInfo.height][mapInfo.width][3];
        for (int x = 0; x < markEnemyMap[0].length; x++)
            for (int y = 0; y < markEnemyMap.length; y++)
                for (int c = 0; c < markEnemyMap[0][0].length; c++)
                    markEnemyMap[y][x][c] = -1;

        //数组初始化
        unitMap = new int[mapInfo.height][mapInfo.width][2];
        for (int x = 0; x < unitMap[0].length; x++)
            for (int y = 0; y < unitMap.length; y++)
                for (int c = 0; c < unitMap[0][0].length; c++)
                    unitMap[y][x][c] = -1;
    }

    //地图适配屏幕
    private void init(int screenXSixe, int screenYSize, int mapXGrid, int mapYGrid, int pixelPG) {
        //比较屏幕和地图长和高比例
        float screenXDivY = (float) screenXSixe / screenYSize;
        float mapXDivY = (float) mapXGrid / mapYGrid;
        //限制屏幕最大显示格数
        screenXGrid = screenXSixe / pixelPG;
        screenYGrid = screenYSize / pixelPG;
        //关键参数备份
        mapInfo.pixelPerGrid = pixelPG;
        screenWidth = screenXSixe;
        screenHeight = screenYSize;
        mapInfo.width = mapXGrid;
        mapInfo.height = mapYGrid;
        //屏幕的长高比例大于地图,地图参照屏幕长来缩放
        if (screenXDivY > mapXDivY) {
            //得到屏幕横向实际显示格数
            if (mapXGrid < screenXGrid)
                screenXGrid = mapXGrid;
            //得到屏幕竖向实际显示格数
            screenYGrid = (int) ((float) screenXGrid / screenWidth * screenHeight);
        }
        //其他时候,地图参照屏幕高来缩放
        else {
            //得到屏幕竖向实际显示格数
            if (mapYGrid < screenYGrid)
                screenYGrid = mapYGrid;
            //得到屏幕横向实际显示格数
            screenXGrid = (int) ((float) screenYGrid / screenHeight * screenWidth);
        }
        //横纵向每格像素
        xGridPixel = (float) screenWidth / screenXGrid;
        yGridPixel = (float) screenHeight / screenYGrid;
        //关联参数
        xAnimGridPixel = (int) (xGridPixel * 2);
        yAnimGridPixel = (int) (yGridPixel * 2);
        xAnimOffsetPixel = (int) (-xGridPixel / 2);
        yAnimOffsetPixel = (int) (-yGridPixel);
        //得到地图实际显示大小
        mapWidth = xGridPixel * mapXGrid;
        mapHeight = yGridPixel * mapYGrid;
        //
        xGridErr = yGridErr = 0.0f;
        //
        trap = new Trapezoid(mapInfo.width, mapInfo.height);
    }

    //----- 地图梯形变换 -----

    //中心甜区,用于判断是否需要挪动地图来让选中人物居中(数值单位:格)
    public Rect srcGridCenter = new Rect(0, 0, 0, 0);
    //屏幕在地图上框了一个矩形,然后拉高拉宽上边两个点,框到一个倒梯形区域
    public float[] srcPoint = new float[8];
    //srcPoint的坐标数据按地图的实际分辨率转换之后的位置
    public float[] srcPointBitmap = new float[8];
    //上面框到的倒梯形输出到屏幕的位置
    public float[] distPoint = new float[8];
    //梯形网格信息
    public Trapezoid trap;

    public class Trapezoid{
        //梯形区域包含的格子矩阵
        public int width, height;
        //网格产生的交叉点的坐标
        public float[][][] grid;

        public Trapezoid(int mapXGrid, int mapYGrid){
            //WxH 矩阵网格会产生 (W+1)x(H+1) 个交叉点坐标
            grid = new float[mapYGrid + 1][mapXGrid + 1][2];
            width = mapXGrid;
            height = mapYGrid;
        }

        /*
            刷新梯形信息
         */
        public void refresh(float xPow, float yPow){
            //获取转换后的网格点坐标
            for(int y = 0; y <= height; y++){
                float yOffset = y * yGridPixel * yPow;
                for(int x = 0; x <= width; x++){
                    grid[y][x][0] = x * xGridPixel * xPow;
                    grid[y][x][1] = yOffset;
                    matrix.mapPoints(grid[y][x]);
                }
            }
        }

        /*
            计算格子在屏幕中的位置
         */
        public void getSite(int xGrid, int yGrid, Rect rect, Path path, int edge){
            if(xGrid >= 0 && xGrid < width && yGrid >= 0 && yGrid < height){
                float[] p = new float[]{
                        xGrid * xGridPixel, yGrid * yGridPixel,
                        (xGrid + 1) * xGridPixel, yGrid * yGridPixel,
                        (xGrid + 1) * xGridPixel, (yGrid + 1) * yGridPixel,
                        xGrid * xGridPixel, (yGrid + 1) * yGridPixel
                };
                matrix.mapPoints(p);
                //赋值
                rect.left = (int)p[0];
                rect.top = (int)p[1];
                rect.right = (int)p[4];
                rect.bottom = (int)p[5];
                //赋值
                path.moveTo(p[0] + edge, p[1] + edge);
                path.lineTo(p[2] - edge, p[3] + edge);
                path.lineTo(p[4] - edge, p[5] - edge);
                path.lineTo(p[6] + edge, p[7] - edge);
                path.close();
                return;
            }
            //不在屏幕内
            rect.left = (int)(- xGridPixel * 3);
            rect.top = (int)(- yGridPixel * 3);
            rect.right = rect.left + (int)xGridPixel;
            rect.bottom = rect.top + (int)yGridPixel;
        }

        /*
            计算坐标所在格子
         */
        public int[] getGrid(float x, float y){
            for(int yC = 0; yC < height; yC++){
                if(y > grid[yC][0][1] && y <= grid[yC + 1][0][1]){
                    for(int xC = 0; xC < width; xC++){
                        if(x > grid[yC][xC][0] && x <= grid[yC + 1][xC + 1][0])
                            return new int[]{xC, yC};
                    }
                }
            }
            return new int[]{0, 0};
        }
    }

    //获取梯形转换矩阵,用于绘制
    public void upgradeMatrix() {
        //mapDist为当前地图显示区域 (点顺序左上、左下、右下、右上)
        srcPoint[0] = (- xGridErr) * xGridPixel;
        srcPoint[1] = (- yGridErr) * yGridPixel;
        srcPoint[2] = (- xGridErr) * xGridPixel;
        srcPoint[3] = srcPoint[1] + screenHeight;
        srcPoint[4] = srcPoint[0] + screenWidth;
        srcPoint[5] = srcPoint[1] + screenHeight;
        srcPoint[6] = srcPoint[0] + screenWidth;
        srcPoint[7] = srcPoint[1];
        //梯形左右和上边缩进格数*每格像素 = 缩进像素
        float reduceX = xGridPixel * mapInfo.transferGrid;
        float reduceY = yGridPixel * mapInfo.transferGrid;
        //地图靠近边界时逐渐恢复比例
        if (reduceX > mapWidth - srcPoint[6])//将超出右边界?
            reduceX = mapWidth - srcPoint[6];
        if (reduceX > srcPoint[0])//将超出左边界?
            reduceX = srcPoint[0];
        if (reduceY > srcPoint[1])//将超出上边界?
            reduceY = srcPoint[1];
        if(reduceX < reduceY)//谁小用谁
            reduceY = reduceX;
        else
            reduceX = reduceY;
        //把矩形的左上角、右上角分别往左、右拉开，然后往上拉高，形成倒梯形区域
        srcPoint[0] -= reduceX;//左上角往左拉
        srcPoint[6] += reduceX;//右上角往右拉
        srcPoint[1] -= reduceY;//左上角往上拉
        srcPoint[7] -= reduceY;//右上角往上拉
        //bitmap像素坐标和屏幕像素坐标的比值
        float xPow = (float) bitmap.getWidth() / mapWidth;
        float yPow = (float) bitmap.getHeight() / mapHeight;
        //把刚才算到的倒梯形坐标转换为bitmap上的坐标 (点顺序左上、左下、右下、右上)
        srcPointBitmap[0] = srcPoint[0] * xPow;
        srcPointBitmap[1] = srcPoint[1] * yPow;
        srcPointBitmap[2] = srcPoint[2] * xPow;
        srcPointBitmap[3] = srcPoint[3] * yPow;
        srcPointBitmap[4] = srcPoint[4] * xPow;
        srcPointBitmap[5] = srcPoint[5] * yPow;
        srcPointBitmap[6] = srcPoint[6] * xPow;
        srcPointBitmap[7] = srcPoint[7] * yPow;
        //输出范围为屏幕大小 (点顺序左上、左下、右下、右上)
        distPoint[0] = 0;
        distPoint[1] = 0;
        distPoint[2] = 0;
        distPoint[3] = screenHeight;
        distPoint[4] = screenWidth;
        distPoint[5] = screenHeight;
        distPoint[6] = screenWidth;
        distPoint[7] = 0;
        //梯形变换: 在地图上抠出一块倒梯形区域,然后显示到矩形的屏幕上,就形成了近大远小的显示效果
        matrix.setPolyToPoly(srcPointBitmap, 0, distPoint, 0, 4);

        //更新网格信息
        trap.refresh(xPow, yPow);

        //中心甜区(即没有形变时地图周围缩进3格的区域)
        srcGridCenter.left = Math.round(- xGridErr) + 3;
        srcGridCenter.top = Math.round(- yGridErr) + 3;
        srcGridCenter.right = srcGridCenter.left + screenXGrid - 6 - 1;
        srcGridCenter.bottom = srcGridCenter.top + screenYGrid - 6 - 1;
    }

    //----- 求梯形中的某一格子 -----

    public FeInfoSite selectSite = new FeInfoSite();

    //根据格子求位置
    public void getRectByGrid(int xG, int yG, FeInfoSite site) {
        //默认值
        site.xGrid = xG;
        site.yGrid = yG;
        site.path.reset();
        //求范围
        trap.getSite(xG, yG, site.rect, site.path, 1);
    }

    //根据格子求位置
    public FeInfoSite getRectByGrid(int xG, int yG) {
        FeInfoSite ret = new FeInfoSite();
        getRectByGrid(xG, yG, ret);
        return ret;
    }

    //根据坐标求格子位置
    public void getRectByLocation(float x, float y, FeInfoSite site) {
        int[] xy = trap.getGrid(x, y);
        getRectByGrid(xy[0], xy[1], site);
    }

    //根据坐标求格子位置
    public FeInfoSite getRectByLocation(float x, float y) {
        FeInfoSite ret = new FeInfoSite();
        getRectByLocation(x, y, ret);
        return ret;
    }
}

