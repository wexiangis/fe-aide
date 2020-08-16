package fans.develop.fe;

import android.view.*;

public class FeLayoutSectionPlotEnding extends FeLayout {

    private FeData feData = null;
    // 存档数据
    private FeAssetsSX sxData = null;

    public FeLayoutSectionPlotEnding(FeData feData, FeAssetsSX sxData) {
        super(feData.context);
        this.feData = feData;
        this.sxData = sxData;
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack() {
        return false;
    }

    public boolean onDestory() {
        //释放子view
        _removeViewAll(this);
        return true;
    }

    public void onReload() {
        ;
    }
}
