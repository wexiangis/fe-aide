package fans.develop.fe;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    用来创建、查询手机内存FE文件夹中的文件(注意不包括assets文件夹)
 */
public class FeFile{
    //关键路径
    private String feSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEX";

    /*
        folder: 目标文件夹, 示例 "/unit/" 前后都带斜杠
        name: 目标名称, 示例 "test.txt" 没有斜杠
     */
    public Boolean exists(String path) {
        return new File(feSdRootPath + path).exists();
    }

    public void delete(String path){
        _deleteFile(new File(feSdRootPath + path));
    }

    private void _deleteFile(File file){
        if (!file.exists())
            return;
        else if (file.isFile())
            file.delete();
        else {
            File[] allFile = file.listFiles();
            for (File f : allFile)
                this._deleteFile(f);
            file.delete();
        }
    }

    public void copy(String dist, String src){
        delete(feSdRootPath + dist);
        copyFolder(feSdRootPath + dist, feSdRootPath + src);
    }

    private void copyFolder(String dist, String src){

        File srcFile = new File(src);
        File distFile = new File(dist);

        //源文件不存在则结束
        if (!srcFile.exists())
            return;

        //文件直接拷贝
        if(srcFile.isFile()){
            copyFile(distFile, srcFile);
            return;
        }

        //不是文件也不是文件夹
        if(!srcFile.isDirectory())
            return;

        //目标文件夹不存在则创建
        if (!distFile.exists())
            distFile.mkdirs();

        // 获取源文件夹下的文件夹或文件
        File[] srcFiles = srcFile.listFiles();
        for (File sf : srcFiles) {
            // 复制文件
            if (sf.isFile())
                // File.separator分隔符在windows为"\",在linux为"/"
                copyFile(new File(distFile.getPath() + File.separator + sf.getName()), sf);
            // 复制文件夹
            else if (sf.isDirectory())
                //递归
                copyFolder(distFile.getPath() + File.separator + sf.getName(), sf.getPath());
        }
    }

    private void copyFile(File distFile, File srcFile){
        try{
            // I/O流
            FileInputStream inputStream = new FileInputStream(srcFile);
            if(inputStream == null)
                return;
            FileOutputStream outputStream = new FileOutputStream(distFile);
            if(outputStream == null){
                inputStream.close();
                return;
            }
            // 读写
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) > 0)
                outputStream.write(bytes, 0, len);
            // 清空缓冲
            outputStream.flush();
            // 关闭流
            inputStream.close();
            outputStream.close();
        }catch (java.io.FileNotFoundException e) {
            Log.d("FeFile.copyFile", "not found : src/" + srcFile.getPath() + " dist/" + distFile.getPath());
        }catch (IOException e) {
            Log.d("FeFile.copyFile", "IOException : src/" + srcFile.getPath() + " dist/" + distFile.getPath());
        }
    }

    /*
        defaultRet: 当找不到文件、打开失败或异常时的默认返回
        readLen: 读取多少字节(预计文件有多大)
     */
    public String readFile(String path, String defaultRet, int readLen) {
        File file = new File(feSdRootPath + path);
        if(!file.exists())
            return defaultRet;
        try{
            FileInputStream fis = new FileInputStream(file.getPath());
            if(fis == null)
                return defaultRet;
            byte[] buff = new byte[readLen];
            fis.read(buff);
            fis.close();
            return new String(buff);
        } catch (java.io.FileNotFoundException e) {
            Log.d("FeFile.readFile", "not found : " + file.getPath());
        } catch (IOException e) {
            Log.d("FeFile.readFile", "IOException : " + file.getPath());
        }
        return defaultRet;
    }

    /*
        创建或擦除写入文件
     */
    public void writeFile(String folder, String name, String value){
        File file = new File(feSdRootPath + folder + name);
        File fileParent = new File(feSdRootPath + folder);
        if(!fileParent.exists())
            fileParent.mkdirs();
        if(file.exists())
            file.delete();
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            if(fos != null)
            {
                fos.write(value.getBytes());
                fos.close();
            }
        }catch (java.io.FileNotFoundException e) {
            Log.d("FeFile.writeFile", "not found : " + file.getPath());
        }catch (IOException e) {
            Log.d("FeFile.writeFile", "IOException : " + file.getPath());
        }
    }
}