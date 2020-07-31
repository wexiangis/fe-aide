package fans.develop.fe;

import android.graphics.Bitmap;

/*
    unit 单元信息汇总,该类存放在FeViewUnit,由界面管理
 */
public class FeUnit{
    
    private FeAssets assets;
    private FeAssetsUnit assetsUnit;
    private FeAssetsSX assetsSX;
    private FeAssetsSaveCache.CampUnit campUnit;

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
    // ability
    public int[] getAbility(){
        return campUnit.getLineInt(1);
    }
    public int getAbilityHp(){
        return campUnit.getHp();
    }
    public int getAbilityStr(){
        return campUnit.getStr();
    }
    public int getAbilityMag(){
        return campUnit.getMag();
    }
    public int getAbilitySkill(){
        return campUnit.getSkill();
    }
    public int getAbilitySpe(){
        return campUnit.getSpe();
    }
    public int getAbilityLuk(){
        return campUnit.getLuk();
    }
    public int getAbilityDef(){
        return campUnit.getDef();
    }
    public int getAbilityMde(){
        return campUnit.getMde();
    }
    public int getAbilityWeig(){
        return campUnit.getWeig();
    }
    public int getAbilityMov(){
        return campUnit.getMov();
    }
    // upgrade
    public int[] getProfessionUpgrade(){
        return assetsUnit.getProfessionUpgrade(id());
    }
    public int getUpgradeHp(){
        return assetsUnit.getProfessionUpgradeHp(id());
    }
    public int getUpgradeStr(){
        return assetsUnit.getProfessionUpgradeStr(id());
    }
    public int getUpgradeMag(){
        return assetsUnit.getProfessionUpgradeMag(id());
    }
    public int getUpgradeSkill(){
        return assetsUnit.getProfessionUpgradeSkill(id());
    }
    public int getUpgradeSpe(){
        return assetsUnit.getProfessionUpgradeSpe(id());
    }
    public int getUpgradeLuk(){
        return assetsUnit.getProfessionUpgradeLuk(id());
    }
    public int getUpgradeDef(){
        return assetsUnit.getProfessionUpgradeDef(id());
    }
    public int getUpgradeMde(){
        return assetsUnit.getProfessionUpgradeMde(id());
    }
    public int getUpgradeWeig(){
        return assetsUnit.getProfessionUpgradeWeig(id());
    }
    public int getUpgradeMov(){
        return assetsUnit.getProfessionUpgradeMov(id());
    }
    // grow
    public int[] getGrow(){
        return assetsUnit.getProfessionGrow(id());
    }
    public int getGrowHp(){
        return assetsUnit.getProfessionGrowHp(id());
    }
    public int getGrowStr(){
        return assetsUnit.getProfessionGrowStr(id());
    }
    public int getGrowMag(){
        return assetsUnit.getProfessionGrowMag(id());
    }
    public int getGrowSkill(){
        return assetsUnit.getProfessionGrowSkill(id());
    }
    public int getGrowSpe(){
        return assetsUnit.getProfessionGrowSpe(id());
    }
    public int getGrowLuk(){
        return assetsUnit.getProfessionGrowLuk(id());
    }
    public int getGrowDef(){
        return assetsUnit.getProfessionGrowDef(id());
    }
    public int getGrowMde(){
        return assetsUnit.getProfessionGrowMde(id());
    }
    public int getGrowWeig(){
        return assetsUnit.getProfessionGrowWeig(id());
    }
    public int getGrowMov(){
        return assetsUnit.getProfessionGrowMov(id());
    }
    // add
    public int[] getAddition(){
        return campUnit.getLineInt(2);
    }
    public int getAdditionHp(){
        return campUnit.getAddHp();
    }
    public int getAdditionStr(){
        return campUnit.getAddStr();
    }
    public int getAdditionMag(){
        return campUnit.getAddMag();
    }
    public int getAdditionSkill(){
        return campUnit.getAddSkill();
    }
    public int getAdditionSpe(){
        return campUnit.getAddSpe();
    }
    public int getAdditionLuk(){
        return campUnit.getAddLuk();
    }
    public int getAdditionDef(){
        return campUnit.getAddDef();
    }
    public int getAdditionMde(){
        return campUnit.getAddMde();
    }
    public int getAdditionWeig(){
        return campUnit.getAddWeig();
    }
    public int getAdditionMov(){
        return campUnit.getAddMov();
    }
    // special
    public int[] getSpecial(){
        return campUnit.getLineInt(5);
    }
    public int getSpecial1(){
        return campUnit.getSpe1();
    }
    public int getSpecial2(){
        return campUnit.getSpe2();
    }
    public int getSpecial3(){
        return campUnit.getSpe3();
    }
    public int getSpecial4(){
        return campUnit.getSpe4();
    }
    public int getLevel(){
        return campUnit.getLevel();
    }
    public int[] getItem(){
        return campUnit.getLineInt(4);
    }
    public int getItem1(){
        return campUnit.getIt1();
    }
    public int getItem2(){
        return campUnit.getIt2();
    }
    public int getItem3(){
        return campUnit.getIt3();
    }
    public int getItem4(){
        return campUnit.getIt4();
    }
    public int getItem5(){
        return campUnit.getIt5();
    }
    public int getItem6(){
        return campUnit.getIt6();
    }
    public int getEquip(){
        return campUnit.getEquip();
    }
}