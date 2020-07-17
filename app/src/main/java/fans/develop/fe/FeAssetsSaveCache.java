package fans.develop.fe;

/*
    /assets/save/sX/cache 文件夹资源管理器,章节运行信息缓存
 */
public class FeAssetsSaveCache {

    private FeAssetsUnit _unit;
    private FeAssetsSaveUnit _saveUnit;
    private int sX;
    private String folder;

    public FeAssetsSaveCache(FeAssetsUnit unit, FeAssetsSaveUnit saveUnit, int sX){
        this._unit = unit;
        this._saveUnit = saveUnit;
        this.sX = sX;
        //sX
        folder = String.format("/save/s%d/cache/", sX);
        //file
        this.unit = new Unit(folder, "unit.txt", ";");
        this.round = new Round(folder, "round.txt", ";");
        //每个阵营都有一个链表头
        campBlue = new Camp(folder, 0, 0, ";");
        campRed = new Camp(folder, 1, 0, ";");
        campGreen = new Camp(folder, 2, 0, ";");
        campDark = new Camp(folder, 3, 0, ";");
        campOrange = new Camp(folder, 4, 0, ";");
        campPurple = new Camp(folder, 5, 0, ";");
        campCyan = new Camp(folder, 6, 0, ";");
    }

    //----- file -----

    public Unit unit;
    public Round round;

    // 有添加时初始化
    public Camp campBlue = null;
    public Camp campRed = null;
    public Camp campGreen = null;
    public Camp campDark = null;
    public Camp campOrange = null;
    public Camp campPurple = null;
    public Camp campCyan = null;

    //----- api -----

    //往特定阵营添加特定人物
    public void addUnit(int camp, int id, int xy, boolean fromSave)
    {
        //获取order
        int order = unit.add(camp, id, xy);
        //创建文件
        Camp cp = new Camp(folder, camp, order, ";");
        //添加到链表
        switch(camp){
            case 0: campBlue.add(cp); break;
            case 1: campRed.add(cp); break;
            case 2: campGreen.add(cp); break;
            case 3: campDark.add(cp); break;
            case 4: campOrange.add(cp); break;
            case 5: campPurple.add(cp); break;
            case 6: campCyan.add(cp); break;
            //camp error !!
            default: return;
        }
        //搬迁camp具体参数
        if(fromSave){
            //根据唯一id查找unit
            int line = _saveUnit.findUnit(id);
            if(line < 0)
                return;
            //搬迁数据
            //行0
            cp.setStandby(0);
            cp.setState(0);
            cp.setLevel(_saveUnit.getLevel(line));
            cp.setExp(_saveUnit.getExp(line));
            //行1 (整行拷贝)
            cp.setLine(1, _saveUnit.ability.getLine(_saveUnit.unit.getAbility(line)));
            //行2 (默认为0)
            //行3 (整行拷贝)
            cp.setLine(3, _saveUnit.skill.getLine(_saveUnit.unit.getSkill(line)));
            //行4 (整行拷贝)
            cp.setLine(4, _saveUnit.item.getLine(_saveUnit.unit.getItem(line)));
            //行5 (整行拷贝)
            cp.setLine(5, _saveUnit.special.getLine(_saveUnit.unit.getSpecial(line)));
            //行6
            cp.setRescue(0);
            cp.setRescueOrder(0);
            //行7 (整行拷贝)
            cp.setLine(7, _saveUnit.record.getLine(_saveUnit.unit.getRecord(line)));
            //行8
            cp.setView(3);
            cp.setViewAdd(0);
            //保存文件
            cp.save();
        }
        else{
            //搬迁数据
            //行0
            cp.setStandby(0);
            cp.setState(0);
            cp.setLevel(_unit.getLevel(id));
            cp.setExp(0);
            //行1 (整行拷贝)
            cp.setLine(1, _unit.getProfessionAbility(id));
            //行2 (默认为0)
            //行3 (整行拷贝)
            cp.setLine(3, _unit.getProfessionSkill(id));
            //行4 (整行拷贝)
            cp.setLine(4, _unit.getItem(id));
            //行5 (整行拷贝)
            cp.setLine(5, _unit.getAdditionSpecial(id));
            //行6
            cp.setRescue(0);
            cp.setRescueOrder(0);
            //行7
            cp.setRescue(0);
            cp.setWin(0);
            cp.setDie(0);
            //行8
            cp.setView(3);
            cp.setViewAdd(0);
            //保存文件
            cp.save();
        }
    }

