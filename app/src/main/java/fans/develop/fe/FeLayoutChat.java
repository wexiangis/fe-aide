package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    地图上的对话
 */
public class FeLayoutChat extends FeLayout {

	private FeSectionCallback sectionCallback;

    public FeLayoutChat(Context context, FeSectionCallback sectionCallback) {
        super(context);
		this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */
	
    public boolean checkHit(float x, float y){
        return false;
    }

    public void refresh(){
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, Boolean hitThis, int hitType){
        ;
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
