package fans.develop.fe;

/*
    /assets/save/sX/cache 文件夹资源管理器,章节运行信息缓存
 */
public class FeAssetsSaveCache {

    private FeAssetsUnit _unit;
    private FeAssetsSaveUnit _saveUnit;
    private int sX;
    private String folder;

    public FeAssetsSaveCache(FeAssetsUnit unit, FeAssetsSaveUnit saveUnit, int sX) {
        this._unit = unit;
        this._saveUnit = saveUnit;
        this.sX = sX;
        //sX
        folder = String.format("/save/s%d/cache/", sX);
        //file
        this.unit = new Unit(folder, "unit.txt", ";");
        this.round = new Round(folder, "round.txt", ";");
        //每个阵营都有一个链表头
        campUnitBlue = new CampUnit(folder, 0, 0, ";");
        campUnitRed = new CampUnit(folder, 1, 0, ";");
        campUnitGreen = new CampUnit(folder, 2, 0, ";");
        campUnitDark = new CampUnit(folder, 3, 0, ";");
        campUnitOrange = new CampUnit(folder, 4, 0, ";");
        campUnitPurple = new CampUnit(folder, 5, 0, ";");
        campUnitCyan = new CampUnit(folder, 6, 0, ";");
    }

    //----- file -----

    public Unit unit;
    public Round round;

    // 有添加时初始化
    public CampUnit campUnitBlue = null;
    public CampUnit campUnitRed = null;
    public CampUnit campUnitGreen = null;
    public CampUnit campUnitDark = null;
    public CampUnit campUnitOrange = null;
    public CampUnit campUnitPurple = null;
    public CampUnit campUnitCyan = null;

    //----- api -----

    //往特定阵营添加特定人物
    public void addUnit(int camp, int id, int xy, boolean fromSave) {
        //获取order
        int order = unit.add(camp, id, xy);
        //创建文件
        CampUnit campUnit = new CampUnit(folder, camp, order, ";");
        //添加到链表
        switch (camp) {
            case 0:
                campUnitBlue.add(campUnit);
                break;
            case 1:
                campUnitRed.add(campUnit);
                break;
            case 2:
                campUnitGreen.add(campUnit);
                break;
            case 3:
                campUnitDark.add(campUnit);
                break;
            case 4:
                campUnitOrange.add(campUnit);
                break;
            case 5:
                campUnitPurple.add(campUnit);
                break;
            case 6:
                campUnitCyan.add(campUnit);
                break;
            //camp error !!
            default:
                return;
        }
        //搬迁camp具体参数
        if (fromSave) {
            //根据唯一id查找unit
            int line = _saveUnit.findUnit(id);
            if (line < 0)
                return;
            //搬迁数据
            //行0
            campUnit.setStandby(0);
            campUnit.setState(0);
            campUnit.setLevel(_saveUnit.getLevel(line));
            campUnit.setExp(_saveUnit.getExp(line));
            campUnit.setHpRes(_saveUnit.getAbilityHp(line));
            //行1 (整行拷贝)
            campUnit.setLine(1, _saveUnit.ability.getLine(_saveUnit.unit.getAbility(line)));
            //行2 (默认为0)
            //行3 (整行拷贝)
            campUnit.setLine(3, _saveUnit.skill.getLine(_saveUnit.unit.getSkill(line)));
            //行4 (整行拷贝)
            campUnit.setLine(4, _saveUnit.item.getLine(_saveUnit.unit.getItem(line)));
            //行5 (整行拷贝)
            campUnit.setLine(5, _saveUnit.special.getLine(_saveUnit.unit.getSpecial(line)));
            //行6
            campUnit.setRescue(0);
            campUnit.setRescueOrder(0);
            //行7 (整行拷贝)
            campUnit.setLine(7, _saveUnit.record.getLine(_saveUnit.unit.getRecord(line)));
            //行8
            campUnit.setView(3);
            campUnit.setViewAdd(0);
            //保存文件
            campUnit.save();
        } else {
            //搬迁数据
            //行0
            campUnit.setStandby(0);
            campUnit.setState(0);
            campUnit.setLevel(_unit.getLevel(id));
            campUnit.setExp(0);
            campUnit.setHpRes(_unit.getProfessionAbilityHp(id));
            //行1 (整行拷贝)
            campUnit.setLine(1, _unit.getProfessionAbility(id));
            //行2 (默认为0)
            //行3 (整行拷贝)
            campUnit.setLine(3, _unit.getProfessionSkill(id));
            //行4 (整行拷贝)
            campUnit.setLine(4, _unit.getItem(id));
            //行5 (整行拷贝)
            campUnit.setLine(5, _unit.getAdditionSpecial(id));
            //行6
            campUnit.setRescue(0);
            campUnit.setRescueOrder(0);
            //行7
            campUnit.setRescue(0);
            campUnit.setWin(0);
            campUnit.setDie(0);
            //行8
            campUnit.setView(3);
            campUnit.setViewAdd(0);
            //保存文件
            campUnit.save();
        }
    }