    /*
        根据unit.txt信息加载各阵营人物信息camp_x_xxx.txt到内存
     */
    public void recoverUnit(){
        //遍历所有unit
        for(int i = 0; i < unit.total(); i++)
        {
            //获取order和camp
            int order = unit.getOrder(i);
            int camp = unit.getCamp(i);
            //创建文件
            Camp cp = new Camp(folder, camp, order, ";");
            //添加到链表
            Camp target = getCamp(camp);
            if(target != null)
                target.add(cp);
        }
    }

    /*
        获得某个阵营(的所有人物信息)
     */
    public Camp getCamp(int camp){
        switch(camp){
            case 0: return campBlue;
            case 1: return campRed;
            case 2: return campGreen;
            case 3: return campDark;
            case 4: return campOrange;
            case 5: return campPurple;
            case 6: return campCyan;
        }
        return null;
    }

    //----- class -----

    class Round extends FeReaderFile{

        public int getTurn(){ return getInt(0, 0); }
        public int getCamp(){ return getInt(0, 1); }
        public int getOrder(){ return getInt(0, 2); }
        public int getTime(){ return getInt(0, 3); }

        public void setTurn(int turn){ setValue(turn, 0, 0); }
        public void setCamp(int camp){ setValue(camp, 0, 1); }
        public void setOrder(int order){ setValue(order, 0, 2); }
        public void setTime(int time){ setValue(time, 0, 3); }
        
        public Round(String folder, String name, String split){
            super(folder, name, split);
            //文件没有加载,创建文件
            if(line() == 0)
            {
                addLine(new String[]{"0","0","0","0","回合/当前阵营/当前人物序号/章节时间"});
                save();
            }
        }
    }

    class Unit extends FeReaderFile{

        //用于永不重复的生成新的order
        private int order_seed = 0;

        //返回所在行
        public int find(int camp, int order){
            for(int i = 0; i < total(); i++)
            {
                if(getEnable(i) == 0 &&
                    getCamp(i) == camp && 
                    getOrder(i) == order)
                    return i;
            }
            return -1;
        }
        //移除
        public void remove(int camp, int order){
            int line = find(camp, order);
            if(line >= 0)
                setEnable(line, 1);
        }
        //注意xy格式如003004,代表x=3,y=4
        //返回序号,可用于创建camp_c_xxx.txt
        public int add(int camp, int id, int xxxyyy){
            return addLine(new String[]{
                "0",
                String.valueOf(camp),
                String.valueOf(order_seed++),
                String.valueOf(id),
                String.valueOf(xxxyyy)});
        }

        public int getEnable(int line){ return getInt(line, 0); }
        public int getCamp(int line){ return getInt(line, 1); }
        public int getOrder(int line){ return getInt(line, 2); }
        public int getId(int line){ return getInt(line, 3); }
        public int getXY(int line){ return getInt(line, 4); }
        public int getX(int line){ return getInt(line, 4)/1000; }
        public int getY(int line){ return getInt(line, 4)%1000; }

        public void setEnable(int line, int enable){ setValue(enable, line, 0); }
        public void setCamp(int line, int camp){ setValue(camp, line, 1); }
        public void setOrder(int line, int order){ setValue(order, line, 2); }
        public void setId(int line, int id){ setValue(id, line, 3); }
        public void setXY(int line, int xy){ setValue(xy, line, 4); }
        public void setX(int line, int x){ setValue(x*1000+getY(line), line, 4); }
        public void setY(int line, int y){ setValue(getX(line)*1000+y, line, 4); }
        
        public int total(){ return line(); }
        public Unit(String folder, String name, String split){
            super(folder, name, split);
            //文件加载成功
            if(line() > 0)
                //继续上一次的序号
                order_seed = getOrder(line() - 1);
        }
    }

    class Camp extends FeReaderFile{

