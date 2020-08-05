package fans.develop.fe;

/*
    /assets/section 文件夹资源管理器
 */
public class FeAssetsSection {

    private FeAssetsUnit _unit;
    private int section = 0;

    public FeAssetsSection(FeAssetsUnit unit, int section){
        this._unit = unit;
        this.section = section;
        //section
        String sectionFolder = String.format("/section/section%02d/", section);
        //file
        this.unit = new Unit(sectionFolder, "unit.txt", ";");
        this.site = new Site(sectionFolder, "site.txt", ";");
        this.target = new Target(sectionFolder, "target.txt", ";");
        this.talk = new Talk(sectionFolder, "talk.txt", ";");
        this.bgm = new Bgm(sectionFolder, "bgm.txt", ";");
    }

    //----- file -----

    public Unit unit;
    public Site site;
    public Target target;
    public Talk talk;
    public Bgm bgm;

    //----- api -----

    //----- class -----

    public class Unit extends FeReaderFile{

        public int getTrigger(int line){ return getInt(line, 0); }
        public int getTurn(int line){ return getInt(line, 1); }
        public int getXY(int line){ return getInt(line, 2); }
        public int getX(int line){ return getInt(line, 2)/1000; }
        public int getY(int line){ return getInt(line, 2)%1000; }
        public int getId(int line){ return getInt(line, 3); }
        public int getTrend(int line){ return getInt(line, 4); }
        public int getLeader(int line){ return getInt(line, 5); }
        public int getCamp(int line){ return getInt(line, 6); }
        
        public int total(){ return line(); }
        public Unit(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    public class Site extends FeReaderFile{

        public int getTrigger(int line){ return getInt(line, 0); }
        public int getTurn(int line){ return getInt(line, 1); }
        public int getXY(int line){ return getInt(line, 2); }
        public int getX(int line){ return getInt(line, 2)/1000; }
        public int getY(int line){ return getInt(line, 2)%1000; }
        public int getFix(int line){ return getInt(line, 3); }
        public int getId(int line){ return getInt(line, 4); }
        
        public int total(){ return line(); }
        public Site(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    public class Target extends FeReaderFile{

        public int getCondition(int line){ return getInt(line, 0); }
        public int getId(int line){ return getInt(line, 1); }
        public int getTurn(int line){ return getInt(line, 2); }
        public int getXYNum(int line){ return getInt(line, 3); }
        public int getXY(int line, int num){ return getInt(line, num); }
        public int getX(int line, int num){ return getInt(line, num + 4)/1000; }
        public int getY(int line, int num){ return getInt(line, num + 4)%1000; }
        
        public int total(){ return line(); }
        public Target(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    public class Talk extends FeReaderFile{
        
        public int total(){ return line(); }
        public Talk(String folder, String name, String split){
            super(folder, name, split);
        }
    }

    public class Bgm extends FeReaderFile{
        
        public int total(){ return line(); }
        public Bgm(String folder, String name, String split){
            super(folder, name, split);
        }
    }
}