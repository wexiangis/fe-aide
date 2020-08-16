package fans.develop.fe;

import android.graphics.Bitmap;
import android.graphics.Matrix;
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
    //地图实际显示区域
    public Rect mapDist = null;
    //地图左上角偏移格子数,一般都为负值
    public float xGridErr = 0.0f, yGridErr = 0.0f;
		public float xGridErrRed = 0.0f, yGridErrRed = 0.0f;
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
        init(screenWidth, screenHeight, mapInfo.width, mapInfo.height, mapInfo.pixelPerGrid);
        //xp、yp分别为xy缩放比例(原始长、高缩放到实际显示长、高)
        float xp = (float)width/mapInfo.bitmap.getWidth();
        float yp = (float)height/mapInfo.bitmap.getHeight();
        //用缩放后的矩阵初始化bitmap,缩放效果较好
        matrix.postScale(xp, yp);
        bitmap = Bitmap.createBitmap(mapInfo.bitmap, 0, 0,
                (int)mapInfo.bitmap.getWidth(), (int)mapInfo.bitmap.getHeight(), matrix, true);

        //数组初始化
        markEnemyMap = new int[mapInfo.height][mapInfo.width][3];
        for(int x = 0; x < markEnemyMap[0].length; x++)
            for(int y = 0; y < markEnemyMap.length; y++)
                for(int c = 0; c < markEnemyMap[0][0].length; c++)
                    markEnemyMap[y][x][c] = -1;

        //数组初始化
        unitMap = new int[mapInfo.height][mapInfo.width][2];
        for(int x = 0; x < unitMap[0].length; x++)
            for(int y = 0; y < unitMap.length; y++)
                for(int c = 0; c < unitMap[0][0].length; c++)
                    unitMap[y][x][c] = -1;
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
        mapInfo.width = mapXGrid;
        mapInfo.height = mapYGrid;
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
                mapDist.left = (int)((int)((float)mapDist.left/width*mapInfo.width)*xGridPixel);
            }
            mapDist.right = width - mapDist.left;
            //
            if(mapDist.top + height < screenHeight)
                mapDist.top = screenHeight - height;
            else{
                mapDist.top -= (height - (mapDist.bottom - mapDist.top))/2;
                mapDist.top = (int)((int)((float)mapDist.top/height*mapInfo.height)*yGridPixel);
            }
            mapDist.bottom = height - mapDist.top;
        }
        //
        xGridErr = yGridErr = 0.0f;
    }

    //----- 地图梯形变换 -----

		//梯形区域网格信息
    public class TrapeaoidGrid{

        private float[][] grid;
        //行高
        public void ySize(int line, float value){
            grid[line][0] = value;
        }
        public float ySize(int line){
            return grid[line][0];
        }
        //行总高(相对于屏幕上边的y值)
        public void ySizeTotal(int line, float value){
            grid[line][1] = value;
        }
        public float ySizeTotal(int line){
            return grid[line][1];
        }
        //横向左边offset
        public void xOffset(int line, float value){
            grid[line][2] = value;
        }
        public float xOffset(int line){
            return grid[line][2];
        }
        //横向平均宽
        public void xSize(int line, float value){
            grid[line][3] = value;
        }
        public float xSize(int line){
            return grid[line][3];
        }

        public TrapeaoidGrid(int height){
            grid = new float[height][4];
            for(int y = 0; y < grid.length; y++)
                for(int x = 0; x < grid[0].length; x++)
                    grid[y][x] = 0.0f;
        }
    }

    //梯形缩进的格数和像素
    public int reduceGrid = 0;
    public float reduce = 0;
    //梯形区域里的方格状态: 横纵向格数, 起始格子
    public int srcGridX = 0, srcGridY = 0, srcGridXStart = 0, srcGridYStart = 0;
    //中心甜区,用于判断是否需要挪动地图来让选中人物居中
    public Rect srcGridCenter = new Rect(0,0,0,0);
    //梯形转换后,屏幕中的网格信息(一屏最多显示72行)
    public TrapeaoidGrid trapGrid = new TrapeaoidGrid(72);
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
        //mapDist为当前地图显示区域
        srcPoint[0] = -mapDist.left;
        srcPoint[1] = -mapDist.top;
        srcPoint[2] = -mapDist.left;
        srcPoint[3] = -mapDist.top + screenHeight;
        srcPoint[4] = -mapDist.left + screenWidth;
        srcPoint[5] = -mapDist.top + screenHeight;
        srcPoint[6] = -mapDist.left + screenWidth;
        srcPoint[7] = -mapDist.top;
        //梯形左右和上边缩进格数*每格像素 = 缩进像素
        reduce = xGridPixel*transferGrid;
        //地图靠近边界时逐渐恢复比例
        if(reduce > width - srcPoint[6])
            reduce = width - srcPoint[6];
        if(reduce > srcPoint[0])
            reduce = srcPoint[0];
        if(reduce > srcPoint[1])
            reduce = srcPoint[1];
        //把矩形的上边左右各拉宽reduce, 同时往上拉高reduce, 变成倒梯形
        srcPoint[0] -= reduce;
        srcPoint[6] += reduce;
        srcPoint[1] -= reduce;
        srcPoint[7] -= reduce;
        //防止梯形出屏
        // if(srcPoint[0] < 0) srcPoint[0] = 0;
        // if(srcPoint[6] > width) srcPoint[6] = width;
        // if(srcPoint[1] < 0) srcPoint[1] = 0;
        // if(srcPoint[7] < 0) srcPoint[7] = 0;
        //bitmap像素坐标和屏幕像素坐标的比值
        float xPow = (float)bitmap.getWidth()/width;
        float yPow = (float)bitmap.getHeight()/height;
        //把刚才算到的倒梯形坐标转换为bitmap上的坐标
        srcPointBitmap[0] = srcPoint[0]*xPow;
        srcPointBitmap[1] = srcPoint[1]*yPow;
        srcPointBitmap[2] = srcPoint[2]*xPow;
        srcPointBitmap[3] = srcPoint[3]*yPow;
        srcPointBitmap[4] = srcPoint[4]*xPow;
        srcPointBitmap[5] = srcPoint[5]*yPow;
        srcPointBitmap[6] = srcPoint[6]*xPow;
        srcPointBitmap[7] = srcPoint[7]*yPow;
        //输出范围为屏幕大小
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

        //关键参数提取
        reduceGrid = Math.round(reduce/xGridPixel);//实际缩进格子数
        srcGridX = reduceGrid*2 + screenXGrid;//梯形区域内包含列数
        srcGridY = reduceGrid + screenYGrid;//梯形区域内包含行数
        srcGridXStart = (int)Math.floor(srcPoint[0]/xGridPixel);
        srcGridYStart = (int)Math.floor(srcPoint[1]/yGridPixel);
				//srcGridXStart = - (int)(Math.floor(xGridErr) + reduceGrid*2);
				//srcGridYStart = - (int)(Math.floor(yGridErr) + reduceGrid);
				xGridErrRed = srcPoint[0]/xGridPixel%1;
				yGridErrRed = srcPoint[1]/yGridPixel%1;

        //中心甜区
        srcGridCenter.left = srcGridXStart + reduceGrid + 3;
        srcGridCenter.top = srcGridYStart + reduceGrid + 3;
        srcGridCenter.right = srcGridCenter.left + (screenXGrid - 6) - 1;
        srcGridCenter.bottom = srcGridCenter.top + (screenYGrid - 6) - 1;

        //第一行的高, 总高, 横向offset, 平均宽
        trapGrid.ySize(0, yGridPixel - reduce*1.5f / srcGridY - reduceGrid * 3.0f);
        trapGrid.ySizeTotal(0, trapGrid.ySize(0));
        trapGrid.xOffset(0, trapGrid.ySizeTotal(0) / screenHeight * reduce);
        trapGrid.xSize(0, (trapGrid.xOffset(0) * 2 + screenWidth) / srcGridX);
        //最后一行的高, 总高, 横向offset, 平均宽
        trapGrid.ySize(srcGridY - 1, yGridPixel + reduceGrid);
        trapGrid.ySizeTotal(srcGridY - 1, screenHeight);
        trapGrid.xOffset(srcGridY - 1, trapGrid.ySizeTotal(srcGridY - 1) / screenHeight * reduce);
        trapGrid.xSize(srcGridY - 1, (trapGrid.xOffset(srcGridY - 1) * 2 + screenWidth) / srcGridX);
        //最后一行和第一行高的差值
        float ySErr = trapGrid.ySize(srcGridY - 1) * srcGridY - screenHeight;
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
        for(int i = srcGridY - 2; i >= 0; i--) {
            trapGrid.ySize(i, trapGrid.ySize(srcGridY - 1) - m[srcGridY - 1 - i] / n * ySErr);
            trapGrid.ySizeTotal(i, trapGrid.ySizeTotal(i + 1) - trapGrid.ySize(i + 1));
            trapGrid.xOffset(i, trapGrid.ySizeTotal(i) / screenHeight * reduce);
            trapGrid.xSize(i, (trapGrid.xOffset(i) * 2 + screenWidth) / srcGridX);
        }
    }

    //----- 求梯形中的某一格子 -----

    public FeInfoSite selectSite = new FeInfoSite();

    //输入格子求位置
    public void getRectByGrid(int xG, int yG, FeInfoSite fig){
        //多边形路径缩进(显示移动范围的网格相邻缝隙大小)
        int edge = 1;
        //默认值
        fig.xGrid = xG;
        fig.yGrid = yG;
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
						//偏移矫正,零头部分
						int xC = - (int)(xGridErrRed * trapGrid.xSize(y));
						int yC = - (int)(yGridErrRed * trapGrid.ySize(y));
            //取矩阵
            fig.rect.top = (int)(trapGrid.ySizeTotal(y) - trapGrid.ySize(y)) + yC;
            fig.rect.bottom = (int)trapGrid.ySizeTotal(y) + yC;
            fig.rect.left = (int)(x * trapGrid.xSize(y) - trapGrid.xOffset(y)) + xC;
            fig.rect.right = (int)(fig.rect.left + trapGrid.xSize(y));
            //取多边形路径
            if(y == 0){
                fig.path.moveTo(x * screenWidth / srcGridX + edge + xC, 0 + edge + yC);
                fig.path.lineTo((x + 1) * screenWidth / srcGridX - edge + xC, 0 + edge + yC);
            }else{
                fig.path.moveTo(
                        x * trapGrid.xSize(y - 1) - trapGrid.xOffset(y - 1) + edge + xC,
                        trapGrid.ySizeTotal(y - 1) + edge + yC);
                fig.path.lineTo(
                        (x + 1) * trapGrid.xSize(y - 1) - trapGrid.xOffset(y - 1) - edge + xC,
                        trapGrid.ySizeTotal(y - 1) + edge + yC);
            }
            fig.path.lineTo(fig.rect.right - 1 + xC, fig.rect.bottom - edge + yC);
            fig.path.lineTo(fig.rect.left + 1 + xC, fig.rect.bottom - edge + yC);
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

    public FeInfoSite getRectByGrid(int xG, int yG){
        FeInfoSite ret = new FeInfoSite();
        getRectByGrid(xG, yG, ret);
        return ret;
    }

    //输入坐标求格子位置
    public void getRectByLocation(float x, float y, FeInfoSite fig) {
        for(int yCount = 0; yCount < srcGridY; yCount++){
            if(y < trapGrid.ySizeTotal(yCount)){
                fig.yGrid = yCount + srcGridYStart;
                fig.xGrid = (int)((x + trapGrid.xOffset(yCount)) / trapGrid.xSize(yCount)) + srcGridXStart;
                //
                getRectByGrid(fig.xGrid, fig.yGrid, fig);
                return;
            }
        }
        getRectByGrid(-1, -1 + srcGridXStart, fig);
    }

    public FeInfoSite getRectByLocation(float x, float y){
        FeInfoSite ret = new FeInfoSite();
        getRectByLocation(x, y, ret);
        return ret;
    }
}

