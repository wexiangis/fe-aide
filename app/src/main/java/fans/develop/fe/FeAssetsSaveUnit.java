package fans.develop.fe;

import android.graphics.Bitmap;

/*
    /assets/save/sX/unit 文件夹资源管理器,己方人物存档信息
 */
class FeAssetsSaveUnit {

    private FeAssetsUnit _unit;
    public int sX = 0;

    public FeAssetsSaveUnit(FeAssetsUnit unit, int sX){
        this._unit = unit;
        this.sX = sX;
        //sX
        String folder = String.format("/save/s%d/unit/", sX);
        //file
        this.unit = new Unit(folder, "unit.txt", ";");
        this.ability = new Ability(folder, "ability.txt", ";");
        this.grow = new Grow(folder, "grow.txt", ";");
        this.skill = new Skill(folder, "skill.txt", ";");
        this.special = new Special(folder, "special.txt", ";");
        this.item = new Item(folder, "item.txt", ";");
        this.record = new Record(folder, "record.txt", ";");
    }
    
    //----- file -----

    public Unit unit;
    public Ability ability;
    public Grow grow;
    public Skill skill;
    public Special special;
    public Item item;
    public Record record;

    //----- api -----

    // 根据id定位行
    public int findUnit(int id){
        for(int i = 0; i < unit.total(); i++){
            if(unit.getId(i) == id)
                return i;
        }
        return -1;
    }