        //链表信息
        public int order = 0;
        public int camp = 0;
        public Camp next = null, last = null;
        //链表操作
        public Camp find(int order){
            Camp camp = this.next;
            if(camp != null){
                while(camp.order != order && camp.next != null)
                    camp = camp.next;
                if(camp.order == order)
                    return camp;
            }
            return this;
        }
        public void add(Camp cp){
            if(this.next == null){
                this.next = cp;
                cp.last = this;
            }
            else{
                Camp camp = this.next;
                while(camp.next != null)
                    camp = camp.next;
                camp.next = cp;
                cp.last = camp;
            }
        }
        public void remove(int order){
            Camp camp = this.next;
            if(camp != null){
                while(camp.order != order && camp.next != null)
                    camp = camp.next;
                if(camp.order == order){
                    if(camp.next != null)
                        camp.next = camp.last;
                    if(camp.last != null)
                        camp.last = camp.next;
                }
            }
        }

        // line 0
        public int getStandby(){ return getInt(0, 0); }
        public int getState(){ return getInt(0, 1); }
        public int getLevel(){ return getInt(0, 2); }
        public int getExp(){ return getInt(0, 3); }
        public void setStandby(int standby){ setValue(standby, 0, 0); }
        public void setState(int state){ setValue(state, 0, 1); }
        public void setLevel(int level){ setValue(level, 0, 2); }
        public void setExp(int exp){ setValue(exp, 0, 3); }
        // line 1
        public int getHp(){ return getInt(1, 0); }
        public int getStr(){ return getInt(1, 1); }
        public int getMag(){ return getInt(1, 2); }
        public int getSkill(){ return getInt(1, 3); }
        public int getSpe(){ return getInt(1, 4); }
        public int getLuk(){ return getInt(1, 5); }
        public int getDef(){ return getInt(1, 6); }
        public int getMde(){ return getInt(1, 7); }
        public int getWeig(){ return getInt(1, 8); }
        public int getMov(){ return getInt(1, 9); }
        public void setHp(int hp){ setValue(hp, 1, 0); }
        public void setStr(int str){ setValue(str, 1, 1); }
        public void setMag(int mag){ setValue(mag, 1, 2); }
        public void setSkill(int skill){ setValue(skill, 1, 3); }
        public void setSpe(int spe){ setValue(spe, 1, 4); }
        public void setLuk(int luk){ setValue(luk, 1, 5); }
        public void setDef(int def){ setValue(def, 1, 6); }
        public void setMde(int mde){ setValue(mde, 1, 7); }
        public void setWeig(int weig){ setValue(weig, 1, 8); }
        public void setMov(int mov){ setValue(mov, 1, 9); }
        // line 2
        public int getAddHp(){ return getInt(2, 0); }
        public int getAddStr(){ return getInt(2, 1); }
        public int getAddMag(){ return getInt(2, 2); }
        public int getAddSkill(){ return getInt(2, 3); }
        public int getAddSpe(){ return getInt(2, 4); }
        public int getAddLuk(){ return getInt(2, 5); }
        public int getAddDef(){ return getInt(2, 6); }
        public int getAddMde(){ return getInt(2, 7); }
        public int getAddWeig(){ return getInt(2, 8); }
        public int getAddMov(){ return getInt(2, 9); }
        public void setAddHp(int hp){ setValue(hp, 2, 0); }
        public void setAddStr(int str){ setValue(str, 2, 1); }
        public void setAddMag(int mag){ setValue(mag, 2, 2); }
        public void setAddSkill(int skill){ setValue(skill, 2, 3); }
        public void setAddSpe(int spe){ setValue(spe, 2, 4); }
        public void setAddLuk(int luk){ setValue(luk, 2, 5); }
        public void setAddDef(int def){ setValue(def, 2, 6); }
        public void setAddMde(int mde){ setValue(mde, 2, 7); }
        public void setAddWeig(int weig){ setValue(weig, 2, 8); }
        public void setAddMov(int mov){ setValue(mov, 2, 9); }
        // line 3
        public int getSward(){ return getInt(3, 0); }
        public int getGun(){ return getInt(3, 1); }
        public int getAxe(){ return getInt(3, 2); }
        public int getArrow(){ return getInt(3, 3); }
        public int getPhy(){ return getInt(3, 4); }
        public int getLight(){ return getInt(3, 5); }
        public int getDark(){ return getInt(3, 6); }
        public int getStick(){ return getInt(3, 7); }
        public void setSward(int sward){ setValue(sward, 3, 0); }
        public void setGun(int gun){ setValue(gun, 3, 1); }
        public void setAxe(int axe){ setValue(axe, 3, 2); }
        public void setArrow(int arrow){ setValue(arrow, 3, 3); }
        public void setPhy(int phy){ setValue(phy, 3, 4); }
        public void setLight(int light){ setValue(light, 3, 5); }
        public void setDark(int dark){ setValue(dark, 3, 6); }
        public void setStick(int stick){ setValue(stick, 3, 7); }
        // line 4
        public int getIt1(){ return getInt(4, 0); }
        public int getIt2(){ return getInt(4, 1); }
        public int getIt3(){ return getInt(4, 2); }
        public int getIt4(){ return getInt(4, 3); }
        public int getIt5(){ return getInt(4, 4); }
        public int getIt6(){ return getInt(4, 5); }
        public int getEquip(){ return getInt(4, 6); }
        public void setIt1(int it1){ setValue(it1, 4, 0); }
        public void setIt2(int it2){ setValue(it2, 4, 1); }
        public void setIt3(int it3){ setValue(it3, 4, 2); }
        public void setIt4(int it4){ setValue(it4, 4, 3); }
        public void setIt5(int it5){ setValue(it5, 4, 4); }
        public void setIt6(int it6){ setValue(it6, 4, 5); }
        public void setEquip(int equip){ setValue(equip, 4, 6); }
        // line 5
        public int getSpe1(){ return getInt(5, 0); }
        public int getSpe2(){ return getInt(5, 1); }
        public int getSpe3(){ return getInt(5, 2); }
        public int getSpe4(){ return getInt(5, 3); }
        public void setSpe1(int spe1){ setValue(spe1, 5, 0); }
        public void setSpe2(int spe2){ setValue(spe2, 5, 1); }
        public void setSpe3(int spe3){ setValue(spe3, 5, 2); }
        public void setSpe4(int spe4){ setValue(spe4, 5, 3); }
        // line 6
        public int getRescue(){ return getInt(6, 0); }
        public int getRescueOrder(){ return getInt(6, 1); }
        public void setRescue(int rescue){ setValue(rescue, 6, 0); }
        public void setRescueOrder(int rescueOrder){ setValue(rescueOrder, 6, 1); }
        // line 7
        public int getFight(){ return getInt(7, 0); }
        public int getWin(){ return getInt(7, 1); }
        public int getDie(){ return getInt(7, 3); }
        public void setFight(int fight){ setValue(fight, 7, 0); }
        public void setWin(int win){ setValue(win, 7, 1); }
        public void setDie(int die){ setValue(die, 7, 2); }
        // line 8
        public int getView(){ return getInt(8, 0); }
        public int getViewAdd(){ return getInt(8, 1); }
        public void setView(int view){ setValue(view, 8, 0); }
        public void setViewAdd(int viewAdd){ setValue(viewAdd, 8, 1); }
        
        public Camp(String folder, int camp, int order, String split){
            super(folder, String.format("camp_%d_%03d.txt", camp, order), split);
            this.camp = camp;
            this.order = order;
            //文件没有加载,创建文件
            if(line() == 0)
            {
                addLine(new String[]{"0","0","0","行0:是否待机/状态/level"});
                addLine(new String[]{"0","0","0","0","0","0","0","0","0","0","行1:基本能力"});
                addLine(new String[]{"0","0","0","0","0","0","0","0","0","0","行2:加成能力"});
                addLine(new String[]{"0","0","0","0","0","0","0","0","行3:武器熟练度"});
                addLine(new String[]{"0","0","0","0","0","0","0","行4:物品和当前装备"});
                addLine(new String[]{"0","0","0","0","行5:特技列表"});
                addLine(new String[]{"0","0","行6:救出状态及其ID"});
                addLine(new String[]{"0","0","0","0","行7:战绩/战胜/战败"});
                addLine(new String[]{"0","0","行8:视野/道具加成视野"});
//                save();
            }
        }
    }
}