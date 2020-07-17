package fans.develop.fe;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    进度加载界面,作为子view在各任务中显示
 */
public class FeLayoutLoading extends FeLayout{

    /*
        界面类型
     */
    public static final int TYPE_NORMAL = 0;

	/*
		提前结束loading界面
	 */
	public void cancel(boolean b){
		asyncTask.cancel(b);
	}
	
	/*
	 	检查结束标志
	 */
	public boolean isCancelled(){
		return asyncTask.isCancelled();
	}
	
    /*
        进度设置接口(用户调用)
     */
    public void setPercent(int percent){
        asyncTask.setPercent(percent);
    }

    /*
        后台运行任务(用户实现,期间通过percent.set(xxx),告知UI进度情况)
        返回: 成功返回null, 失败返回提示
     */
    public interface DoInBackground<U>{
        public String run(U obj, FeLayoutLoading layoutLoading);
    }

    /*
        UI任务(用户实现)
     */
    public interface DoInFinal<U>{
        public void run(U obj, String result);
    }

    /*
        public 为了能在callback中调用
     */
    public Context context;
    public FeAsyncTask asyncTask;
    public int type;
    public Object obj;
    public FeLayoutLoading.DoInBackground doInBackground;
    public FeLayoutLoading.DoInFinal doInFinal;

    /*
        type: 加载动画类型
        obj: 私有数据,传到 doInBackground、doInFinal 中的参数之一
        doInBackground:
            1.后台任务,由使用者定义;
            2.其携带参数 layoutLoading,用于修改UI进度,调用 layoutLoading.setPercent(xx)
            3.返回: 将作为 doInFinal 的传入参数
        doInFinal:
            1.doInBackground 结束后的UI任务,用于界面跳转
            2.携带参数 result 为 doInBackground 最后的返回
     */
    public FeLayoutLoading(Context context, final int type, Object obj, FeLayoutLoading.DoInBackground doInBackground, FeLayoutLoading.DoInFinal doInFinal){
        super(context);

        this.context = context;
        this.type = type;
        this.obj = obj;
        this.doInBackground = doInBackground;
        this.doInFinal = doInFinal;

        asyncTask = new FeAsyncTask(this, new FeAsyncTask.Callback<FeLayoutLoading>() {

            private TextView textView;

            @Override
            public void onPreExecute(FeLayoutLoading layoutLoading) {

                textView = new TextView(layoutLoading.context);
                textView.setText("wait");
                textView.setTextSize(32);
                textView.setTextColor(0xFFFF0000);

                //text相对主界面的位置
                RelativeLayout.LayoutParams tvLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                tvLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                tvLayoutParams.setMargins(0, 0, 0, 100);
                textView.setLayoutParams(tvLayoutParams);

                //loading相对主界面的位置
                RelativeLayout.LayoutParams ldLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ldLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                layoutLoading.setLayoutParams(ldLayoutParams);

                layoutLoading.addView(textView);
                layoutLoading.setBackgroundColor(0x80008080);
            }

            @Override
            public String doInBackground(FeLayoutLoading layoutLoading, Integer... integers) {
                if(layoutLoading.doInBackground != null)
                    return layoutLoading.doInBackground.run(layoutLoading.obj, layoutLoading);
                else
                    return null;
            }

            @Override
            public Integer onProgressUpdate(FeLayoutLoading layoutLoading, Integer... values) {
                if(values.length > 0)
                    textView.setText(String.valueOf(values[0]));
                else
                    textView.setText("error percent !!");
                return null;
            }

            @Override
            public void onPostExecute(FeLayoutLoading layoutLoading, String result) {
                if(layoutLoading.doInFinal != null)
                    layoutLoading.doInFinal.run(layoutLoading.obj, result);
            }
        });

        asyncTask.execute(0);
    }

    /* ---------- abstract interface ---------- */
    public boolean onKeyBack(){
        return false;
    }
    public boolean onDestory(){
        return true;
    }
    public void onReload(){
        ;
    }
}
