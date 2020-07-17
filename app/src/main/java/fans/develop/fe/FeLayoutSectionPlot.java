package fans.develop.fe;

import android.view.*;

public class FeLayoutSectionPlot extends FeLayout{
    
    private FeData feData = null;
    // 存档数据
    private FeAssetsSX sxData = null;

    public FeLayoutSectionPlot(FeData feData, FeAssetsSX sxData){
        super(feData.context);
        this.feData = feData;
        this.sxData = sxData;
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof FeView)
                ((FeView)v).onDestory();
        }
        return true;
    }
    public void onReload(){
        ;
    }
}