    /*
        根据unit.txt信息加载各阵营人物信息camp_x_xxx.txt到内存
     */
    public void recoverUnit() {
        //遍历所有unit
        for (int i = 0; i < unit.total(); i++) {
            //获取order和camp
            int order = unit.getOrder(i);
            int camp = unit.getCamp(i);
            //创建文件
            CampUnit campUnit = new CampUnit(folder, camp, order, ";");
            //添加到链表
            CampUnit campUnits = getCampUnits(camp);
            if (campUnits != null)
                campUnits.add(campUnit);
        }
    }

    /*
        获得某个阵营链表
     */
    public CampUnit getCampUnits(int camp) {
        switch (camp) {
            case 0:
                return campUnitBlue;
            case 1:
                return campUnitRed;
            case 2:
                return campUnitGreen;
            case 3:
                return campUnitDark;
            case 4:
                return campUnitOrange;
            case 5:
                return campUnitPurple;
            case 6:
                return campUnitCyan;
        }
        return null;
    }

    /*
        获得特定order人物
     */
    public CampUnit getCampUnit(int order) {
        if (order >= unit.line())
            return null;
        int camp = unit.getCamp(order);
        CampUnit campUnit = getCampUnits(camp);
        if (campUnit == null)
            return null;
        return campUnit.find(order);
    }

    //----- class -----

    public class Round extends FeReaderFile {

        public int getTurn() {
            return getInt(0, 0);
        }

        public int getCamp() {
            return getInt(0, 1);
        }

        public int getOrder() {
            return getInt(0, 2);
        }

        public int getTime() {
            return getInt(0, 3);
        }

        public void setTurn(int turn) {
            setValue(turn, 0, 0);
        }

        public void setCamp(int camp) {
            setValue(camp, 0, 1);
        }

        public void setOrder(int order) {
            setValue(order, 0, 2);
        }

        public void setTime(int time) {
            setValue(time, 0, 3);
        }

        public Round(String folder, String name, String split) {
            super(folder, name, split);
            //文件没有加载,创建文件
            if (line() == 0) {
                addLine(new String[]{"0", "0", "0", "0", "回合/当前阵营/当前人物序号/章节时间"});
                save();
            }
        }
    }

    public class Unit extends FeReaderFile {

        //移除
        public void remove(int order) {
            setDisable(order, 1);
        }

        //恢复
        public void recover(int order) {
            setDisable(order, 0);
        }

        //注意xy格式如003004,代表x=3,y=4
        //返回序号,可用于创建camp_c_xxx.txt
        public int add(int camp, int id, int xxxyyy) {
            return addLine(new String[]{
                    "0",
                    String.valueOf(camp),
                    String.valueOf(line()),
                    String.valueOf(id),
                    String.valueOf(xxxyyy)});
        }

        public int total() {
            return line();
        }

        public int getDisable(int line) {
            return getInt(line, 0);
        }

        public int getCamp(int line) {
            return getInt(line, 1);
        }

        public int getOrder(int line) {
            return getInt(line, 2);
        }

        public int getId(int line) {
            return getInt(line, 3);
        }

        public int getXY(int line) {
            return getInt(line, 4);
        }

        public int getX(int line) {
            return getInt(line, 4) / 1000;
        }

        public int getY(int line) {
            return getInt(line, 4) % 1000;
        }

        public void setDisable(int line, int disable) {
            setValue(disable, line, 0);
        }

        public void setCamp(int line, int camp) {
            setValue(camp, line, 1);
        }

        public void setOrder(int line, int order) {
            setValue(order, line, 2);
        }

        public void setId(int line, int id) {
            setValue(id, line, 3);
        }

        public void setXY(int line, int xy) {
            setValue(xy, line, 4);
        }

        public void setX(int line, int x) {
            setValue(x * 1000 + getY(line), line, 4);
        }

        public void setY(int line, int y) {
            setValue(getX(line) * 1000 + y, line, 4);
        }

        public Unit(String folder, String name, String split) {
            super(folder, name, split);
        }
    }

    public class CampUnit extends FeReaderFile {

        //链表信息
        private CampUnit next = null, last = null;
        private int total = 0;

        public int total() {
            return total;
        }

        //链表操作
        public CampUnit find(int order) {
            CampUnit campUnit = next;
            if (campUnit != null) {
                while (campUnit.order != order && campUnit.next != null)
                    campUnit = campUnit.next;
                if (campUnit.order == order)
                    return campUnit;
            }
            return null;
        }

