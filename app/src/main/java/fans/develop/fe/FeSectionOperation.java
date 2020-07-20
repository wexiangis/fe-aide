package fans.develop.fe;

import android.view.MotionEvent;

/*
    系统事件流: 触屏事件 -> 分发事件到各控件 -> 控件变更
 */
public class FeSectionOperation {

    private FeSectionCallback sectionCallback;

    //用来松开时判断是不是拖动后的松开
    private Boolean isMove = false;
    //记住点击位置,和拖动后的位置进行比较
    private float tDownX, tDownY;
    //在触屏 down 事件时,标记谁需要拖动事件?
    private FeFlagHit flagMove = new FeFlagHit();

    public FeSectionOperation(FeSectionCallback sectionCallback){
        this.sectionCallback = sectionCallback;
    }

    /*
        一切事件的源头皆来自触屏
     */
    public Boolean onTouchEvent(MotionEvent event)
    {
        //禁用触屏,丢弃该轮触屏事件
        if(!sectionCallback.onTouchEnable())
            return false;

        //触屏产生的 down、move、up 事件
        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN: {
                //记录坐标
                tDownX = event.getX();
                tDownY = event.getY();

                //清标记
                isMove = false;
                flagMove.cleanFlagAll();

                //检查点击都命中了谁?
                FeFlagHit flag = sectionCallback.checkHit(tDownX, tDownY);

                //谁需要拖动事件?
                if(flag.checkFlag(FeFlagHit.HIT_SYS_MENU)){
                    ;
                }
                else if(flag.checkFlag(FeFlagHit.HIT_CHAT)){
                    ;
                }
                else if(flag.checkFlag(FeFlagHit.HIT_UNIT_MENU)){
                    ;
                }
                else if(flag.checkFlag(FeFlagHit.HIT_MAP_INFO)){
                    ;
                }
                else if(flag.checkFlag(FeFlagHit.HIT_UNIT)
                    || flag.checkFlag(FeFlagHit.HIT_MARK)
                    || flag.checkFlag(FeFlagHit.HIT_MAP)){
                    flagMove.setFlag(FeFlagHit.HIT_MAP);
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                //记录坐标,取差值
                float tMoveX = event.getX();
                float tMoveY = event.getY();
                float xErr = tMoveX - tDownX;
                float yErr = tMoveY - tDownY;
                //产生的拖动格数
                int xGridErr = 0, yGridErr = 0;
                //横向拖动是否满一格像素,是就拖动一格
                if (Math.abs(xErr) > sectionCallback.getSectionMap().xGridPixel) {
                    xGridErr = xErr < 0 ? (1) : (-1);
                    //更新坐标
                    tDownX = tMoveX;
                    //置标记
                    isMove = true;
                }
                //纵向拖动是否满一格像素,是就拖动一格
                if (Math.abs(yErr) > sectionCallback.getSectionMap().yGridPixel) {
                    yGridErr = yErr < 0 ? (1) : (-1);
                    //更新坐标
                    tDownY = tMoveY;
                    //置标记
                    isMove = true;
                }
                //谁需要拖动事件?
                if(flagMove.checkFlag(FeFlagHit.HIT_SYS_MENU)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_CHAT)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_UNIT_MENU)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_MAP_INFO)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_UNIT)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_MARK)){
                    ;
                }
                else if(flagMove.checkFlag(FeFlagHit.HIT_MAP)){
                    sectionCallback.getLayoutMap().move(xGridErr, yGridErr);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                //取坐标
                float tUpX = event.getX();
                float tUpY = event.getY();

                //清标记
                sectionCallback.onMapMove(false);
                sectionCallback.onMapHit(false);

                //拖动结束
                if(isMove){
                    ;
                }
                
                //点击事件
                else{
                    //检查点击都命中了谁?
                    FeFlagHit flag = sectionCallback.checkHit(tUpX, tUpY);
                    int hitType = -1;

                    //按优先级选定唯一点击对象
                    if(flag.checkFlag(FeFlagHit.HIT_SYS_MENU)) hitType = FeFlagHit.HIT_SYS_MENU;
                    else if(flag.checkFlag(FeFlagHit.HIT_CHAT)) hitType = FeFlagHit.HIT_CHAT;
                    else if(flag.checkFlag(FeFlagHit.HIT_UNIT_MENU)) hitType = FeFlagHit.HIT_UNIT_MENU;
                    else if(flag.checkFlag(FeFlagHit.HIT_MAP_INFO)) hitType = FeFlagHit.HIT_MAP_INFO;
                    else if(flag.checkFlag(FeFlagHit.HIT_UNIT)) hitType = FeFlagHit.HIT_UNIT;
                    else if(flag.checkFlag(FeFlagHit.HIT_MARK)) hitType = FeFlagHit.HIT_MARK;
                    else if(flag.checkFlag(FeFlagHit.HIT_MAP)) hitType = FeFlagHit.HIT_MAP;
                    flag.setOnlyFlag(hitType);

                    //即使没有被选中,也会得到一个非己点击事件,用来清除原点击状态
                    sectionCallback.getLayoutSysMenu().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_SYS_MENU), hitType);
                    sectionCallback.getLayoutChat().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_CHAT), hitType);
                    sectionCallback.getLayoutUnitMenu().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_UNIT_MENU), hitType);
                    sectionCallback.getLayoutMapInfo().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_MAP_INFO), hitType);
                    sectionCallback.getLayoutUnit().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_UNIT), hitType);
                    sectionCallback.getLayoutMark().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_MARK), hitType);
                    sectionCallback.getLayoutMap().click(tUpX, tUpY, flag.checkFlag(FeFlagHit.HIT_MAP), hitType);
                }

                //清标记
                isMove = false;
            }
            break;
        }
        //否则触屏拖动和松开事件不会进来
        return true;
    }
}
