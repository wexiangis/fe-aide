package fans.develop.fe;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
    按行读文件工具,手机内存FE文件夹内有文件会优先读取,否则读/assets目录文件
    使用顺序:
        1.构造函数
        2.ready() //检查文件打开成功
        3.readLine() //返回 true/读取一行数据就绪 false/文件读完了
        4.getContent() 或者 getLineContent() 获取该行数据
        5.重复3,4
        6.exit() //关闭各种句柄
 */
public class FeFileRead {
    //关键路径
    private String feSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEX";
    private String filePath;
    //各种玄学句柄
    private FileInputStream fis;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;
    //每次调用 readLine() 后得到的数据
    private String lineContent;//行原始数据
    private String[] content;//按分隔符处理后的行数据
    private int line = 0;//当前行数

    // 返回按分隔符分割好的一行数据
    public String[] getContent() {
        return content;
    }

    // 返回一行原始数据
    public String getLineContent() {
        return lineContent;
    }

    // 当前读取行号, 从0数起
    public int getLine() {
        return line;
    }

    // 返回改行 String[count] 的数据
    public int getInt(int count) {
        if (content != null && content.length > count)
            return FeFormat.StringToInt(content[count]);
        return -1;
    }

    // 返回改行 String[count] 的数据
    public String getString(int count) {
        if (content != null && content.length > count)
            return content[count];
        return "";
    }

    // spl: 分隔符, 一般为";"
    public boolean readLine(String spl) {
        try {
            if (br == null)
                return false;
            lineContent = br.readLine();
            if (lineContent == null)
                return false;
            content = lineContent.split(spl);
            if (content == null) {
                lineContent = null;
                return false;
            }
            line += 1;
            return true;
        } catch (IOException e) {
            Log.d("FeFileRead.readLine", "IOException : " + filePath);
        }
        return false;
    }

    /*
        folder: 目标文件夹, 示例 "/unit/" 前后都带斜杠
        name: 目标名称, 示例 "test.txt" 没有斜杠
     */
    public FeFileRead(String folder, String name) {
        File assetsFilePath = new File("/assets" + folder + name);
        File sdFilePath = new File(feSdRootPath + folder + name);
        File sdFileFolderPath = new File(feSdRootPath + folder);
        //sd卡(内置存储)路径准备
        if (!sdFileFolderPath.exists())
            sdFileFolderPath.mkdirs();
        //存在sd卡(内置存储)配置则优先使用该配置
        try {
            //存在于手机内存
            if (sdFilePath.exists()) {
                filePath = sdFilePath.getPath();
                fis = new FileInputStream(filePath);
            }
            //否则尝试从assets打开
            else {
                filePath = assetsFilePath.getPath();
                is = getClass().getResourceAsStream(filePath);
            }
            //获得reader
            if (fis != null) {
                isr = new InputStreamReader(fis, "UTF-8");
                br = new BufferedReader(isr);
            } else if (is != null) {
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("FeFileRead.FeFileRead", "not found : " + filePath);
        } catch (IOException e) {
            Log.d("FeFileRead.FeFileRead", "IOException : " + filePath);
        }
    }

    // 文件打开就绪
    public Boolean ready() {
        if (br != null)
            return true;
        return false;
    }

    // 关闭各种句柄
    public void exit() {
        try {
            if (br != null)
                br.close();
            br = null;
            if (isr != null)
                isr.close();
            isr = null;
            if (is != null)
                is.close();
            is = null;
            if (fis != null)
                fis.close();
            fis = null;
        } catch (IOException e) {
            Log.d("FeFileRead.exit", "IOException : " + filePath);
        }
    }
}