        public void add(CampUnit campUnit) {
            if (next == null) {
                next = campUnit;
                campUnit.last = this;
                total = 1;
            } else {
                CampUnit tmp = next;
                while (tmp.next != null)
                    tmp = tmp.next;
                tmp.next = campUnit;
                campUnit.last = tmp;
            }
        }

        public void remove(int order) {
            CampUnit campUnit = next;
            if (campUnit != null) {
                while (campUnit.order != order && campUnit.next != null)
                    campUnit = campUnit.next;
                if (campUnit.order == order) {
                    if (campUnit.next != null)
                        campUnit.next.last = campUnit.last;
                    if (campUnit.last != null)
                        campUnit.last.next = campUnit.next;
                    if (campUnit == next)
                        next = campUnit.next;
                    total -= 1;
                }
            }
        }

        //文件名信息
        private int order = 0;

        public int order() {
            return order;
        }

        private int camp = 0;

        public int camp() {
            return camp;
        }

        // line 0
        public int getStandby() {
            return getInt(0, 0);
        }

        public int getState() {
            return getInt(0, 1);
        }

        public int getLevel() {
            return getInt(0, 2);
        }

        public int getExp() {
            return getInt(0, 3);
        }

        public int getHpRes() {
            return getInt(0, 4);
        }

        public void setStandby(int standby) {
            setValue(standby, 0, 0);
        }

        public void setState(int state) {
            setValue(state, 0, 1);
        }

        public void setLevel(int level) {
            setValue(level, 0, 2);
        }

        public void setExp(int exp) {
            setValue(exp, 0, 3);
        }

        public void setHpRes(int hpRes) {
            setValue(hpRes, 0, 4);
        }

        // line 1
        public int getHp() {
            return getInt(1, 0);
        }

        public int getStr() {
            return getInt(1, 1);
        }

        public int getMag() {
            return getInt(1, 2);
        }

        public int getSkill() {
            return getInt(1, 3);
        }

        public int getSpe() {
            return getInt(1, 4);
        }

        public int getLuk() {
            return getInt(1, 5);
        }

        public int getDef() {
            return getInt(1, 6);
        }

        public int getMde() {
            return getInt(1, 7);
        }

        public int getWeig() {
            return getInt(1, 8);
        }

        public int getMov() {
            return getInt(1, 9);
        }

        public void setHp(int hp) {
            setValue(hp, 1, 0);
        }

        public void setStr(int str) {
            setValue(str, 1, 1);
        }

        public void setMag(int mag) {
            setValue(mag, 1, 2);
        }

        public void setSkill(int skill) {
            setValue(skill, 1, 3);
        }

        public void setSpe(int spe) {
            setValue(spe, 1, 4);
        }

        public void setLuk(int luk) {
            setValue(luk, 1, 5);
        }

        public void setDef(int def) {
            setValue(def, 1, 6);
        }

        public void setMde(int mde) {
            setValue(mde, 1, 7);
        }

        public void setWeig(int weig) {
            setValue(weig, 1, 8);
        }

        public void setMov(int mov) {
            setValue(mov, 1, 9);
        }

        // line 2
        public int getAddHp() {
            return getInt(2, 0);
        }

        public int getAddStr() {
            return getInt(2, 1);
        }

        public int getAddMag() {
            return getInt(2, 2);
        }

        public int getAddSkill() {
            return getInt(2, 3);
        }

        public int getAddSpe() {
            return getInt(2, 4);
        }

        public int getAddLuk() {
            return getInt(2, 5);
        }

        public int getAddDef() {
            return getInt(2, 6);
        }

        public int getAddMde() {
            return getInt(2, 7);
        }

        public int getAddWeig() {
            return getInt(2, 8);
        }

        public int getAddMov() {
            return getInt(2, 9);
        }

        public void setAddHp(int hp) {
            setValue(hp, 2, 0);
        }

        public void setAddStr(int str) {
            setValue(str, 2, 1);
        }

        public void setAddMag(int mag) {
            setValue(mag, 2, 2);
        }

        public void setAddSkill(int skill) {
            setValue(skill, 2, 3);
        }

        public void setAddSpe(int spe) {
            setValue(spe, 2, 4);
        }

        public void setAddLuk(int luk) {
            setValue(luk, 2, 5);
        }

        public void setAddDef(int def) {
            setValue(def, 2, 6);
        }

        public void setAddMde(int mde) {
            setValue(mde, 2, 7);
        }

        public void setAddWeig(int weig) {
            setValue(weig, 2, 8);
        }

        public void setAddMov(int mov) {
            setValue(mov, 2, 9);
        }

        // line 3
        public int getSward() {
            return getInt(3, 0);
        }

