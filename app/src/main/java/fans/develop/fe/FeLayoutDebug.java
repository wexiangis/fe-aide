package fans.develop.fe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    放置于界面最上层,用于打印调试信息
 */
public class FeLayoutDebug extends FeLayout {

    private FeSectionCallback sectionCallback;

    //左右屏打印区域
    private LinearLayout linearLayoutL, linearLayoutR;
    //线性布局在父控件中的位置
    RelativeLayout.LayoutParams linearLayoutParamL, linearLayoutParamR;
    //textView在线性布局中的位置
    LinearLayout.LayoutParams txLayoutParams;

    public boolean checkHit(float x, float y) {
        return false;
    }

    public void refresh() {
        //遍历所有子view
        for (int i = 0; i < getChildCount(); i++)
            getChildAt(i).invalidate();
    }

    public FeLayoutDebug(Context context, FeSectionCallback sectionCallback) {
        super(context);
        this.sectionCallback = sectionCallback;

        linearLayoutL = new LinearLayout(context);
        linearLayoutL.setOrientation(LinearLayout.VERTICAL);
        linearLayoutL.setBackgroundColor(0x40804040);

        linearLayoutR = new LinearLayout(context);
        linearLayoutR.setOrientation(LinearLayout.VERTICAL);
        linearLayoutR.setBackgroundColor(0x40404080);

        linearLayoutParamL = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutParamL.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayoutParamL.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        linearLayoutParamL.setMargins(10, 0, 10, 0);

        linearLayoutParamR = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutParamR.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayoutParamR.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        linearLayoutParamR.setMargins(0, 0, 10, 0);

        txLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txLayoutParams.setMargins(0, 0, 0, 30);

        addView(linearLayoutL, linearLayoutParamL);
        addView(linearLayoutR, linearLayoutParamR);
    }

    /* ----- debug 条目的增、删 ----- */

    private TextView add(LinearLayout layout, int color) {
        TextView textView = new TextView(sectionCallback.getContext());
        textView.setTextColor(color);
        textView.setTextSize(16);
        layout.addView(textView, txLayoutParams);
        return textView;
    }

    /* ----- debug 设置 ----- */

    private void _show(LinearLayout layout, String key, int color, String val) {
        TextView tv = null;
        String tvText = key + ": ";
        //遍历所有子view
        for (int i = 0; i < layout.getChildCount(); i++) {
            tv = (TextView) layout.getChildAt(i);
            if (tv != null && tv.getText().toString().indexOf(tvText) == 0) {
                //删除键值?
                if (val == null)
                    layout.removeView(tv);
                    //修改键值
                else {
                    tv.setTextColor(color);
                    tv.setText(tvText + val);
                }
                return;
            }
        }
        //未找到,则增加键值
        tv = add(layout, color);
        tv.setText(key + ": " + val);
    }

    public void show(String key, int color, String val) {
        _show(linearLayoutL, key, color, val);
    }

    public void show2(String key, int color, String val) {
        _show(linearLayoutR, key, color, val);
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
