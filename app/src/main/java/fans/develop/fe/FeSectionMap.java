package fans.develop.fe;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;


/*
    加载地图、地图移动和变形后所产生的一系列关键参数, 各个
    图层在绘图时获取某个方格位置、方格和地图长宽都从这里来.
 */
public class FeSectionMap {

    public int section;

    public Matrix matrix = new Matrix();

    public FeInfoMap mapInfo;

    public Bitmap bitmap = null;

    //----- 地图基本信息 -----

    //屏幕实际宽高
    public int screenWidth = 1920, screenHeight = 1080;

    //地图实际显示区域
    public Rect mapDist = null;
    //地图移动格子数
    public int xGridErr = 0, yGridErr = 0;

    //地图实际显示宽高像素
    public int width = 1920, height = 1080;
    //屏幕横纵向格数
    public int screenXGrid = 20, screenYGrid = 10;

    //横纵向每格像素
    public float xGridPixel = 96, yGridPixel = 108;
    //横纵向动画方块像素大小
    public int xAnimGridPixel = 192, yAnimGridPixel = 216;
    //横纵向动画初始偏移
    public int xAnimOffsetPixel = 48, yAnimOffsetPixel = 54;

    public FeSectionMap(int section, FeInfoMap mapInfo, int screenWidth, int screenHeight)
    {
        this.section = section;
        this.mapInfo = mapInfo;
        //屏幕长、高格子数适配屏幕分辨率,得到地图实际显示长高(width、height)
        init(screenWidth, screenHeight, mapInfo.xGrid, mapInfo.yGrid, mapInfo.pixelPerGrid);
        //xp、yp分别为xy缩放比例(原始长、高缩放到实际显示长、高)
        float xp = (float)width/mapInfo.bitmap.getWidth();
        float yp = (float)height/mapInfo.bitmap.getHeight();
        //用缩放后的矩阵初始化bitmap,缩放效果较好
        matrix.postScale(xp, yp);
        bitmap = Bitmap.createBitmap(mapInfo.bitmap, 0, 0,
                (int)mapInfo.bitmap.getWidth(), (int)mapInfo.bitmap.getHeight(), matrix, true);
    }

    //地图适配屏幕
    private void init(int screenXSixe, int screenYSize, int mapXGrid, int mapYGrid, int pixelPG){
        //比较屏幕和地图长和高比例
        float screenXDivY = (float)screenXSixe/screenYSize;
        float mapXDivY = (float)mapXGrid/mapYGrid;
        //限制屏幕最大显示格数
        screenXGrid = screenXSixe/pixelPG;
        screenYGrid = screenYSize/pixelPG;
        //关键参数备份
        mapInfo.pixelPerGrid = pixelPG;
        screenWidth = screenXSixe;
        screenHeight = screenYSize;
        mapInfo.xGrid = mapXGrid;
        mapInfo.yGrid = mapYGrid;
        //屏幕的长高比例大于地图,地图参照屏幕长来缩放
        if(screenXDivY > mapXDivY){
            //得到屏幕横向实际显示格数
            if(mapXGrid < screenXGrid)
                screenXGrid = mapXGrid;
            //得到屏幕竖向实际显示格数
            screenYGrid = (int)((float)screenXGrid/screenWidth*screenHeight);
        }
        //其他时候,地图参照屏幕高来缩放
        else{
            //得到屏幕竖向实际显示格数
            if(mapYGrid < screenYGrid)
                screenYGrid = mapYGrid;
            //得到屏幕横向实际显示格数
            screenXGrid = (int)((float)screenYGrid/screenHeight*screenWidth);
        }
        //横纵向每格像素
        xGridPixel = (float)screenWidth/screenXGrid;
        yGridPixel = (float)screenHeight/screenYGrid;
        //关联参数
        xAnimGridPixel = (int)(xGridPixel*2);
        yAnimGridPixel = (int)(yGridPixel*2);
        xAnimOffsetPixel = (int)(-xGridPixel/2);
        yAnimOffsetPixel = (int)(-yGridPixel);
        //得到地图实际显示大小
        width = (int)(xGridPixel*mapXGrid);
        height = (int)(yGridPixel*mapYGrid);
        //和原大小进行比较后中心缩放
        if(mapDist == null)
            mapDist = new Rect(0, 0, width, height);
        else{
            if(mapDist.left + width < screenWidth)
                mapDist.left = screenWidth - width;
            else{
                mapDist.left -= (width - (mapDist.right - mapDist.left))/2;
                mapDist.left = (int)((int)((float)mapDist.left/width*mapInfo.xGrid)*xGridPixel);
            }
            mapDist.right = width - mapDist.left;
            //
            if(mapDist.top + height < screenHeight)
                mapDist.top = screenHeight - height;
            else{
                mapDist.top -= (height - (mapDist.bottom - mapDist.top))/2;
                mapDist.top = (int)((int)((float)mapDist.top/height*mapInfo.yGrid)*yGridPixel);
            }
            mapDist.bottom = height - mapDist.top;
        }
        //
        xGridErr = yGridErr = 0;
    }

