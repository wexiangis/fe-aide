package fans.develop.fe;

import android.content.Context;
import android.widget.RelativeLayout;
import android.view.View;

/*
    所有layout的父类,用于提供一些统一的接口或方法,比如返回键事件处理接口
 */
public abstract class FeLayout extends RelativeLayout{

    //按键返回
    //返回: true表示有使用到, false时将由系统决定退回界面或退出
    public abstract boolean onKeyBack();

    //即将注销该layout,请在这里做收尾操作
    //返回: false表示未准备好, 将不注销
    public abstract boolean onDestory();

    //重新加载界面及其数据
    //子类可以在这里添加自己的loading界面
    public abstract void onReload();

    /* ---------- remove方法替代 ---------- */

    public void _removeView(FeLayout layout, View v){
        //该 view 属于 FeView 子系
        if (v instanceof FeView)
            ((FeView)v).onDestory();
        //该 view 属于 FeLayout 子系
        else if (v instanceof FeLayout)
            ((FeLayout)v).onDestory();
        //移除
        layout.removeView(v);
    }

    public void _removeViewAll(FeLayout layout){
        //遍历所有子view,逐个移除
        for (int i = 0; i < getChildCount(); i++)
            _removeView(layout, getChildAt(i));
        //确保移除
        layout.removeAllViews();
    }

    public FeLayout(Context context){
        super(context);
    }
}