    // unit.txt
    public int getId(int count){
        return unit.getId(count);
    }
    // _unit.name.txt
    public String getName(int count){
        return _unit.getName(unit.getId(count));
    }
    public String getSummary(int count){
        return _unit.getSummary(unit.getId(count));
    }
    // /head/xx.png
    public Bitmap getHead(int count){
        return _unit.getHead(unit.getId(count));
    }
    // /anim/xxx.png
    public Bitmap getProfessionAnim(int count){
        return _unit.getProfessionAnim(unit.getId(count));
    }
    // _unit.p_name.txt
    public String getProfessionName(int count){
        return _unit.getProfessionName(unit.getId(count));
    }
    public String getProfessionSummary(int count){
        return _unit.getProfessionSummary(unit.getId(count));
    }
    // _unit.p_upgrade.txt
    public int[] getProfessionUpgrade(int count){
        return _unit.getProfessionUpgrade(unit.getId(count));
    }
    public int getProfessionUpgradeHp(int count){
        return _unit.getProfessionUpgradeHp(unit.getId(count));
    }
    public int getProfessionUpgradeStr(int count){
        return _unit.getProfessionUpgradeStr(unit.getId(count));
    }
    public int getProfessionUpgradeMag(int count){
        return _unit.getProfessionUpgradeMag(unit.getId(count));
    }
    public int getProfessionUpgradeSkill(int count){
        return _unit.getProfessionUpgradeSkill(unit.getId(count));
    }
    public int getProfessionUpgradeSpe(int count){
        return _unit.getProfessionUpgradeSpe(unit.getId(count));
    }
    public int getProfessionUpgradeLuk(int count){
        return _unit.getProfessionUpgradeLuk(unit.getId(count));
    }
    public int getProfessionUpgradeDef(int count){
        return _unit.getProfessionUpgradeDef(unit.getId(count));
    }
    public int getProfessionUpgradeMde(int count){
        return _unit.getProfessionUpgradeMde(unit.getId(count));
    }
    public int getProfessionUpgradeWeig(int count){
        return _unit.getProfessionUpgradeWeig(unit.getId(count));
    }
    public int getProfessionUpgradeMov(int count){
        return _unit.getProfessionUpgradeMov(unit.getId(count));
    }
    // _unit.p_special.txt
    public int[] getProfessionSpecial(int count){
        return _unit.getProfessionSpecial(unit.getId(count));
    }
    public int getProfessionSpecial1(int count){
        return _unit.getProfessionSpecial1(unit.getId(count));
    }
    public int getProfessionSpecial2(int count){
        return _unit.getProfessionSpecial2(unit.getId(count));
    }
    public int getProfessionSpecial3(int count){
        return _unit.getProfessionSpecial3(unit.getId(count));
    }
    public int getProfessionSpecial4(int count){
        return _unit.getProfessionSpecial4(unit.getId(count));
    }
    // ability.txt
    public int[] getAbility(int count){
        return ability.getLineInt(unit.getAbility(count));
    }
    public int getAbilityHp(int count){
        return ability.getHp(unit.getAbility(count));
    }
    public int getAbilityStr(int count){
        return ability.getStr(unit.getAbility(count));
    }
    public int getAbilityMag(int count){
        return ability.getMag(unit.getAbility(count));
    }
    public int getAbilitySkill(int count){
        return ability.getSkill(unit.getAbility(count));
    }
    public int getAbilitySpe(int count){
        return ability.getSpe(unit.getAbility(count));
    }
    public int getAbilityLuk(int count){
        return ability.getLuk(unit.getAbility(count));
    }
    public int getAbilityDef(int count){
        return ability.getDef(unit.getAbility(count));
    }
    public int getAbilityMde(int count){
        return ability.getMde(unit.getAbility(count));
    }
    public int getAbilityWeig(int count){
        return ability.getWeig(unit.getAbility(count));
    }
    public int getAbilityMov(int count){
        return ability.getMov(unit.getAbility(count));
    }
    // grow.txt
    public int[] getGrow(int count){
        return grow.getLineInt(unit.getGrow(count));
    }
    public int getGrowHp(int count){
        return grow.getHp(unit.getGrow(count));
    }
    public int getGrowStr(int count){
        return grow.getStr(unit.getGrow(count));
    }
    public int getGrowMag(int count){
        return grow.getMag(unit.getGrow(count));
    }
    public int getGrowSkill(int count){
        return grow.getSkill(unit.getGrow(count));
    }
    public int getGrowSpe(int count){
        return grow.getSpe(unit.getGrow(count));
    }
    public int getGrowLuk(int count){
        return grow.getLuk(unit.getGrow(count));
    }
    public int getGrowDef(int count){
        return grow.getDef(unit.getGrow(count));
    }
    public int getGrowMde(int count){
        return grow.getMde(unit.getGrow(count));
    }
    public int getGrowWeig(int count){
        return grow.getWeig(unit.getGrow(count));
    }
    public int getGrowMov(int count){
        return grow.getMov(unit.getGrow(count));
    }
    // skill.txt
    public int[] getSkill(int count){
        return skill.getLineInt(unit.getSkill(count));
    }
    public int getSkillSword(int count){
        return skill.getSword(unit.getSkill(count));
    }
    public int getSkillGun(int count){
        return skill.getGun(unit.getSkill(count));
    }
    public int getSkillAxe(int count){
        return skill.getAxe(unit.getSkill(count));
    }
    public int getSkillArrow(int count){
        return skill.getArrow(unit.getSkill(count));
    }
    public int getSkillPhy(int count){
        return skill.getPhy(unit.getSkill(count));
    }
    public int getSkillLight(int count){
        return skill.getLight(unit.getSkill(count));
    }
    public int getSkillDark(int count){
        return skill.getDark(unit.getSkill(count));
    }
    public int getSkillStick(int count){
        return skill.getStick(unit.getSkill(count));
    }
    // special.txt
    public int[] getSpecial(int count){
        return special.getLineInt(unit.getSpecial(count));
    }
    public int getSpecial1(int count){
        return special.getSpe1(unit.getSpecial(count));
    }
    public int getSpecial2(int count){
        return special.getSpe2(unit.getSpecial(count));
    }
    public int getSpecial3(int count){
        return special.getSpe3(unit.getSpecial(count));
    }
    public int getSpecial4(int count){
        return special.getSpe4(unit.getSpecial(count));
    }
    // unit.txt
    public int getLevel(int count){
        return unit.getLevel(count);
    }
    // item.txt
    public int[] getItem(int count){
        return item.getLineInt(unit.getItem(count));
    }
    public int getItem1(int count){
        return item.getIt1(unit.getItem(count));
    }
    public int getItem2(int count){
        return item.getIt2(unit.getItem(count));
    }
    public int getItem3(int count){
        return item.getIt3(unit.getItem(count));
    }
    public int getItem4(int count){
        return item.getIt4(unit.getItem(count));
    }
    public int getItem5(int count){
        return item.getIt5(unit.getItem(count));
    }
    public int getItem6(int count){
        return item.getIt6(unit.getItem(count));
    }
    // unit.txt
    public int getCamp(int count){
        return unit.getCamp(count);
    }
    // unit.txt
    public int getState(int count){
        return unit.getState(count);
    }
    // record.txt
    public int getRecord(int count){
        return record.getRecord(unit.getRecord(count));
    }
    public int getWin(int count){
        return record.getWin(unit.getRecord(count));
    }
    public int getDie(int count){
        return record.getDie(unit.getRecord(count));
    }
    // exp
    public int getExp(int count){
        return unit.getExp(count);
    }

