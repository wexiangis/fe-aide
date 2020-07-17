package fans.develop.fe;

import android.view.ViewGroup;

/*
    流程管理: 用于来回切换各个界面
 */
public class FeFlow {

    private FeData feData;

    public FeFlow(FeData feData){
        this.feData = feData;
    }

    //activity被暂停时调用
    public void stop(){
        if(feData.layoutCurrent != null &&
            feData.layoutCurrent.getParent() != null)
            ((ViewGroup)feData.layoutCurrent.getParent()).removeAllViews();
    }

    //activity从暂停中恢复时调用
    public void start(){
        //恢复
        if(feData.layoutCurrent != null){
            feData.activity.setContentView(feData.layoutCurrent);
            return;
        }
        //加载OP
        loadOpening();
    }

    //加载片头
    public void loadOpening() {
        //播放片头
        loadNext(new FeLayoutOpening(feData));
    }

    //加载主界面
    public void loadTheme(){
        loadNext(new FeLayoutTheme(feData));
    }

    //加载职业动画
    public void loadProfessionAnim(){
        ;
    }

    //加载主界面菜单
    public void loadMainMenu(){
        loadNext(new FeLayoutMainMenu(feData));
    }

    //加载存档界面: ctrl 0/新建 1/继续 2/加载(或继续) 3/删除 4/复制 5/通关存档
    public void loadSaveMenu(int ctrl){
        loadNext(new FeLayoutSave(feData, ctrl));
    }

    //加载额外内容
    public void loadExtraMenu(){
        loadNext(new FeLayoutExtra(feData));
    }

    //加载章节片头
    public void loadSectionTheme(int sX){
        loadNext(new FeLayoutSectionTheme(feData, sX));
    }

    //加载章节初始剧情
    public void loadSectionPlot(FeAssetsSX sxData){
        loadNext(new FeLayoutSectionPlot(feData, sxData));
    }

    //加载章节
    //sX: 存档位置 mode: 0/重新加载 1/中断继续
    public void loadSection(int sX, int mode) {
        loadNext(new FeLayoutSection(feData, sX, mode));
    }

    //加载章节结束剧情
    public void loadSectionPlotEnding(FeAssetsSX sxData){
        loadNext(new FeLayoutSectionPlotEnding(feData, sxData));
    }

    //加载GameOver
    public void loadGameOver(){
        loadNext(new FeLayoutGameOver(feData));
    }

    //系统的界面返回, 返回false表示没有上一级界面了
    public boolean loadLast(){
        //链表已空
        if(feData.layoutChain == null)
            return false;
        //销毁旧layout
        //返回false时表示控件未准备好,不继续操作
        if(feData.layoutCurrent.onDestory() == false)
            return true;
        //记录当前
        feData.layoutCurrent = feData.layoutChain.data;
        //出栈
        feData.layoutChain = feData.layoutChain.previous;
        //有需要reload的
        feData.layoutCurrent.onReload();
        //显示
        feData.activity.setContentView(feData.layoutCurrent);
        return true;
    }

    public void loadNext(FeLayout layout){
        //销毁旧layout
        //返回false时表示控件未准备好,不继续操作
        if(feData.layoutCurrent != null && feData.layoutCurrent.onDestory() == false)
            return;
        //入栈
        if(feData.layoutCurrent != null){
            if(feData.layoutChain == null)
                feData.layoutChain = new FeChain<FeLayout>(feData.layoutCurrent);
            else {
                feData.layoutChain.next = new FeChain<FeLayout>(feData.layoutCurrent);
                feData.layoutChain.next.previous = feData.layoutChain;
                feData.layoutChain = feData.layoutChain.next;
            }
        }
        //记录当前
        feData.layoutCurrent = layout;
        //需要的reload
        feData.layoutCurrent.onReload();
        //显示
        feData.activity.setContentView(feData.layoutCurrent);
    }
}
