package fans.develop.fe;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/*
    额外菜单列表
 */
public class FeLayoutExtra extends FeLayout {

    private FeData feData;
    //条目列表
    private Button[] bnSaveList;
    //菜单线性布局参数
    private LinearLayout linearLayout = null;
    //linear布局在主界面中的位置参数
    RelativeLayout.LayoutParams linearLayoutParam = null;
    //button在linear中的布局参数
    LinearLayout.LayoutParams bnLayoutParams = null;

    //触屏事件回调函数
    private View.OnTouchListener onTouchListener  = new View.OnTouchListener (){
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //按下反馈
                v.setAlpha(0.5f);
            }
            else if(event.getAction() == MotionEvent.ACTION_UP) {
                //松开反馈
                v.setAlpha(1.0f);
                //遍历检查目标条目
                for(int i = 0; i < bnSaveList.length; i++){
                    if(v == bnSaveList[i])
                        ;
                }
            }
            //不返回true的话ACTION_DOWN之后的事件都会被丢弃
            return true;
        }
    };

    private Button buildButtonStyle(String text){
        Button button = new Button(feData.context);
        button.setText(text);
        button.setTextColor(0xFFFFFFFF);
        button.setTextSize(24);
        button.setGravity(Gravity.CENTER);
        button.setOnTouchListener(onTouchListener);
        button.setBackground(Drawable.createFromStream(getClass().getResourceAsStream("/assets/menu/item/item_g.png"), null));
        button.setPadding(80, 0, 80, 0);
		return button;
    }

    public void reload(){

        this.removeAllViews();

        /* ----- 数据初始化 -----*/

        //初始化
        bnSaveList = new Button[2];
        bnSaveList[0] = buildButtonStyle("音 乐");
        bnSaveList[1] = buildButtonStyle("战 绩");
        //创建线性布局窗体
        linearLayout = new LinearLayout(feData.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //创建线性布局窗体参数
        bnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bnLayoutParams.setMargins(0,0, 0, 30);
        //线性布局窗体相对主界面位置参数
        linearLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayoutParam.addRule(RelativeLayout.CENTER_VERTICAL);

        //添加条目到视图
        linearLayout.removeAllViews();
        for(int i = 0; i < bnSaveList.length; i++)
            linearLayout.addView(bnSaveList[i], bnLayoutParams);

        /* ----- 装载界面 -----*/

        //显示列表
        this.removeAllViews();
        this.addView(linearLayout, linearLayoutParam);
        this.setBackgroundColor(0x80408040);
    }

    public FeLayoutExtra(FeData feData){
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
