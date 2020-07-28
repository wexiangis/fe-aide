package fans.develop.fe;

import android.view.*;
import android.widget.*;

/*
    片头动画
 */
public class FeLayoutOpening extends FeLayout {


    private FeData feData;
    private TextView textView;
    private FeAsyncTask asyncTask = null;

    //触屏事件回调函数
    private View.OnTouchListener onTouchListener  = new View.OnTouchListener (){
        public boolean onTouch(View v, MotionEvent event) {
            //触屏UP时, 跳过openning动画
            if(event.getAction() == MotionEvent.ACTION_UP){
                //关闭后台线程
                asyncTask.cancel(true);
                //界面跳转
                feData.flow.loadTheme();
                //内存回收
                asyncTask = null;
            }
            //不返回true的话ACTION_DOWN之后的事件都会被丢弃
            return true;
        }
    };

    public void reload(){

        this._removeViewAll(this);
        
        asyncTask = new FeAsyncTask(this, new FeAsyncTask.Callback<FeLayoutOpening>() {

                @Override
                public void onPreExecute(FeLayoutOpening layoutOpening) {

                    textView = new TextView(feData.context);
                    textView.setText("Opening ...");
                    textView.setTextSize(32);
                    textView.setTextColor(0xFFFF0000);

                    //text相对主界面的位置
                    RelativeLayout.LayoutParams tvLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tvLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    textView.setLayoutParams(tvLayoutParams);

                    //loading相对主界面的位置
                    RelativeLayout.LayoutParams ldLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    ldLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    layoutOpening.setLayoutParams(ldLayoutParams);

                    layoutOpening.addView(textView);
                    layoutOpening.setBackgroundColor(0x80008080);
                    layoutOpening.setOnTouchListener(onTouchListener);
                }

                @Override
                public String doInBackground(FeLayoutOpening layoutOpening, Integer... integers) {
                    try{
                        for(int i = 0; i < 100; i++){
                            if(asyncTask.isCancelled())
                                return "break";
                            Thread.sleep(10);
                            asyncTask.setPercent(i);
                        }
                    }catch(java.lang.InterruptedException e) {
                        return "break";
                    }
                    return null;
                }

                @Override
                public Integer onProgressUpdate(FeLayoutOpening layoutOpening, Integer... values) {
                    if(values.length > 0)
                        textView.setText(String.format("Opening .. %d", values[0]));
                    else
                        textView.setText("error percent !!");
                    return null;
                }

                @Override
                public void onPostExecute(FeLayoutOpening layoutOpening, String result) {
                    if(result != "break")
                        feData.flow.loadTheme();
                }
            });

        asyncTask.execute(0);

        /* ----- 数据初始化 -----*/

//        //按任意键继续
//        textView = new TextView(feData.context);
//        textView.setText("opening");
//        textView.setTextColor(0x80FFFFFF);
//        textView.setTextSize(32);
//        //相对主界面的位置
//        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        //添加布局参数
//        textView.setLayoutParams(layoutParams);
//
//        /* ----- 界面装载 -----*/
//
//        //添加到主界面
//        this.addView(textView);
//        //背景
//        this.setBackgroundColor(0xFF004040);
//        this.setOnTouchListener(onTouchListener);
    }
    
    public FeLayoutOpening(FeData feData){
        super(feData.context);
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        asyncTask.cancel(true);
        //释放子view
        _removeViewAll(this);
        return true;
    }
    public void onReload(){
        this.reload();
    }
}
