package fans.develop.fe;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;

/*
    存档条列表
 */
public class FeLayoutSave extends FeLayout {

    public static final String default_name = "未 使 用";

    private FeData feData;
    private int ctrl;
    //条目列表
    private Button[] bnSaveList;
    //菜单线性布局参数
    private LinearLayout linearLayout = null;

    private LinearLayout.LayoutParams bnLayoutParams;
    private RelativeLayout.LayoutParams linearLayoutParam;

    //复制模式时,记录第一个选中的存档
    private int currnt_select = -1;

    int[][] saveState;

    //触屏事件回调函数
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //按下反馈
                v.setAlpha(0.5f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                //松开反馈
                v.setAlpha(1.0f);
                //遍历所有存档位置
                for (int i = 0; i < bnSaveList.length; i++) {
                    if (v == bnSaveList[i]) {
                        switch (FeLayoutSave.this.ctrl) {
                            //新建
                            case 0:
                                //确认为空存档
                                if (bnSaveList[i].getText().toString().indexOf(default_name) == 0) {
                                    //新建
                                    feData.assets.save.newSx(i);
                                    //刷新
                                    refresh();
                                    //检查创建结果
                                    if (bnSaveList[i].getText().toString().indexOf(default_name) == 0) {
                                        //提示缺少存储权限
                                        Toast.makeText(feData.activity, "缺少存储权限,无法创建文件", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(feData.activity, "请到设置->应用管理启用存储权限", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            //继续游戏
                            case 1:
                                //确认为非空存档且为中断记录
                                if (bnSaveList[i].getText().toString().indexOf(default_name) != 0
                                        && saveState[i][1] > 0)
                                    feData.flow.loadSection(i, 1);
                                break;
                            //读取记录
                            case 2:
                                //确认为非空存档
                                if (bnSaveList[i].getText().toString().indexOf(default_name) != 0)
                                    feData.flow.loadSection(i, 0);
                                break;
                            //删除
                            case 3:
                                //确认为非空存档
                                if (bnSaveList[i].getText().toString().indexOf(default_name) != 0) {
                                    //删除
                                    feData.assets.save.delSx(i);
                                    //刷新
                                    refresh();
                                }
                                break;
                            //复制
                            case 4:
                                //点击被复制项
                                if (currnt_select < 0) {
                                    //确认为非空存档
                                    if (bnSaveList[i].getText().toString().indexOf(default_name) != 0) {
                                        //标记
                                        currnt_select = i;
                                        v.setAlpha(0.5f);
                                    }
                                }
                                //点击复制到位置
                                else {
                                    //不是选中那条
                                    if (i != currnt_select) {
                                        //复制
                                        feData.assets.save.copySx(i, currnt_select);
                                        //解除标记
                                        bnSaveList[currnt_select].setAlpha(1.0f);
                                        currnt_select = -1;
                                        //刷新
                                        reload();
                                    }
                                    //点击了选中那条,解除选中状态
                                    else
                                        currnt_select = -1;
                                }
                                break;
                            //通关保存
                            case 5:
                                break;
                        }
                    }
                }
            }
            //不返回true的话ACTION_DOWN之后的事件都会被丢弃
            return true;
        }
    };

    private Button buildButtonStyle(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextColor(0xFFFFFFFF);
        button.setTextSize(24);
        button.setGravity(Gravity.CENTER);
        button.setOnTouchListener(onTouchListener);
        button.setBackground(Drawable.createFromStream(getClass().getResourceAsStream("/assets/menu/item/item_save_g.png"), null));
        button.setPadding(80, 0, 80, 0);
        return button;
    }

    /*
        刷新按键内容
     */
    public void refresh() {
        //更新存档状态(saveState[][]的状态)
        saveState = feData.saveLoad();
        //更新词条
        for (int i = 0; i < bnSaveList.length; i++) {
            int h = saveState[i][2] / 3600;
            int m = saveState[i][2] % 3600 / 60;
            int s = saveState[i][2] % 60;
            if (saveState[i][0] >= 0)
                bnSaveList[i].setText(String.format("第%d章 XXX %02d:%02d:%02d", saveState[i][0], h, m, s));
            else
                bnSaveList[i].setText(default_name);
        }
    }

    /*
        刷新内容
     */
    public void reload() {

        this._removeViewAll(this);

        /* ----- 数据初始化 -----*/
        //更新存档状态(saveState[][]的状态)
        saveState = feData.saveLoad();
        //初始化
        bnSaveList = new Button[feData.saveNum()];
        for (int i = 0; i < bnSaveList.length; i++) {
            int h = saveState[i][2] / 3600;
            int m = saveState[i][2] % 3600 / 60;
            int s = saveState[i][2] % 60;
            if (saveState[i][0] >= 0)
                bnSaveList[i] = buildButtonStyle(
                        feData.context, String.format("第%d章 XXX %02d:%02d:%02d", saveState[i][0], h, m, s));
            else
                bnSaveList[i] = buildButtonStyle(feData.context, default_name);
        }
        //创建线性布局窗体
        linearLayout = new LinearLayout(feData.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //创建线性布局窗体参数
        bnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bnLayoutParams.setMargins(0, 0, 0, 30);
        //线性布局窗体相对主界面位置参数
        linearLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayoutParam.addRule(RelativeLayout.CENTER_VERTICAL);
        //添加条目到视图
        for (int i = 0; i < bnSaveList.length; i++)
            linearLayout.addView(bnSaveList[i], bnLayoutParams);

        //刷新按键内容
        refresh();

        /* ----- 界面装载 -----*/

        //显示列表
        this.addView(linearLayout, linearLayoutParam);
        this.setBackgroundColor(0x80404080);
    }

    /*
        ctrl 0/新建 1/继续 2/加载(或继续) 3/删除 4/复制 5/通关存档
     */
    public FeLayoutSave(FeData feData, int ctrl) {
        super(feData.context);
        this.feData = feData;
        this.ctrl = ctrl;
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
