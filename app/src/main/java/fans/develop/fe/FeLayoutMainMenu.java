package fans.develop.fe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/*
    主界面菜单选项,不从属于主界面,它有自己的背景墙
 */
public class FeLayoutMainMenu extends FeLayout {

    private FeData feData;
    //菜单信息
    private Button tvContinue = null, tvLoad = null, tvNew = null, tvCopy = null, tvDel = null, tvElse = null;
    //菜单线性布局参数
    private LinearLayout linearLayout = null;
    private RelativeLayout.LayoutParams linearLayoutParam = null;
    private LinearLayout.LayoutParams tvLayoutParams = null;

    //触屏事件回调函数
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //按下反馈
                v.setAlpha(0.5f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //松开反馈
                v.setAlpha(1.0f);
                //点击:创建存档
                if (v == tvNew)
                    feData.flow.loadSaveMenu(0);
                    //点击:继续
                else if (v == tvContinue)
                    feData.flow.loadSaveMenu(1);
                    //点击:加载存档
                else if (v == tvLoad)
                    feData.flow.loadSaveMenu(2);
                    //点击:删除存档
                else if (v == tvDel)
                    feData.flow.loadSaveMenu(3);
                    //点击:复制存档
                else if (v == tvCopy)
                    feData.flow.loadSaveMenu(4);
                    //点击:附加内容
                else if (v == tvElse)
                    feData.flow.loadExtraMenu();
            }
            //不返回true的话ACTION_DOWN之后的事件都会被丢弃
            return true;
        }
    };

    private void loadMenu() {
        //更新存档状态(saveState[][]的状态)
        int[][] saveState = feData.saveLoad();
        //检查是否存在记录
        boolean findRecord = false, findContinue = false;
        for (int i = 0; i < feData.saveNum(); i++) {
            if (saveState[i][0] >= 0) {
                findRecord = true;
                if (saveState[i][1] > 0)
                    findContinue = true;
                break;
            }
        }
        //按特定参数添加各项到线性布局
        linearLayout.removeAllViews();
        if (findContinue)
            linearLayout.addView(tvContinue, tvLayoutParams);
        if (findRecord)
            linearLayout.addView(tvLoad, tvLayoutParams);
        linearLayout.addView(tvNew, tvLayoutParams);
        if (findRecord)
            linearLayout.addView(tvCopy, tvLayoutParams);
        if (findRecord)
            linearLayout.addView(tvDel, tvLayoutParams);
        linearLayout.addView(tvElse, tvLayoutParams);
    }

    private Button buildButtonStyle(String text) {
        Button button = new Button(feData.context);
        button.setText(text);
        button.setTextColor(0xFFFFFFFF);
        button.setTextSize(24);
        button.setGravity(Gravity.CENTER);
        button.setOnTouchListener(onTouchListener);
        button.setBackground(Drawable.createFromStream(getClass().getResourceAsStream("/assets/menu/item/item_b.png"), null));
        //button.setBackgroundDrawable(Drawable.createFromStream(getClass().getResourceAsStream("/assets/menu/item/item_b.png"), null));
        button.setPadding(80, 0, 80, 0);
        return button;
    }

    public void reload() {

        this._removeViewAll(this);

        /* ----- 数据初始化 -----*/

        //菜单各项TXT
        tvContinue = buildButtonStyle("继续游戏");
        tvLoad = buildButtonStyle("读取记录");
        tvNew = buildButtonStyle("新游戏");
        tvCopy = buildButtonStyle("复 制");
        tvDel = buildButtonStyle("删 除");
        tvElse = buildButtonStyle("附加内容");
        //创建线性布局窗体
        linearLayout = new LinearLayout(feData.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //创建线性布局窗体参数
        tvLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLayoutParams.setMargins(0, 0, 0, 30);
        //线性布局窗体相对主界面位置参数
        linearLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayoutParam.addRule(RelativeLayout.CENTER_VERTICAL);
        //根据存档状态加载条目
        loadMenu();

        /* ----- 装载界面 -----*/

        //显示列表
        this._removeViewAll(this);
        this.addView(linearLayout, linearLayoutParam);
        this.setBackgroundColor(0x80408040);
    }

    public FeLayoutMainMenu(FeData feData) {
        super(feData.context);
        this.feData = feData;
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
        this.reload();
    }
}
