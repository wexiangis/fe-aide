package fans.develop.fe;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    主题曲界面
 */
public class FeLayoutTheme extends FeLayout {

    private FeData feData;
    private TextView textView;
    private RelativeLayout.LayoutParams layoutParams;

    //触屏事件回调函数
    private View.OnTouchListener onTouchListener  = new View.OnTouchListener (){
        public boolean onTouch(View v, MotionEvent event) {
            //触屏UP时
            if(event.getAction() == MotionEvent.ACTION_UP)
                feData.flow.loadMainMenu();
            //不返回true的话ACTION_DOWN之后的事件都会被丢弃
            return true;
        }
    };

    public void reload(){

        this.removeAllViews();

        /* ----- 数据初始化 -----*/

        //按任意键继续
        textView = new TextView(feData.context);
        textView.setText("任意触屏键继续");
        textView.setTextColor(0x80FFFFFF);
        textView.setTextSize(24);
        //相对主界面的位置
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.setMargins(0, 0, 0, 100);
        //添加布局参数
        textView.setLayoutParams(layoutParams);

        /* ----- 界面装载 -----*/

        //添加到主界面
        this.addView(textView);
        //背景
        this.setBackgroundColor(0xFF404040);
        this.setOnTouchListener(onTouchListener);
    }

    public FeLayoutTheme(FeData feData)
    {
        super(feData.context);
        this.feData = feData;
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        return true;
    }
    public void onReload(){
        this.reload();
    }
}