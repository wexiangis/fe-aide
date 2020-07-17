package fans.develop.fe;

import android.content.Context;
import android.widget.RelativeLayout;

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

    public FeLayout(Context context){
        super(context);
    }
}
