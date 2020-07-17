package fans.develop.fe;

/*
    当前的人物信息, 各个图层涉及当前人物信息都从这里取
 */
public class FeSectionUnit {

    // ----------- 选中信息 -----------

    //当前选中的unit的位置
    public FeInfoGrid selectSite = new FeInfoGrid();

    //当前选中的unit
    public FeViewUnit selectView = null;
}
