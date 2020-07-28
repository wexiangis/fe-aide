package fans.develop.fe;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

/*
    地板标记图层,显示各个人物的行动范围
 */
public class FeLayoutMarkEnemy extends FeLayout {

    private Context context;
    private FeSectionCallback sectionCallback;

    public FeLayoutMarkEnemy(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.context = context;
        this.sectionCallback = sectionCallback;
    }

    /* ---------- function ---------- */
    
    public boolean checkHit(float x, float y){
        return false;
    }

    public void refresh(){
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    /*
        显示特定人物的mark范围
     */
    public void markUnit(){
        ;
    }

    /*
        关闭特定人物的mark范围
     */
    public void cleanUnit(int id){
        ;
    }

    /*
        关闭全部人物的mark范围
     */
    public void cleanAllUnit(){
        _removeViewAll(this);
    }

    /*
        接收点击事件
        hitThis: 点击目标为当前控件
        hitType: 具体点击目标,查看 FeFlagHit.java
     */
    public void click(float x, float y, FeFlagHit flag){
        ;
    }

    /* ---------- abstract interface ---------- */

    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        //释放子view
        _removeViewAll(this);
        return true;
    }
    public void onReload(){
        ;
    }
}