        public int getGun() {
            return getInt(3, 1);
        }

        public int getAxe() {
            return getInt(3, 2);
        }

        public int getArrow() {
            return getInt(3, 3);
        }

        public int getPhy() {
            return getInt(3, 4);
        }

        public int getLight() {
            return getInt(3, 5);
        }

        public int getDark() {
            return getInt(3, 6);
        }

        public int getStick() {
            return getInt(3, 7);
        }

        public void setSward(int sward) {
            setValue(sward, 3, 0);
        }

        public void setGun(int gun) {
            setValue(gun, 3, 1);
        }

        public void setAxe(int axe) {
            setValue(axe, 3, 2);
        }

        public void setArrow(int arrow) {
            setValue(arrow, 3, 3);
        }

        public void setPhy(int phy) {
            setValue(phy, 3, 4);
        }

        public void setLight(int light) {
            setValue(light, 3, 5);
        }

        public void setDark(int dark) {
            setValue(dark, 3, 6);
        }

        public void setStick(int stick) {
            setValue(stick, 3, 7);
        }

        // line 4
        public int getIt1() {
            return getInt(4, 0);
        }

        public int getIt2() {
            return getInt(4, 1);
        }

        public int getIt3() {
            return getInt(4, 2);
        }

        public int getIt4() {
            return getInt(4, 3);
        }

        public int getIt5() {
            return getInt(4, 4);
        }

        public int getIt6() {
            return getInt(4, 5);
        }

        public int getEquip() {
            return getInt(4, 6);
        }

        public void setIt1(int it1) {
            setValue(it1, 4, 0);
        }

        public void setIt2(int it2) {
            setValue(it2, 4, 1);
        }

        public void setIt3(int it3) {
            setValue(it3, 4, 2);
        }

        public void setIt4(int it4) {
            setValue(it4, 4, 3);
        }

        public void setIt5(int it5) {
            setValue(it5, 4, 4);
        }

        public void setIt6(int it6) {
            setValue(it6, 4, 5);
        }

        public void setEquip(int equip) {
            setValue(equip, 4, 6);
        }

        // line 5
        public int getSpe1() {
            return getInt(5, 0);
        }

        public int getSpe2() {
            return getInt(5, 1);
        }

        public int getSpe3() {
            return getInt(5, 2);
        }

        public int getSpe4() {
            return getInt(5, 3);
        }

        public void setSpe1(int spe1) {
            setValue(spe1, 5, 0);
        }

        public void setSpe2(int spe2) {
            setValue(spe2, 5, 1);
        }

        public void setSpe3(int spe3) {
            setValue(spe3, 5, 2);
        }

        public void setSpe4(int spe4) {
            setValue(spe4, 5, 3);
        }

        // line 6
        public int getRescue() {
            return getInt(6, 0);
        }

        public int getRescueOrder() {
            return getInt(6, 1);
        }

        public void setRescue(int rescue) {
            setValue(rescue, 6, 0);
        }

        public void setRescueOrder(int rescueOrder) {
            setValue(rescueOrder, 6, 1);
        }

        // line 7
        public int getFight() {
            return getInt(7, 0);
        }

        public int getWin() {
            return getInt(7, 1);
        }

        public int getDie() {
            return getInt(7, 3);
        }

        public void setFight(int fight) {
            setValue(fight, 7, 0);
        }

        public void setWin(int win) {
            setValue(win, 7, 1);
        }

        public void setDie(int die) {
            setValue(die, 7, 2);
        }

        // line 8
        public int getView() {
            return getInt(8, 0);
        }

        public int getViewAdd() {
            return getInt(8, 1);
        }

        public void setView(int view) {
            setValue(view, 8, 0);
        }

        public void setViewAdd(int viewAdd) {
            setValue(viewAdd, 8, 1);
        }

        public CampUnit(String folder, int camp, int order, String split) {
            super(folder, String.format("camp_%d_%03d.txt", camp, order), split);
            this.camp = camp;
            this.order = order;
            //文件没有加载,创建文件
            if (line() == 0) {
                addLine(new String[]{"0", "0", "0", "行0:是否待机/状态/level"});
                addLine(new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "行1:基本能力"});
                addLine(new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "行2:加成能力"});
                addLine(new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "行3:武器熟练度"});
                addLine(new String[]{"0", "0", "0", "0", "0", "0", "0", "行4:物品和当前装备"});
                addLine(new String[]{"0", "0", "0", "0", "行5:特技列表"});
                addLine(new String[]{"0", "0", "行6:救出状态及其ID"});
                addLine(new String[]{"0", "0", "0", "0", "行7:战绩/战胜/战败"});
                addLine(new String[]{"0", "0", "行8:视野/道具加成视野"});
                // save();
            }
        }
    }
}
