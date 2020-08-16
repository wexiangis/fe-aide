package fans.develop.fe;

/*
    /assets/map 文件夹资源管理器
 */
public class FeAssetsMap {

    private FeReaderMap mapReader = new FeReaderMap();

    public FeInfoMap getMap(int section) {
        FeInfoMap mapInfo = new FeInfoMap(section);
        mapReader.getFeMapInfo(mapInfo, 99);
        return mapInfo;
    }
}
