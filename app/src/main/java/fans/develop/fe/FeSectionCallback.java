package fans.develop.fe;

import android.content.Context;

public interface FeSectionCallback{

    void addHeartUnit(FeHeartUnit heartUnit);
    void removeHeartUnit(FeHeartUnit heartUnit);
    /* ------------------------------- */
    void refreshSectionMap(int section);
    void refresh();
    FeFlagHit checkHit(float x, float y);
    /* ------------------------------- */
    FeAssets getAssets();
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

    void onMapMove(Boolean on);
    Boolean onMapMove();

    void onMapHit(Boolean on);
    Boolean onMapHit();

    void onUnitSelect(Boolean on);
    Boolean onUnitSelect();

    void onUnitMove(Boolean on);
    Boolean onUnitMove();

    void onUnitMenu(Boolean on);
    Boolean onUnitMenu();

    void onSysMenu(Boolean on);
    Boolean onSysMenu();
}
