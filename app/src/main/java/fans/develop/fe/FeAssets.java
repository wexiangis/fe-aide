package fans.develop.fe;

/*
    assets资源管理器, 所有assets下的资源都汇总到这里
 */
public class FeAssets {

    //各个文件夹
    public FeAssetsMap map;
    public FeAssetsMenu menu;
    public FeAssetsParam param;
    public FeAssetsUnit unit;
    public FeAssetsSave save;
    public FeAssetsEvent event;

    public FeAssets(){
        map = new FeAssetsMap();
        menu = new FeAssetsMenu();
        param = new FeAssetsParam();
        unit = new FeAssetsUnit();
        save = new FeAssetsSave(unit);
        event = new FeAssetsEvent();
    }

}