    //----- class -----

    //人物列表
    public class Unit extends FeReaderFile {
        public int getId(int line){ return getInt(line, 0); }
        public int getAbility(int line){ return getInt(line, 1); }
        public int getGrow(int line){ return getInt(line, 2); }
        public int getSkill(int line){ return getInt(line, 3); }
        public int getSpecial(int line){ return getInt(line, 4); }
        public int getLevel(int line){ return getInt(line, 5); }
        public int getItem(int line){ return getInt(line, 6); }
        public int getCamp(int line){ return getInt(line, 7); }
        public int getState(int line){ return getInt(line, 8); }
        public int getRecord(int line){ return getInt(line, 9); }
        public int getExp(int line){ return getInt(line, 10); }

        public void setId(int line, int id){ setValue(id, line, 0); }
        public void setAbility(int line, int ability){ setValue(ability, line, 1); }
        public void setGrow(int line, int grow){ setValue(grow, line, 2); }
        public void setSkill(int line, int skill){ setValue(skill, line, 3); }
        public void setSpecial(int line, int special){ setValue(special, line, 4); }
        public void setLevel(int line, int level){ setValue(level, line, 5); }
        public void setItem(int line, int item){ setValue(item, line, 6); }
        public void setCamp(int line, int camp){ setValue(camp, line, 7); }
        public void setState(int line, int state){ setValue(state, line, 8); }
        public void setRecord(int line, int record){ setValue(record, line, 9); }
        public void setExp(int line, int exp){ setValue(exp, line, 10); }

