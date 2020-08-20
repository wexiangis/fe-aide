package fans.develop.fe;

import android.content.Context;

/*
    各个图层(FeLayoutXXX)及其子view(FeViewXXX)运行时所需的接口都在这里
    当需要显示某个图层或view时,需先实现下列接口
 */
public interface FeSectionCallback {

    void addHeartUnit(FeHeartUnit heartUnit);

    void removeHeartUnit(FeHeartUnit heartUnit);

    /* ------------------------------- */
    void refreshSectionMap(int section);

    void refresh();

    FeFlagHit checkHit(float x, float y);

    void debug(String key, int color, String val);

    void debug2(String key, int color, String val);

    /* ------------------------------- */
    FeAssets getAssets();

    FeAssetsSX getAssetsSX();

    Context getContext();

    FeSectionMap getSectionMap();

    FeSectionUnit getSectionUnit();

    FeSectionShader getSectionShader();

    /* ------------------------------- */
    FeLayoutMap getLayoutMap();

    FeLayoutMarkEnemy getLayoutMarkEnemy();

    FeLayoutMark getLayoutMark();

    FeLayoutUnit getLayoutUnit();

    FeLayoutMapInfo getLayoutMapInfo();

    FeLayoutUnitMenu getLayoutUnitMenu();

    FeLayoutChat getLayoutChat();

    FeLayoutSysMenu getLayoutSysMenu();

    FeLayoutInterlude getLayoutInterlude();

    FeLayoutDebug getLayoutDebug();
    /* ------------------------------- */

    void onTouchDisable(Boolean disable);

    Boolean onTouchDisable();

    void onTouchMov(Boolean disable);

    Boolean onTouchMov();

    void onMapMove(Boolean on);

    Boolean onMapMove();

    void onMapHit(Boolean on);

    Boolean onMapHit();

    void onUnitSelect(Boolean on);

    Boolean onUnitSelect();

    void onUnitMove(Boolean on);

    Boolean onUnitMove();

    void onUnitMoveing(Boolean on);

    Boolean onUnitMoveing();

    void onUnitMenu(Boolean on);

    Boolean onUnitMenu();

    void onSysMenu(Boolean on);

    Boolean onSysMenu();
}
