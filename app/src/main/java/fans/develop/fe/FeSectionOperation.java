package fans.develop.fe;

import android.view.MotionEvent;

/*
    触屏操作事件流的管理
    系统事件流: 触屏事件 -> 分发事件到各控件 -> 控件自动变更
 */
public class FeSectionOperation {

    private FeSectionCallback sectionCallback;

    //记住点击位置,和拖动后的位置进行比较
    private float tDownX, tDownY;
    //在触屏 down 事件时,标记谁需要拖动事件?
    private FeFlagHit flagMove = new FeFlagHit();

    public FeSectionOperation(FeSectionCallback sectionCallback) {
        this.sectionCallback = sectionCallback;
    }

    /*
        一切事件的源头皆来自触屏
     */
    public Boolean onTouchEvent(MotionEvent event) {
        //禁用触屏,丢弃该轮触屏事件
        if (sectionCallback.onTouchDisable())
            return false;

        //触屏产生的 down、move、up 事件
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                //记录坐标
                tDownX = event.getX();
                tDownY = event.getY();

                //清标记
                sectionCallback.onTouchMov(false);
                flagMove.cleanFlagAll();

                //检查点击都命中了谁?
                FeFlagHit flag = sectionCallback.checkHit(tDownX, tDownY);

                //谁需要拖动事件?
                if (flag.checkFlag(FeFlagHit.HIT_SYS_MENU)) {
                    ;
                } else if (flag.checkFlag(FeFlagHit.HIT_CHAT)) {
                    ;
                } else if (flag.checkFlag(FeFlagHit.HIT_UNIT_MENU)) {
                    flagMove.setFlag(FeFlagHit.HIT_UNIT_MENU);
                } else if (flag.checkFlag(FeFlagHit.HIT_MAP_INFO)) {
                    ;
                } else if (flag.checkFlag(FeFlagHit.HIT_UNIT)
                        || flag.checkFlag(FeFlagHit.HIT_MARK)
                        || flag.checkFlag(FeFlagHit.HIT_MARK_ENEMY)
                        || flag.checkFlag(FeFlagHit.HIT_MAP)) {
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
                //拖动是否满10像素,是就开始拖动
                if (!sectionCallback.onTouchMov() &&
                        (Math.abs(xErr) > 10 || Math.abs(yErr) > 10)) {
                    //置标记
                    sectionCallback.onTouchMov(true);
                }
                //已经开始移动了？
                if (sectionCallback.onTouchMov()) {
                    //更新坐标
                    tDownX = tMoveX;
                    tDownY = tMoveY;
                }
                //否则无移动量
                else {
                    xErr = yErr = 0;
          break;
                }
                //谁需要拖动事件?
                if (flagMove.checkFlag(FeFlagHit.HIT_SYS_MENU)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_CHAT)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_UNIT_MENU)) {
                    sectionCallback.getLayoutUnitMenu().move(xErr, yErr);
                } else if (flagMove.checkFlag(FeFlagHit.HIT_MAP_INFO)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_UNIT)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_MARK)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_MARK_ENEMY)) {
                    ;
                } else if (flagMove.checkFlag(FeFlagHit.HIT_MAP)) {
                    sectionCallback.getLayoutMap().move(xErr, yErr);
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
                if (sectionCallback.onTouchMov()) {
                    ;
                }

                //点击事件
                else {
                    //检查点击都命中了谁?
                    FeFlagHit flag = sectionCallback.checkHit(tUpX, tUpY);

                    //按优先级选定唯一点击对象
                    if (flag.checkFlag(FeFlagHit.HIT_SYS_MENU))
                        flag.setOnlyFlag(FeFlagHit.HIT_SYS_MENU);
                    else if (flag.checkFlag(FeFlagHit.HIT_CHAT))
                        flag.setOnlyFlag(FeFlagHit.HIT_CHAT);
                    else if (flag.checkFlag(FeFlagHit.HIT_UNIT_MENU))
                        flag.setOnlyFlag(FeFlagHit.HIT_UNIT_MENU);
                    else if (flag.checkFlag(FeFlagHit.HIT_MAP_INFO))
                        flag.setOnlyFlag(FeFlagHit.HIT_MAP_INFO);
                    else if (flag.checkFlag(FeFlagHit.HIT_UNIT))
                        flag.setOnlyFlag(FeFlagHit.HIT_UNIT);
                    else if (flag.checkFlag(FeFlagHit.HIT_MARK))
                        flag.setOnlyFlag(FeFlagHit.HIT_MARK);
                    else if (flag.checkFlag(FeFlagHit.HIT_MARK_ENEMY))
                        flag.setOnlyFlag(FeFlagHit.HIT_MARK_ENEMY);
                    else if (flag.checkFlag(FeFlagHit.HIT_MAP)) flag.setOnlyFlag(FeFlagHit.HIT_MAP);

                    //即使没有被选中,也会得到一个非己点击事件,用来清除原点击状态
                    sectionCallback.getLayoutSysMenu().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutChat().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutUnitMenu().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutMapInfo().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutUnit().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutMark().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutMarkEnemy().click(tUpX, tUpY, flag);
                    sectionCallback.getLayoutMap().click(tUpX, tUpY, flag);

                }

                //清标记
                sectionCallback.onTouchMov(false);
            }
            break;
        }
        //否则触屏拖动和松开事件不会进来
        return true;
    }
}