    //----- 地图梯形变换 -----

    //梯形缩进的格数和像素
    public int reduceGrid = 0;
    public float reduce = 0;
    //梯形区域里的方格状态: 横纵向格数, 起始格子
    public int srcGridX = 0, srcGridY = 0, srcGridXStart = 0, srcGridYStart = 0;
    //中心甜区,用于判断是否需要挪动地图来让选中人物居中
    public Rect srcGridCenter = new Rect(0,0,0,0);
    //[n][0]:行高, [n][1]:总行高, [n][2]:横向offset, [n][3]:平均宽
    public float[][] srcGridLine = new float[64][4];
    //屏幕在地图上框了一个矩形,然后拉高拉宽上边两个点,框到一个倒梯形区域
    public float[] srcPoint = new float[8];
    //srcPoint的坐标数据按地图的实际分辨率转换之后的位置
    public float[] srcPointBitmap = new float[8];
    //上面框到的倒梯形输出到屏幕的位置
    public float[] distPoint = new float[8];

    //梯形变换缩进格数
    private int transferGrid = 4;

    //获取梯形转换矩阵,用于绘制
    public void upgradeMatrix(){
        srcPoint[0] = -mapDist.left;
        srcPoint[1] = -mapDist.top;
        srcPoint[2] = -mapDist.left;
        srcPoint[3] = -mapDist.top + screenHeight;
        srcPoint[4] = -mapDist.left + screenWidth;
        srcPoint[5] = -mapDist.top + screenHeight;
        srcPoint[6] = -mapDist.left + screenWidth;
        srcPoint[7] = -mapDist.top;
        //梯形左右和上边缩进格数
        reduce = xGridPixel*transferGrid;
        //地图靠近边界时逐渐恢复比例
        if(reduce > width - srcPoint[6])
            reduce = width - srcPoint[6];
        if(reduce > srcPoint[0])
            reduce = srcPoint[0];
        if(reduce > srcPoint[1])
            reduce = srcPoint[1];
        //梯形变换
        srcPoint[0] -= reduce;
        srcPoint[6] += reduce;
        srcPoint[1] -= reduce;
        srcPoint[7] -= reduce;
        //防止梯形出屏
//        if(srcPoint[0] < 0) srcPoint[0] = 0;
//        if(srcPoint[6] > width) srcPoint[6] = width;
//        if(srcPoint[1] < 0) srcPoint[1] = 0;
//        if(srcPoint[7] < 0) srcPoint[7] = 0;
        //
        float xPow = (float)bitmap.getWidth()/width;
        float yPow = (float)bitmap.getHeight()/height;
        //
        srcPointBitmap[0] = srcPoint[0]*xPow;
        srcPointBitmap[1] = srcPoint[1]*yPow;
        srcPointBitmap[2] = srcPoint[2]*xPow;
        srcPointBitmap[3] = srcPoint[3]*yPow;
        srcPointBitmap[4] = srcPoint[4]*xPow;
        srcPointBitmap[5] = srcPoint[5]*yPow;
        srcPointBitmap[6] = srcPoint[6]*xPow;
        srcPointBitmap[7] = srcPoint[7]*yPow;
        //
        distPoint[0] = 0;
        distPoint[1] = 0;
        distPoint[2] = 0;
        distPoint[3] = screenHeight;
        distPoint[4] = screenWidth;
        distPoint[5] = screenHeight;
        distPoint[6] = screenWidth;
        distPoint[7] = 0;
        //梯形变换
        matrix.setPolyToPoly(srcPointBitmap, 0, distPoint, 0, 4);

        //关键参数提取
        reduceGrid = Math.round(reduce/xGridPixel);
        srcGridX = reduceGrid*2 + screenXGrid;
        srcGridY = reduceGrid + screenYGrid;
        srcGridXStart = Math.round(srcPoint[0]/xGridPixel);
        srcGridYStart = Math.round(srcPoint[1]/yGridPixel);

        //中心甜区
        srcGridCenter.left = srcGridXStart + reduceGrid + 3;
        srcGridCenter.top = srcGridYStart + reduceGrid + 3;
        srcGridCenter.right = srcGridCenter.left + (screenXGrid - 6) - 1;
        srcGridCenter.bottom = srcGridCenter.top + (screenYGrid - 6) - 1;

        //第一行的高, 总高, 横向offset, 平均宽
        srcGridLine[0][0] = yGridPixel - reduce*1.5f/srcGridY - reduceGrid*3.0f;
        srcGridLine[0][1] = srcGridLine[0][0];
        srcGridLine[0][2] = srcGridLine[0][1]/screenHeight*reduce;
        srcGridLine[0][3] = (srcGridLine[0][2]*2 + screenWidth)/srcGridX;
        //最后一行的高, 总高, 横向offset, 平均宽
        srcGridLine[srcGridY-1][0] = yGridPixel + reduceGrid;
        srcGridLine[srcGridY-1][1] = screenHeight;
        srcGridLine[srcGridY-1][2] = srcGridLine[srcGridY-1][1]/screenHeight*reduce;
        srcGridLine[srcGridY-1][3] = (srcGridLine[srcGridY-1][2]*2 + screenWidth)/srcGridX;
        //最后一行和第一行高的差值
        float ySErr = srcGridLine[srcGridY-1][0]*srcGridY - screenHeight;
        float n = 0;//分母
        float[] m = new float[srcGridY];//分子
        //把分子数组累加到分母n
        float basePoint = 10000;
        float basePointCount = 0;
        float basePointPlusBase = 0.99f;
        for(int i = 0; i < srcGridY; i++) {
            basePointCount += basePoint;
            m[i] = basePointCount;
            n += m[i];
            //
            if(reduceGrid > 0) {
                basePoint *= basePointPlusBase;
                basePointPlusBase *= 0.99f;
            }
        }
        //统计每行信息
        for(int i = srcGridY-2; i >= 0; i--) {
            srcGridLine[i][0] = srcGridLine[srcGridY-1][0] - m[srcGridY-1-i]/n*ySErr;
            srcGridLine[i][1] = srcGridLine[i+1][1] - srcGridLine[i+1][0];
            srcGridLine[i][2] = srcGridLine[i][1]/screenHeight*reduce;
            srcGridLine[i][3] = (srcGridLine[i][2]*2 + screenWidth)/srcGridX;
        }
    }

