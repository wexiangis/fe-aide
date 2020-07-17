package fans.develop.fe;

import android.content.Context;
import android.view.View;

/*
    封装的公用View父类，添加回收方法
 */
public abstract class FeView extends View {

    //即将销毁该view,在这里释放各种内存句柄
    public abstract void onDestory();

    public FeView(Context context){
        super(context);
    }
}
