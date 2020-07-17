package fans.develop.fe;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/*
    按行写文件工具,文件会写到手机内存FE文件夹内
    使用顺序:
        1.构造函数
        2.ready() //检查文件打开成功
        3.重复 write() 或者 writeLine()
        6.exit() //关闭各种句柄
 */
public class FeFileWrite {
    //关键路径
    private String feSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEX";
    private String filePath;
    //各种玄学句柄
    private FileOutputStream fos;
    private OutputStreamWriter osw;

    // autoCRCL: 是否自动补上"\r\n"
    public void write(String value, Boolean autoCRCL){
        try {
            if (osw != null){
                osw.write(value);
                if(autoCRCL)
                    osw.write("\r\n");
                osw.flush();
            }
        } catch (java.io.IOException e) {
            Log.d("FeFileWrite.write", "IOException : " + filePath);
        }
    }

    // 用分隔符 spl 分隔后面输入的 argv.length 个字符串
    public void writeLine(String spl, String ... argv){
        try {
            if (osw != null){
                for(int i = 0; i < argv.length; i++){
                    osw.write(argv[i]);
                    osw.write(spl);
                }
                osw.write("\r\n");
                osw.flush();
            }
        } catch (java.io.IOException e) {
            Log.d("FeFileWrite.writeLine", "IOException : " + filePath);
        }
    }

    /*
        folder: 目标文件夹, 示例 "/unit/" 前后都带斜杠
        name: 目标名称, 示例 "test.txt" 没有斜杠
     */
    public FeFileWrite(String folder, String name) {
        //文件夹检查
        File sdFileFolderPath = new File(feSdRootPath + folder);
        if(!sdFileFolderPath.exists())
            sdFileFolderPath.mkdirs();
        //文件写到sd卡(内置)存储
        filePath = feSdRootPath + folder + name;
        try {
            fos = new FileOutputStream(filePath);
            if(fos != null)
                osw = new OutputStreamWriter(fos, "UTF-8");
        }catch (java.io.FileNotFoundException e) {
            Log.d("FeFileWrite.open", "not found : " + filePath);
        }catch (java.io.UnsupportedEncodingException e) {
            Log.d("FeFileWrite.open", "UnsupportedEncodingException : " + filePath);
        }
    }

    // 文件打开就绪
    public Boolean ready(){
        if(osw != null)
            return true;
        return false;
    }

    // 关闭各种句柄
    public void exit(){
        try {
            if(osw != null)
                osw.close();
            osw = null;
            if (fos != null)
                fos.close();
            fos = null;
        } catch (java.io.IOException e) {
            Log.d("FeFileWrite.exit", "IOException : " + filePath);
        }
    }
}
