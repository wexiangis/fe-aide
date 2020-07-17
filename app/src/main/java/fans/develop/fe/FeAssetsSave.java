package fans.develop.fe;


/*
    /assets/save 文件夹资源管理器
 */
public class FeAssetsSave {

    private FeAssetsUnit unit;
    private int sXCurrent;//当前档位
    private int saveNum;//总存档槽数量
    private FeFile file;//文件操作工具

    public FeAssetsSave(FeAssetsUnit unit){
        this.unit = unit;
        file = new FeFile();
        //读取最后存档位置
        sXCurrent = FeFormat.StringToInt(file.readFile("/save/last.txt", "00", 2));
        //读取存档槽数量
        saveNum = FeFormat.StringToInt(file.readFile("/save/num.txt", "05", 2));
    }

    /* ---------- 存档槽位检查 ---------- */

    /*
        存档槽数量
     */
    public int saveNum(){
        return saveNum;
    }

    /*
        获取最后存档位置
     */
    public int getLastSx(){
        return sXCurrent;
    }

    /*
        获取所有存档槽状态
        返回: int[saveNum][0], -1/表示空 其它表示章节
            int[saveNum][1], 0/表示非中断状态 1/中断
            int[saveNum][2], 时长,单位秒
     */
    public int[][] getSx(){
        int[][] ret = new int[saveNum][3];
        //遍历saveNum个sX文件夹中的info.txt文件
        for(int i = 0; i < saveNum; i++)
        {
            //拼接路径
            String path = String.format("/save/s%d/info.txt", i);
            //默认值,无存档,非中断状态
            ret[i][0] = -1;
            ret[i][1] = ret[i][2] = 0;
            //文件存在
            if(file.exists(path))
            {
                //读取文件
                String[] line = file.readFile(path, "-1;0;0;", 16).split(";");
                if(line != null){
                    ret[i][0] = FeFormat.StringToInt(line[0]);//得到章节数
                    if(line.length > 1)
                        ret[i][1] = FeFormat.StringToInt(line[1]);//得到是否中断
                    if(line.length > 2)
                        ret[i][2] = FeFormat.StringToInt(line[2]);//得到时长
                }
            }
        }
        return ret;
    }

    /* ---------- 存档槽位操作 ---------- */

    //创建新存档
    public void newSx(int sX){
        String rootPath = String.format("/save/s%d/", sX);
        //删空档位
        file.delete(rootPath);
        //创建档位,章节0,非中断状态
        file.writeFile(rootPath, "info.txt", "0;0;0;");
        //更新最后存档位置
        sXCurrent = sX;
        file.writeFile("/save/", "last.txt", String.format("%02d", sXCurrent));
    }

    //删除存档
    public void delSx(int sX){
        file.delete(String.format("/save/s%d", sX));
    }

    //复制存档sXSrc到sXDist
    public void copySx(int sXDist, int sXSrc){
        file.copy(String.format("/save/s%d", sXDist), String.format("/save/s%d", sXSrc));
    }

    /* ---------- 存档加载和保存 ---------- */

    //通关后存档
    public void saveSx(FeAssetsSX sX, int nextSection){
        ;
    }
    
    //加载存档
    public FeAssetsSX loadSx(int sX){
        FeAssetsSX ret = new FeAssetsSX(unit, sX);
        ret.init();
        return ret;
    }

    //继续中断中的存档
    public FeAssetsSX recoverSx(int sX){
        FeAssetsSX ret = new FeAssetsSX(unit, sX);
        ret.recover();
        return ret;
    }
}
