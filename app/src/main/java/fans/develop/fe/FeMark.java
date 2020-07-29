package fans.develop.fe;

/*
    移动范围计算
    调用构造函数即可得到 rangeMov(移动范围)、rangeHit(攻击范围)、rangeSpecial(特效范围)、
 */
public class FeMark {

    //可移动范围
    public Range rangeMov;
    //可攻击范围
    public Range rangeHit;
    //特效范围
    public Range rangeSpecial;

    /*
        xGrid, yGrid: 地图上的坐标
        typeProfession: 职业类型
        hit: 攻击距离
        hitSpace: 近身空当(比如弓箭空档为1)
        special: 特效(杖、回复等)范围
     */
    public FeMark(int xGrid, int yGrid, FeInfoMap mapInfo, int mov, int typeProfession, int hit, int hitSpace, int special){
        //范围初始化
        rangeMov = new Range(xGrid, yGrid, mov, mapInfo.width, mapInfo.height);
        rangeMov.cut();
        //递归获得移动范围
        loopRangeMov(rangeMov.xGridCenter, rangeMov.yGridCenter, mapInfo, mov, typeProfession, rangeMov);
        //获得攻击范围
        rangeHit = getRangeHit(rangeMov, hit, hitSpace);
        rangeHit.cut();
        //获得特效范围
        rangeSpecial = getRangeSpecial(rangeMov, special);
        rangeSpecial.cut();
    }

    /*
        往数组 range 的坐标 (xGrid, yGrid) 中投放一个递归点, 每次往上下左右递归检查移动范围
     */
    private void loopRangeMov(int xGrid, int yGrid, FeInfoMap mapInfo, int mov, int typeProfession, Range range){
        //移动力剩余不足?是则结束递归
        if(mov < 0)
            return;
        //当前格子已标记?是则结束递归
        if(range.array[yGrid][xGrid])
            return;
        //填写当前点剩余移动力
        range.array[yGrid][xGrid] = true;
        //获取移动剩余
        mov -= mapInfo.movReduce(xGrid + range.xGridStart, yGrid + range.yGridStart, typeProfession);
        //继续递归
        if(xGrid - 1 >= 0)
            loopRangeMov(xGrid - 1, yGrid, mapInfo, mov, typeProfession, range);
        if(xGrid + 1 < range.width)
            loopRangeMov(xGrid + 1, yGrid, mapInfo, mov, typeProfession, range);
        if(yGrid - 1 >= 0)
            loopRangeMov(xGrid, yGrid - 1, mapInfo, mov, typeProfession, range);
        if(yGrid + 1 < range.height)
            loopRangeMov(xGrid, yGrid + 1, mapInfo, mov, typeProfession, range);
    }

    /*
        小范围无条件递归mark
        count: 递归次数倒计时
        markValue: 标记值
     */
    private void loopMark(int xGrid, int yGrid, Boolean[][] range, int count, Boolean markValue){
        //递归计数用完
        if(count < 0)
            return;
        //该格已经标记过了
        // if(range[yGrid][xGrid] == markValue)
        //     return;
        //标记当前格
        range[yGrid][xGrid] = markValue;
        //计数减
        count -= 1;
        //递归
        loopMark(xGrid - 1, yGrid, range, count, markValue);
        loopMark(xGrid + 1, yGrid, range, count, markValue);
        loopMark(xGrid, yGrid - 1, range, count, markValue);
        loopMark(xGrid, yGrid + 1, range, count, markValue);
    }

    /*
        根据移动范围计算攻击范围
        hit： 攻击距离
        hitSpace: 近身空当
     */
    private Range getRangeHit(Range rangeMov, int hit, int hitSpace){
        Range rangeHit = new Range(rangeMov, hit);
        //哪有这种情况?
        if(hitSpace >= hit || hit < 1)
            return rangeHit;
        //在 rangeMov 矩阵定位的坐标要转到 rangeHit 坐标时加上该值
        int xErr = hit, yErr = hit;
        //站在原地时的攻击范围
        Boolean[][] rangeStayHit = new Boolean[hit * 2 + 1][hit * 2 + 1];
        //画原地不动时的攻击范围
        loopMark(hit, hit, rangeStayHit, hit, true);
        //扣掉 hitSpace 范围
        if(hitSpace > 0)
            loopMark(hit, hit, rangeStayHit, hitSpace, false);
        //遍历 rangeMov 中所有可移动点
        for(int x = 0; x < rangeMov.width; x++){
            for(int y = 0; y < rangeMov.height; y++){
                //是可移动点?
                if(rangeMov.array[y][x]){
                    //用 rangeStayHit 范围覆盖到当前可移动点上
                    for(int xRH = x + xErr - hit, xRSH = 0; xRSH < rangeStayHit.length; xRH++, xRSH++){
                        for(int yRH = y + yErr - hit, yRSH = 0; yRSH < rangeStayHit[0].length; yRH++, yRSH++){
                            //是标记格？
                            if(rangeStayHit[yRSH][xRSH])
                                rangeHit.array[yRH][xRH] = true;
                        }
                    }
                }
            }
        }
        return rangeHit;
    }