    //----- 求梯形中的某一格子 -----

    public FeInfoGrid selectSite = new FeInfoGrid();

    //输入格子求位置
    public void getRectByGrid(int xG, int yG, FeInfoGrid fig){
        //多边形路径缩进(显示移动范围的网格相邻缝隙大小)
        int edge = 1;
        //默认值
        fig.point[0] = xG;
        fig.point[1] = yG;
        fig.path.reset();
        //在屏幕范围内
        if(xG >= srcGridXStart &&
                xG < srcGridXStart + srcGridX &&
                yG >= srcGridYStart &&
                yG < srcGridYStart + srcGridY)
        {
            //屏幕棋盘格子所在坐标
            int x = xG - srcGridXStart;
            int y = yG - srcGridYStart;
            //取矩阵
            fig.rect.top = (int)(srcGridLine[y][1] - srcGridLine[y][0]);
            fig.rect.bottom = (int)(srcGridLine[y][1]);
            fig.rect.left = (int)(x*srcGridLine[y][3] - srcGridLine[y][2]);
            fig.rect.right = (int)(fig.rect.left + srcGridLine[y][3]);
            //取多边形路径
            if(y == 0){
                fig.path.moveTo(x * screenWidth / srcGridX + edge, 0 + edge);
                fig.path.lineTo((x + 1) * screenWidth / srcGridX - edge, 0 + edge);
            }else{
                fig.path.moveTo(
                        x * srcGridLine[y-1][3] - srcGridLine[y-1][2] + edge,
                        srcGridLine[y-1][1] + edge);
                fig.path.lineTo(
                        (x + 1) * srcGridLine[y-1][3] - srcGridLine[y-1][2] - edge,
                        srcGridLine[y-1][1] + edge);
            }
            fig.path.lineTo(fig.rect.right - 1, fig.rect.bottom - edge);
            fig.path.lineTo(fig.rect.left + 1, fig.rect.bottom - edge);
            fig.path.close();
        }
        //不在屏幕范围内
        else{
            fig.rect.left = (int)(-xGridPixel)*2;
            fig.rect.right = (int)(-xGridPixel);
            fig.rect.top = (int)(-yGridPixel)*2;
            fig.rect.bottom = (int)(-yGridPixel);
        }
    }

    //输入坐标求格子位置
    public void getRectByLocation(float x, float y, FeInfoGrid fig) {
        for(int yCount = 0; yCount < srcGridY; yCount++){
            if(y < srcGridLine[yCount][1]){
                fig.point[1] = yCount + srcGridYStart;
                fig.point[0] = (int)((x + srcGridLine[yCount][2])/srcGridLine[yCount][3]) + srcGridXStart;
                //
                getRectByGrid(fig.point[0], fig.point[1], fig);
                return;
            }
        }
        getRectByGrid(-1, -1 + srcGridXStart, fig);
    }
}

