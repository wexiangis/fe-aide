package fans.develop.fe;

import android.graphics.Bitmap;

/*
    unit 单元信息汇总,该类存放在FeViewUnit,由界面管理
 */
public class FeUnit{
    
    private FeAssets assets;
    private FeAssetsUnit assetsUnit;
    private FeAssetsSX assetsSX;
    
    public FeAssetsSaveCache.CampUnit campUnit;

    public FeUnit(FeAssets assets, FeAssetsSX sX, int order){
        this.assets = assets;
        this.assetsUnit = assets.unit;
        this.assetsSX = sX;
        this.campUnit = sX.saveCache.getCampUnit(order);
    }

    //不可修改的参数
    public int order(){
        return campUnit.order();
    }
    public FeTypeCamp camp(){
        return FeTypeCamp.values()[campUnit.camp()];
    }
    public int id(){
        return assetsSX.saveCache.unit.getId(campUnit.order());
    }
    public int professionType(){
        return assetsUnit.getProfessionType(id());
    }
    //失能/使能/保存
    public void on(){
        assetsSX.saveCache.unit.setDisable(campUnit.order(), 0);
    }
    public void off(){
        assetsSX.saveCache.unit.setDisable(campUnit.order(), 1);
    }
    public void save(){
        assetsSX.saveCache.unit.save();
        campUnit.save();
    }
    //位置参数
    public void x(int x){
        assetsSX.saveCache.unit.setX(campUnit.order(), x);
    }
    public int x(){
        return assetsSX.saveCache.unit.getX(campUnit.order());
    }
    public void y(int y){
        assetsSX.saveCache.unit.setY(campUnit.order(), y);
    }
    public int y(){
        return assetsSX.saveCache.unit.getY(campUnit.order());
    }
    //基本信息
    public String getName(){
        return assetsUnit.getName(id());
    }
    public String getSummary(){
        return assetsUnit.getSummary(id());
    }
    public Bitmap getHead(){
        return assetsUnit.getHead(id());
    }
    public String getProfessionName(){
        return assetsUnit.getProfessionName(id());
    }
    public String getProfessionSummary(){
        return assetsUnit.getProfessionSummary(id());
    }
    public Bitmap getProfessionAnim(){
        return assetsUnit.getProfessionAnim(id());
    }
    // 状态
    public void standby(Boolean b){
        campUnit.setStandby(b ? 1 : 0);
    }
    public Boolean standby(){
        return campUnit.getStandby() > 0 ? true : false;
    }
    public void state(int s){
        campUnit.setState(s);
    }
    public int state(){
        return campUnit.getState();
    }
    public void levelUp(){
        campUnit.setLevel(campUnit.getLevel() + 1);
    }
    public int level(){
        return campUnit.getLevel();
    }
    public void exp(int exp){
        campUnit.setExp(exp);
    }
    public int exp(){
        return campUnit.getExp();
    }
    // ability
    public int[] ability(){
        return campUnit.getLineInt(1);
    }
    public int hp(){
        return campUnit.getHp();
    }
    public int str(){
        return campUnit.getStr();
    }
    public int mag(){
        return campUnit.getMag();
    }
    public int skill(){
        return campUnit.getSkill();
    }
    public int spe(){
        return campUnit.getSpe();
    }
    public int luk(){
        return campUnit.getLuk();
    }
    public int def(){
        return campUnit.getDef();
    }
    public int mde(){
        return campUnit.getMde();
    }
    public int weig(){
        return campUnit.getWeig();
    }
    public int mov(){
        return campUnit.getMov();
    }
    // add
    public int[] add(){
        return campUnit.getLineInt(2);
    }
    public int addHp(){
        return campUnit.getAddHp();
    }
    public int addStr(){
        return campUnit.getAddStr();
    }
    public int addMag(){
        return campUnit.getAddMag();
    }
    public int addSkill(){
        return campUnit.getAddSkill();
    }
    public int addSpe(){
        return campUnit.getAddSpe();
    }
    public int addLuk(){
        return campUnit.getAddLuk();
    }
    public int addDef(){
        return campUnit.getAddDef();
    }
    public int addMde(){
        return campUnit.getAddMde();
    }
    public int addWeig(){
        return campUnit.getAddWeig();
    }
    public int addMov(){
        return campUnit.getAddMov();
    }
    // skillLevel
    public int[] skillLevel(){
        return campUnit.getLineInt(3);
    }
    public int sward(){
        return campUnit.getSward();
    }
    public int gun(){
        return campUnit.getGun();
    }
    public int axe(){
        return campUnit.getAxe();
    }
    public int arrow(){
        return campUnit.getArrow();
    }
    public int phy(){
        return campUnit.getPhy();
    }
    public int light(){
        return campUnit.getLight();
    }
    public int dark(){
        return campUnit.getDark();
    }
    public int stick(){
        return campUnit.getStick();
    }
    // upgrade
    public int[] upgrade(){
        return assetsUnit.getProfessionUpgrade(id());
    }
    public int upgradeHp(){
        return assetsUnit.getProfessionUpgradeHp(id());
    }
    public int upgradeStr(){
        return assetsUnit.getProfessionUpgradeStr(id());
    }
    public int upgradeMag(){
        return assetsUnit.getProfessionUpgradeMag(id());
    }
    public int upgradeSkill(){
        return assetsUnit.getProfessionUpgradeSkill(id());
    }
    public int upgradeSpe(){
        return assetsUnit.getProfessionUpgradeSpe(id());
    }
    public int upgradeLuk(){
        return assetsUnit.getProfessionUpgradeLuk(id());
    }
    public int upgradeDef(){
        return assetsUnit.getProfessionUpgradeDef(id());
    }
    public int upgradeMde(){
        return assetsUnit.getProfessionUpgradeMde(id());
    }
    public int upgradeWeig(){
        return assetsUnit.getProfessionUpgradeWeig(id());
    }
    public int upgradeMov(){
        return assetsUnit.getProfessionUpgradeMov(id());
    }
    // grow
    public int[] grow(){
        return assetsUnit.getProfessionGrow(id());
    }
    public int growHp(){
        return assetsUnit.getProfessionGrowHp(id());
    }
    public int growStr(){
        return assetsUnit.getProfessionGrowStr(id());
    }
    public int growMag(){
        return assetsUnit.getProfessionGrowMag(id());
    }
    public int growSkill(){
        return assetsUnit.getProfessionGrowSkill(id());
    }
    public int growSpe(){
        return assetsUnit.getProfessionGrowSpe(id());
    }
    public int growLuk(){
        return assetsUnit.getProfessionGrowLuk(id());
    }
    public int growDef(){
        return assetsUnit.getProfessionGrowDef(id());
    }
    public int growMde(){
        return assetsUnit.getProfessionGrowMde(id());
    }
    public int growWeig(){
        return assetsUnit.getProfessionGrowWeig(id());
    }
    public int growMov(){
        return assetsUnit.getProfessionGrowMov(id());
    }
    // special
    public int[] special(){
        return campUnit.getLineInt(5);
    }
    public int special1(){
        return campUnit.getSpe1();
    }
    public int special2(){
        return campUnit.getSpe2();
    }
    public int special3(){
        return campUnit.getSpe3();
    }
    public int special4(){
        return campUnit.getSpe4();
    }
    public int getLevel(){
        return campUnit.getLevel();
    }
    // items
    public int[] item(){
        return campUnit.getLineInt(4);
    }
    public int item1(){
        return campUnit.getIt1();
    }
    public int item2(){
        return campUnit.getIt2();
    }
    public int item3(){
        return campUnit.getIt3();
    }
    public int item4(){
        return campUnit.getIt4();
    }
    public int item5(){
        return campUnit.getIt5();
    }
    public int item6(){
        return campUnit.getIt6();
    }
    public int equip(){
        return campUnit.getEquip();
    }
}