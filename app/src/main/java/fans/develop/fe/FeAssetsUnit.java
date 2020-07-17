package fans.develop.fe;

import android.graphics.Bitmap;

/*
    /assets/unit 文件夹资源管理器
 */
public class FeAssetsUnit {

    //----- api -----

    // name.txt
    public String getName(int count){
        return name.getName(unit.getName(count));
    }
    public String getSummary(int count){
        return name.getSummary(unit.getName(count));
    }
    // /head/xx.png
    public Bitmap getHead(int count){
        return getHeadBitmap(unit.getHead(count));
    }
    // p_name.txt
    public String getProfessionName(int count){
        return p_name.getName(profession.getName(unit.getProfession(count)));
    }
    public String getProfessionSummary(int count){
        return p_name.getSummary(profession.getName(unit.getProfession(count)));
    }
    // /anim/xxx.png
    public Bitmap getProfessionAnim(int count){
        return getAnimBitmap(profession.getAnim(unit.getProfession(count)));
    }
    // p_ability.txt
    public int[] getProfessionAbility(int count){
        return p_ability.getLineIntPlus(profession.getAbility(unit.getProfession(count)), getAdditionAbility(count));
    }
    public int getProfessionAbilityHp(int count){
        return p_ability.getHp(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityHp(count);
    }
    public int getProfessionAbilityStr(int count){
        return p_ability.getStr(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityStr(count);
    }
    public int getProfessionAbilityMag(int count){
        return p_ability.getMag(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityMag(count);
    }
    public int getProfessionAbilitySkill(int count){
        return p_ability.getSkill(profession.getAbility(unit.getProfession(count))) + getAdditionAbilitySkill(count);
    }
    public int getProfessionAbilitySpe(int count){
        return p_ability.getSpe(profession.getAbility(unit.getProfession(count))) + getAdditionAbilitySpe(count);
    }
    public int getProfessionAbilityLuk(int count){
        return p_ability.getLuk(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityLuk(count);
    }
    public int getProfessionAbilityDef(int count){
        return p_ability.getDef(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityDef(count);
    }
    public int getProfessionAbilityMde(int count){
        return p_ability.getMde(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityMde(count);
    }
    public int getProfessionAbilityWeig(int count){
        return p_ability.getWeig(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityWeig(count);
    }
    public int getProfessionAbilityMov(int count){
        return p_ability.getMov(profession.getAbility(unit.getProfession(count))) + getAdditionAbilityMov(count);
    }
    // p_upgrade.txt
    public int[] getProfessionUpgrade(int count){
        return p_upgrade.getLineInt(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeHp(int count){
        return p_upgrade.getHp(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeStr(int count){
        return p_upgrade.getStr(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeMag(int count){
        return p_upgrade.getMag(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeSkill(int count){
        return p_upgrade.getSkill(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeSpe(int count){
        return p_upgrade.getSpe(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeLuk(int count){
        return p_upgrade.getLuk(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeDef(int count){
        return p_upgrade.getDef(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeMde(int count){
        return p_upgrade.getMde(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeWeig(int count){
        return p_upgrade.getWeig(profession.getUpgrade(unit.getProfession(count)));
    }
    public int getProfessionUpgradeMov(int count){
        return p_upgrade.getMov(profession.getUpgrade(unit.getProfession(count)));
    }
    // p_grow.txt
    public int[] getProfessionGrow(int count){
        return p_grow.getLineIntPlus(profession.getGrow(unit.getProfession(count)), getAdditionGrow(count));
    }
    public int getProfessionGrowHp(int count){
        return p_grow.getHp(profession.getGrow(unit.getProfession(count))) + getAdditionGrowHp(count);
    }
    public int getProfessionGrowStr(int count){
        return p_grow.getStr(profession.getGrow(unit.getProfession(count))) + getAdditionGrowStr(count);
    }
    public int getProfessionGrowMag(int count){
        return p_grow.getMag(profession.getGrow(unit.getProfession(count))) + getAdditionGrowMag(count);
    }
    public int getProfessionGrowSkill(int count){
        return p_grow.getSkill(profession.getGrow(unit.getProfession(count))) + getAdditionGrowSkill(count);
    }
    public int getProfessionGrowSpe(int count){
        return p_grow.getSpe(profession.getGrow(unit.getProfession(count))) + getAdditionGrowSpe(count);
    }
    public int getProfessionGrowLuk(int count){
        return p_grow.getLuk(profession.getGrow(unit.getProfession(count))) + getAdditionGrowLuk(count);
    }
    public int getProfessionGrowDef(int count){
        return p_grow.getDef(profession.getGrow(unit.getProfession(count))) + getAdditionGrowDef(count);
    }
    public int getProfessionGrowMde(int count){
        return p_grow.getMde(profession.getGrow(unit.getProfession(count))) + getAdditionGrowMde(count);
    }
    public int getProfessionGrowWeig(int count){
        return p_grow.getWeig(profession.getGrow(unit.getProfession(count))) + getAdditionGrowWeig(count);
    }
    public int getProfessionGrowMov(int count){
        return p_grow.getMov(profession.getGrow(unit.getProfession(count))) + getAdditionGrowMov(count);
    }
    // p_skill.txt
    public int[] getProfessionSkill(int count){
        return p_skill.getLineIntPlus(profession.getSkill(unit.getProfession(count)), getAdditionSkill(count));
    }
    public int getProfessionSkillSword(int count){
        return p_skill.getSword(profession.getSkill(unit.getProfession(count))) + getAdditionSkillSword(count);
    }
    public int getProfessionSkillGun(int count){
        return p_skill.getGun(profession.getSkill(unit.getProfession(count))) + getAdditionSkillGun(count);
    }
    public int getProfessionSkillAxe(int count){
        return p_skill.getAxe(profession.getSkill(unit.getProfession(count))) + getAdditionSkillAxe(count);
    }
    public int getProfessionSkillArrow(int count){
        return p_skill.getArrow(profession.getSkill(unit.getProfession(count))) + getAdditionSkillArrow(count);
    }
    public int getProfessionSkillPhy(int count){
        return p_skill.getPhy(profession.getSkill(unit.getProfession(count))) + getAdditionSkillPhy(count);
    }
    public int getProfessionSkillLight(int count){
        return p_skill.getLight(profession.getSkill(unit.getProfession(count))) + getAdditionSkillLight(count);
    }
    public int getProfessionSkillDark(int count){
        return p_skill.getDark(profession.getSkill(unit.getProfession(count))) + getAdditionSkillDark(count);
    }
    public int getProfessionSkillStick(int count){
        return p_skill.getStick(profession.getSkill(unit.getProfession(count))) + getAdditionSkillStick(count);
    }
    // p_special.txt
    public int[] getProfessionSpecial(int count){
        return p_special.getLineInt(profession.getSpecial(unit.getProfession(count)));
    }
    public int getProfessionSpecial1(int count){
        return p_special.getSpe1(profession.getSpecial(unit.getProfession(count)));
    }
    public int getProfessionSpecial2(int count){
        return p_special.getSpe2(profession.getSpecial(unit.getProfession(count)));
    }
    public int getProfessionSpecial3(int count){
        return p_special.getSpe3(profession.getSpecial(unit.getProfession(count)));
    }
    public int getProfessionSpecial4(int count){
        return p_special.getSpe4(profession.getSpecial(unit.getProfession(count)));
    }
    // profession.txt
    public int getProfessionType(int count){
        return profession.getType(unit.getProfession(count));
    }
    // a_ability.txt
    public int[] getAdditionAbility(int count){
        return a_ability.getLineInt(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityHp(int count){
        return a_ability.getHp(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityStr(int count){
        return a_ability.getStr(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityMag(int count){
        return a_ability.getMag(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilitySkill(int count){
        return a_ability.getSkill(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilitySpe(int count){
        return a_ability.getSpe(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityLuk(int count){
        return a_ability.getLuk(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityDef(int count){
        return a_ability.getDef(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityMde(int count){
        return a_ability.getMde(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityWeig(int count){
        return a_ability.getWeig(addition.getAbility(unit.getAddition(count)));
    }
    public int getAdditionAbilityMov(int count){
        return a_ability.getMov(addition.getAbility(unit.getAddition(count)));
    }
    // a_grow.txt
    public int[] getAdditionGrow(int count){
        return a_grow.getLineInt(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowHp(int count){
        return a_grow.getHp(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowStr(int count){
        return a_grow.getStr(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowMag(int count){
        return a_grow.getMag(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowSkill(int count){
        return a_grow.getSkill(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowSpe(int count){
        return a_grow.getSpe(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowLuk(int count){
        return a_grow.getLuk(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowDef(int count){
        return a_grow.getDef(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowMde(int count){
        return a_grow.getMde(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowWeig(int count){
        return a_grow.getWeig(addition.getGrow(unit.getAddition(count)));
    }
    public int getAdditionGrowMov(int count){
        return a_grow.getMov(addition.getGrow(unit.getAddition(count)));
    }
    // a_skill.txt
    public int[] getAdditionSkill(int count){
        return a_skill.getLineInt(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillSword(int count){
        return a_skill.getSword(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillGun(int count){
        return a_skill.getGun(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillAxe(int count){
        return a_skill.getAxe(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillArrow(int count){
        return a_skill.getArrow(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillPhy(int count){
        return a_skill.getPhy(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillLight(int count){
        return a_skill.getLight(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillDark(int count){
        return a_skill.getDark(addition.getSkill(unit.getAddition(count)));
    }
    public int getAdditionSkillStick(int count){
        return a_skill.getStick(addition.getSkill(unit.getAddition(count)));
    }
    // a_special.txt
    public int[] getAdditionSpecial(int count){
        return a_special.getLineInt(addition.getSpecial(unit.getAddition(count)));
    }
    public int getAdditionSpecial1(int count){
        return a_special.getSpe1(addition.getSpecial(unit.getAddition(count)));
    }
    public int getAdditionSpecial2(int count){
        return a_special.getSpe2(addition.getSpecial(unit.getAddition(count)));
    }
    public int getAdditionSpecial3(int count){
        return a_special.getSpe3(addition.getSpecial(unit.getAddition(count)));
    }
    public int getAdditionSpecial4(int count){
        return a_special.getSpe4(addition.getSpecial(unit.getAddition(count)));
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
    public int getEquip(int count){
        return item.getEquip(unit.getItem(count));
    }

    //----- file -----

    // unit
    public Unit unit = new Unit("/unit/", "unit.txt", ";");
    public Name name = new Name("/unit/", "name.txt", ";");
    public Item item = new Item("/unit/", "item.txt", ";");
    // unit addition
    public Addition addition = new Addition("/unit/", "addition.txt", ";");
    public A_Ability a_ability = new A_Ability("/unit/", "a_ability.txt", ";");
    public A_Grow a_grow = new A_Grow("/unit/", "a_grow.txt", ";");
    public A_Skill a_skill = new A_Skill("/unit/", "a_skill.txt", ";");
    public A_Special a_special = new A_Special("/unit/", "a_special.txt", ";");
    // unit profession
    public Profession profession = new Profession("/unit/", "profession.txt", ";");
    public P_Name p_name = new P_Name("/unit/", "p_name.txt", ";");
    public P_Ability p_ability = new P_Ability("/unit/", "p_ability.txt", ";");
    public P_Upgrade p_upgrade = new P_Upgrade("/unit/", "p_upgrade.txt", ";");
    public P_Grow p_grow = new P_Grow("/unit/", "p_grow.txt", ";");
    public P_Skill p_skill = new P_Skill("/unit/", "p_skill.txt", ";");
    public P_Special p_special = new P_Special("/unit/", "p_special.txt", ";");

    //----- 文件夹 -----

    private FeReaderBitmap bitmapReader = new FeReaderBitmap();

    public Bitmap getAnimBitmap(int id){
        return bitmapReader.load_png_byId("/unit/anim/", id);
    }
    public Bitmap getHeadBitmap(int id){
        return bitmapReader.load_png_byId("/unit/head/", id);
    }

    //----- class -----

    //人物列表
    class Unit extends FeReaderFile {
        public int getName(int line){ return getInt(line, 0); }
        public int getHead(int line){ return getInt(line, 1); }
        public int getProfession(int line){ return getInt(line, 2); }
        public int getAddition(int line){ return getInt(line, 3); }
        public int getLevel(int line){ return getInt(line, 4); }
        public int getItem(int line){ return getInt(line, 5); }

        public void setName(int line, int name){ setValue(name, line, 0); }
        public void setHead(int line, int head){ setValue(head, line, 1); }
        public void setProfession(int line, int profession){ setValue(profession, line, 2); }
        public void setAddition(int line, int addition){ setValue(addition, line, 3); }
        public void setLevel(int line, int level){ setValue(level, line, 4); }
        public void setItem(int line, int item){ setValue(item, line, 5); }

        public Unit(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物名称列表
    class Name extends FeReaderFile {
        public String getName(int line){ return getString(line, 0); }
        public String getSummary(int line){ return getString(line, 1); }

        public void setName(int line, String name){ setValue(name, line, 0); }
        public void setSummary(int line, String summary){ setValue(summary, line, 1); }

        public Name(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物物品列表
    class Item extends FeReaderFile {
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

        public Item(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物加成列表
    class Addition extends FeReaderFile {
        public int getAbility(int line){ return getInt(line, 0); }
        public int getGrow(int line){ return getInt(line, 1); }
        public int getSkill(int line){ return getInt(line, 2); }
        public int getSpecial(int line){ return getInt(line, 3); }

        public void setAbility(int line, int ability){ setValue(ability, line, 0); }
        public void setGrow(int line, int grow){ setValue(grow, line, 1); }
        public void setSkill(int line, int skill){ setValue(skill, line, 2); }
        public void setSpecial(int line, int special){ setValue(special, line, 3); }

        public Addition(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物能力加成列表
    class A_Ability extends FeReaderFile {
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

        public A_Ability(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物成长率加成列表
    class A_Grow extends FeReaderFile {
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

        public A_Grow(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物技能加成列表
    class A_Skill extends FeReaderFile {
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

        public A_Skill(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //人物特技列表
    class A_Special extends FeReaderFile {
        public int getSpe1(int line){ return getInt(line, 0); }
        public int getSpe2(int line){ return getInt(line, 1); }
        public int getSpe3(int line){ return getInt(line, 2); }
        public int getSpe4(int line){ return getInt(line, 3); }

        public void setSpe1(int line, int spe1){ setValue(spe1, line, 0); }
        public void setSpe2(int line, int spe2){ setValue(spe2, line, 1); }
        public void setSpe3(int line, int spe3){ setValue(spe3, line, 2); }
        public void setSpe4(int line, int spe4){ setValue(spe4, line, 3); }

        public A_Special(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业列表
    class Profession extends FeReaderFile {
        public int getName(int line){ return getInt(line, 0); }
        public int getAnim(int line){ return getInt(line, 1); }
        public int getAbility(int line){ return getInt(line, 2); }
        public int getUpgrade(int line){ return getInt(line, 3); }
        public int getGrow(int line){ return getInt(line, 4); }
        public int getSkill(int line){ return getInt(line, 5); }
        public int getSpecial(int line){ return getInt(line, 6); }
        public int getType(int line){ return getInt(line, 7); }
        public int getNext(int line){ return getInt(line, 8); }

        public void setName(int line, int name){ setValue(name, line, 0); }
        public void setAnim(int line, int anim){ setValue(anim, line, 1); }
        public void setAbility(int line, int ability){ setValue(ability, line, 2); }
        public void setUpgrade(int line, int upgrade){ setValue(upgrade, line, 3); }
        public void setGrow(int line, int grow){ setValue(grow, line, 4); }
        public void setSkill(int line, int skill){ setValue(skill, line, 5); }
        public void setSpecial(int line, int special){ setValue(special, line, 6); }
        public void setType(int line, int type){ setValue(type, line, 7); }
        public void setNext(int line, int next){ setValue(next, line, 8); }

        public Profession(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业名称列表
    class P_Name extends FeReaderFile {
        public String getName(int line){ return getString(line, 0); }
        public String getSummary(int line){ return getString(line, 1); }

        public void setName(int line, String name){ setValue(name, line, 0); }
        public void setSummary(int line, String summary){ setValue(summary, line, 1); }

        public P_Name(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业能力列表
    class P_Ability extends FeReaderFile {
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

        public P_Ability(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业升级加点列表
    class P_Upgrade extends FeReaderFile {
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

        public P_Upgrade(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业成长率列表
    class P_Grow extends FeReaderFile {
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

        public P_Grow(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业技能列表
    class P_Skill extends FeReaderFile {
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

        public P_Skill(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    //职业特技列表
    class P_Special extends FeReaderFile {
        public int getSpe1(int line){ return getInt(line, 0); }
        public int getSpe2(int line){ return getInt(line, 1); }
        public int getSpe3(int line){ return getInt(line, 2); }
        public int getSpe4(int line){ return getInt(line, 3); }

        public void setSpe1(int line, int spe1){ setValue(spe1, line, 0); }
        public void setSpe2(int line, int spe2){ setValue(spe2, line, 1); }
        public void setSpe3(int line, int spe3){ setValue(spe3, line, 2); }
        public void setSpe4(int line, int spe4){ setValue(spe4, line, 3); }

        public P_Special(String folder, String name, String split){
            super(folder, name, split);
        }
    }
}