    /*
        根据移动范围计算特效范围
        special: 特效距离
     */
    private Range getRangeSpecial(Range rangeMov, int special){
        Range rangeSpecial = new Range(rangeMov, special);
        if(special < 1)
            return rangeSpecial;
        //在 rangeMov 矩阵定位的坐标要转到 rangeSpecial 坐标时加上该值
        int xErr = special, yErr = special;
        //遍历 rangeMov 中所有可移动点
        for(int x = 0; x < rangeMov.width; x++){
            for(int y = 0; y < rangeMov.height; y++){
                //是可移动点?
                if(rangeMov.array[y][x]){
                    //递归绘制 special 范围
                    loopMark(x + xErr, y + yErr, rangeSpecial.array, special, true);
                }
            }
        }
        return rangeSpecial;
    }

    /*
        范围管理类, 可用 getGridInfo() 获得格子在地图位置数组
     */
    public class Range{

        //宽高
        public int width, height;
        //矩阵左上角坐标(相对于地图)
        public int xGridStart, yGridStart;
        //矩阵中心坐标(相对于矩阵自身)
        public int xGridCenter, yGridCenter;
        //输出范围
        public Boolean[][] array;
        //地图范围
        public int mapWidth, mapHeight;

        /*
            xGrid, yGrid: 地图中的坐标
            mov: 移动力
         */
        public Range(int xGrid, int yGrid, int mov, int mapWidth, int mapHeight){
            this.mapWidth = mapWidth;
            this.mapHeight = mapHeight;
            //矩阵宽、高
            width = mov * 2 + 1;
            height = mov * 2 + 1;
            //中心坐标(相对矩阵)
            xGridCenter = mov;
            yGridCenter = mov;
            //左上角坐标(相对地图)
            xGridStart = xGrid - mov;
            yGridStart = yGrid - mov;
            //初始化范围
            array = new Boolean[width][height];
        }

        /*
            腰围扩增
         */
        public Range(Range rangeSrc, int addRad){
            mapWidth = rangeSrc.mapWidth;
            mapHeight = rangeSrc.mapHeight;
            //矩阵宽、高
            width = rangeSrc.width + addRad * 2;
            height = rangeSrc.height + addRad * 2;
            //中心坐标(相对矩阵)
            xGridCenter = rangeSrc.xGridCenter + addRad;
            yGridCenter = rangeSrc.yGridCenter + addRad;
            //左上角坐标(相对地图)
            xGridStart = rangeSrc.xGridStart - addRad;
            yGridStart = rangeSrc.yGridStart - addRad;
            //初始化范围
            array = new Boolean[width][height];
        }

        /*
            统计mark点
         */
        public int sum(){
            int sum = 0;
            for(int x = 0; x < width; x++)
                for(int y = 0; y < height; y++)
                    if(array[y][x])
                        sum += 1;
            return sum;
        }

        /*
            生成 FeInfoSite 数组, 并且计算好在地图中的位置
         */
        public FeInfoSite[] getGridInfo(FeSectionMap sectionMap){
            //没有标记格
            int size = sum();
            if(size < 1)
                return null;
            //生成数组
            FeInfoSite[] gridInfo = new FeInfoSite[size];
            int sizeCount = 0;
            //位置赋值
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    if(array[y][x]){
                        //装填数组
                        gridInfo[sizeCount] = sectionMap.getRectByGrid(x + xGridStart, y + yGridStart);
                        sizeCount += 1;
                        //长度检查
                        if(sizeCount >= size)
                            break;
                    }
                }
                //长度检查
                if(sizeCount >= size)
                    break;
            }
            return gridInfo;
        }

        /*
            裁剪掉超出地图部分
         */
        public void cut(){
            //缓存参数
            int width = this.width;
            int height = this.height;
            int xGridStart = this.xGridStart;
            int yGridStart = this.yGridStart;
            int xGridCenter = this.xGridCenter;
            int yGridCenter = this.yGridCenter;
            //切掉超出屏幕左边沿部分
            if(xGridStart < 0){
                width += xGridStart;
                xGridCenter += xGridStart;
                xGridStart = 0;
            }
            //切掉超出屏幕上边沿部分
            if(yGridStart < 0){
                height += yGridStart;
                yGridCenter += yGridStart;
                yGridStart = 0;
            }
            //切掉超出屏幕右边沿部分
            if(xGridStart >= mapWidth)
                width -= xGridStart - mapWidth + 1;
            //切掉超出屏幕下边沿部分
            if(yGridStart >= mapHeight)
                height -= yGridStart - mapHeight + 1;
            //还有剩余不?
            if(width < 1 || height < 1)
                return;//实在不知道怎么处理...
            //兼容检查
            if(xGridCenter < 0)
                xGridCenter = 0;
            else if(xGridCenter >= width)
                xGridCenter = width - 1;
            if(yGridCenter < 0)
                yGridCenter = 0;
            else if(yGridCenter >= height)
                yGridCenter = height - 1;
            //是否发生了变化？
            if(width != this.width
                || height != this.height
                || xGridStart != this.xGridStart
                || yGridStart != this.yGridStart){
                //重新生成并拷贝
                Boolean[][] array = new Boolean[width][height];
                //这里 array 必然小于 this.array
                for(int xDist = 0, xSrc = this.xGridStart - xGridStart; xDist < width; xDist++, xSrc++)
                    for(int yDist = 0, ySrc = this.yGridStart - yGridStart; yDist < width; xDist++, xSrc++)
                        array[yDist][xDist] = this.array[ySrc][xSrc];
                //参数转移
                this.array = array;
                this.width = width;
                this.height = height;
                this.xGridStart = xGridStart;
                this.yGridStart = yGridStart;
                this.xGridCenter = xGridCenter;
                this.yGridCenter = yGridCenter;
            }
        }
    }
}
