package fans.develop.fe;

import android.os.AsyncTask;

/*
    二次封装FeAsyncTask的回调接口,引入私有数据obj
 */
public class FeAsyncTask extends AsyncTask<Integer, Integer, String> {

    // 第一个参数：传入 doInBackground() 方法的参数类型
    // 第二个参数：传入 onProgressUpdate() 方法的参数类型
    // 第三个参数：传入 onPostExecute() 方法的参数类型，也是 doInBackground() 方法返回的类型

    public interface Callback<U> {
        public void onPreExecute(U obj);// UI操作

        public String doInBackground(U obj, Integer... integers);// 后台操作

        public Integer onProgressUpdate(U obj, Integer... values);// UI操作

        public void onPostExecute(U obj, String result);// UI操作
    }

    private Object obj;
    private FeAsyncTask.Callback callback;

    /*
        obj: 私有数据
        callback: 多了obj参数的回调函数,要"FeAsyncTask.Callback<XXX>"方式初始化
     */
    public FeAsyncTask(Object obj, FeAsyncTask.Callback callback) {
        this.obj = obj;
        this.callback = callback;
    }

    /*
        在 doInBackground 发消息到 onProgressUpdate
     */
    public void setPercent(Integer... percents) {
        publishProgress(percents);
    }

    /*
        方法1：onPreExecute（）
        作用：执行 线程任务前的操作
        注：根据需求复写
     */
    @Override
    protected void onPreExecute() {
        // UI操作
        callback.onPreExecute(obj);
    }

    /*
         方法2：doInBackground（）
         作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
         注：必须复写，从而自定义线程任务
     */
    @Override
    protected String doInBackground(Integer... integers) {
        // 后台操作
        return callback.doInBackground(obj, integers); // 跳转到 onPostExecute()

        // 通知 onProgressUpdate（）
        // publishProgress(99, 0, 1, ...);
    }

    /*
         方法3：onProgressUpdate（）
         作用：在主线程 显示线程任务执行的进度
         注：根据需求复写
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        // UI操作
        callback.onProgressUpdate(obj, values);
    }

    /*
         方法4：onPostExecute（）
         作用：接收线程任务执行结果、将执行结果显示到UI组件
         注：必须复写，从而自定义UI操作
     */
    @Override
    protected void onPostExecute(String result) {
        // UI操作
        callback.onPostExecute(obj, result);
    }

//    // 方法5：onCancelled()
//    // 作用：将异步任务设置为：取消状态
//    @Override
//    protected void onCancelled() {
//        // UI操作
//        ;
//    }
}

/* ***************************************************************************

    // 步骤1：创建 AsyncTask 子类的实例对象（即 任务实例）
    // 注：AsyncTask 子类的实例必须在UI线程中创建
    FeAsyncTask fat = new FeAsyncTask();

    // 步骤2：手动调用 execute(Params... params) 从而执行异步线程任务
    // 注：
    //   a. 必须在UI线程中调用
    //   b. 同一个 AsyncTask 实例对象只能执行1次，若执行第2次将会抛出异常
    //   c. 执行任务中，系统会自动调用 AsyncTask 的一系列方法：onPreExecute() 、doInBackground()、onProgressUpdate() 、onPostExecute()
    //   d. 不能手动调用上述方法
    fat.execute(params);

*************************************************************************** */