        public int total(){ return line(); }
        public Unit(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业能力列表
    public class Ability extends FeReaderFile {
        public int getHp(int line){ return getInt(line, 0); }
        public int getStr(int line){ return getInt(line, 1); }
        public int getMag(int line){ return getInt(line, 2); }
        public int getSkill(int line){ return getInt(line, 3); }
        public int getSpe(int line){ return getInt(line, 4); }
        public int getLuk(int line){ return getInt(line, 5); }
        public int getDef(int line){ return getInt(line, 6); }
        public int getMde(int line){ return getInt(line, 7); }
        public int getWeig(int line){ return getInt(line, 8); }
        public int getMov(int line){ return getInt(line, 9); }

        public void setHp(int line, int hp){ setValue(hp, line, 0); }
        public void setStr(int line, int str){ setValue(str, line, 1); }
        public void setMag(int line, int mag){ setValue(mag, line, 2); }
        public void setSkill(int line, int skill){ setValue(skill, line, 3); }
        public void setSpe(int line, int spe){ setValue(spe, line, 4); }
        public void setLuk(int line, int luk){ setValue(luk, line, 5); }
        public void setDef(int line, int def){ setValue(def, line, 6); }
        public void setMde(int line, int mde){ setValue(mde, line, 7); }
        public void setWeig(int line, int weig){ setValue(weig, line, 8); }
        public void setMov(int line, int mov){ setValue(mov, line, 9); }

        public int total(){ return line(); }
        public Ability(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //成长率列表
    public class Grow extends FeReaderFile {
        public int getHp(int line){ return getInt(line, 0); }
        public int getStr(int line){ return getInt(line, 1); }
        public int getMag(int line){ return getInt(line, 2); }
        public int getSkill(int line){ return getInt(line, 3); }
        public int getSpe(int line){ return getInt(line, 4); }
        public int getLuk(int line){ return getInt(line, 5); }
        public int getDef(int line){ return getInt(line, 6); }
        public int getMde(int line){ return getInt(line, 7); }
        public int getWeig(int line){ return getInt(line, 8); }
        public int getMov(int line){ return getInt(line, 9); }

        public void setHp(int line, int hp){ setValue(hp, line, 0); }
        public void setStr(int line, int str){ setValue(str, line, 1); }
        public void setMag(int line, int mag){ setValue(mag, line, 2); }
        public void setSkill(int line, int skill){ setValue(skill, line, 3); }
        public void setSpe(int line, int spe){ setValue(spe, line, 4); }
        public void setLuk(int line, int luk){ setValue(luk, line, 5); }
        public void setDef(int line, int def){ setValue(def, line, 6); }
        public void setMde(int line, int mde){ setValue(mde, line, 7); }
        public void setWeig(int line, int weig){ setValue(weig, line, 8); }
        public void setMov(int line, int mov){ setValue(mov, line, 9); }

        public int total(){ return line(); }
        public Grow(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //技能列表
    public class Skill extends FeReaderFile {
        public int getSword(int line){ return getInt(line, 0); }
        public int getGun(int line){ return getInt(line, 1); }
        public int getAxe(int line){ return getInt(line, 2); }
        public int getArrow(int line){ return getInt(line, 3); }
        public int getPhy(int line){ return getInt(line, 4); }
        public int getLight(int line){ return getInt(line, 5); }
        public int getDark(int line){ return getInt(line, 6); }
        public int getStick(int line){ return getInt(line, 7); }

        public void setSword(int line, int sword){ setValue(sword, line, 0); }
        public void setGun(int line, int gun){ setValue(gun, line, 1); }
        public void setAxe(int line, int axe){ setValue(axe, line, 2); }
        public void setArrow(int line, int arrow){ setValue(arrow, line, 3); }
        public void setPhy(int line, int phy){ setValue(phy, line, 4); }
        public void setLight(int line, int light){ setValue(light, line, 5); }
        public void setDark(int line, int dark){ setValue(dark, line, 6); }
        public void setStick(int line, int stick){ setValue(stick, line, 7); }

        public int total(){ return line(); }
        public Skill(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //个人特技列表
    public class Special extends FeReaderFile {
        public int getSpe1(int line){ return getInt(line, 0); }
        public int getSpe2(int line){ return getInt(line, 1); }
        public int getSpe3(int line){ return getInt(line, 2); }
        public int getSpe4(int line){ return getInt(line, 3); }

        public void setSpe1(int line, int spe1){ setValue(spe1, line, 0); }
        public void setSpe2(int line, int spe2){ setValue(spe2, line, 1); }
        public void setSpe3(int line, int spe3){ setValue(spe3, line, 2); }
        public void setSpe4(int line, int spe4){ setValue(spe4, line, 3); }

        public int total(){ return line(); }
        public Special(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物物品列表
    public class Item extends FeReaderFile {
        public int getIt1(int line){ return getInt(line, 0); }
        public int getIt2(int line){ return getInt(line, 1); }
        public int getIt3(int line){ return getInt(line, 2); }
        public int getIt4(int line){ return getInt(line, 3); }
        public int getIt5(int line){ return getInt(line, 4); }
        public int getIt6(int line){ return getInt(line, 5); }
        public int getEquip(int line){ return getInt(line, 6); }

        public void setIt1(int line, int it1){ setValue(it1, line, 0); }
        public void setIt2(int line, int it2){ setValue(it2, line, 1); }
        public void setIt3(int line, int it3){ setValue(it3, line, 2); }
        public void setIt4(int line, int it4){ setValue(it4, line, 3); }
        public void setIt5(int line, int it5){ setValue(it5, line, 4); }
        public void setIt6(int line, int it6){ setValue(it6, line, 5); }
        public void setEquip(int line, int equip){ setValue(equip, line, 6); }

        public int total(){ return line(); }
        public Item(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物战绩
    public class Record extends FeReaderFile {
        public int getRecord(int line){ return getInt(line, 0); }
        public int getWin(int line){ return getInt(line, 1); }
        public int getDie(int line){ return getInt(line, 2); }

        public void setRecord(int line, int record){ setValue(record, line, 0); }
        public void setWin(int line, int win){ setValue(win, line, 1); }
        public void setDie(int line, int die){ setValue(die, line, 2); }

        public int total(){ return line(); }
        public Record(String folder, String name, String split){
            super(folder, name, split);
        }
    }

}