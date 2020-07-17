package fans.develop.fe;


import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
    地图信息(FeMapInfo类)的加载工具
 */
class FeReaderMap {

    //关键路径
    private String feSdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEX";
    private String assetsFilePath, sdFilePath;

    private class FeFileAllLine{
        public String line;
        public FeFileAllLine next;
        public FeFileAllLine(String line){
            this.line = line;
        }
    }

    private void _load_map_info_txt(FeInfoMap mapInfo, FeFileAllLine ffal, int total) {
        mapInfo.name = new String[total];
        mapInfo.defend = new short[total];
        mapInfo.avoid = new short[total];
        mapInfo.plus = new short[total];
        mapInfo.mov = new short[total];
        mapInfo.type = new short[total];
        mapInfo.info = new String[total];
        for(int i = 0; i < total && ffal != null; i++)
        {
            String[] lineData = ffal.line.split(";");
            if(lineData.length > 1)  mapInfo.name[i] = lineData[1];
            if(lineData.length > 2) mapInfo.defend[i] = (short)Integer.parseInt(lineData[2]);
            if(lineData.length > 3) mapInfo.avoid[i] = (short)Integer.parseInt(lineData[3]);
            if(lineData.length > 4) mapInfo.plus[i] = (short)Integer.parseInt(lineData[4]);
            if(lineData.length > 5) mapInfo.mov[i] = (short)Integer.parseInt(lineData[5]);
            if(lineData.length > 6) mapInfo.type[i] = (short)Integer.parseInt(lineData[6]);
            if(lineData.length > 7) mapInfo.info[i] = lineData[7];
            ffal = ffal.next;
        }
    }

    private void load_map_info_txt(FeInfoMap mapInfo){
        FeFileAllLine ffal = null, next = null;
        int lineCount = 0;
        String line = null;
        //
        File sdGridInfoPath = new File(feSdRootPath + "/map/grid_info.txt");
        try {
            if(sdGridInfoPath.exists()) {
                FileInputStream fis = new FileInputStream(sdGridInfoPath.getPath());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                //分行读取
                while ((line = br.readLine()) != null) {
                    if(next == null) ffal = next = new FeFileAllLine(line);
                    else next = next.next = new FeFileAllLine(line);
                    lineCount += 1;
                }
                br.close();
                isr.close();
                fis.close();
            }
            else {
                InputStream is = getClass().getResourceAsStream("/assets/map/grid_info.txt");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                //分行读取
                while ((line = br.readLine()) != null) {
                    if(next == null) ffal = next = new FeFileAllLine(line);
                    else next = next.next = new FeFileAllLine(line);
                    lineCount += 1;
                }
                br.close();
                isr.close();
                is.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("FeInfoMap.load_map_info", "not found : grid_info.txt");
        } catch (java.io.IOException e) {
            Log.d("FeInfoMap.load_map_info", "IOException : grid_info.txt");
        }
        //解析链表
        _load_map_info_txt(mapInfo, ffal, lineCount);
    }

    private void _load_grid_txt(FeInfoMap mapInfo, String line, int count) {
        String[] lineData = line.split(";");
        for(int i = 0; i < mapInfo.xGrid && i < lineData.length; i++)
            mapInfo.grid[count][i] = (short)Integer.parseInt(lineData[i]);
    }

    private void load_grid_txt(FeInfoMap mapInfo){
        File sdGridPath = new File(sdFilePath + "grid.txt");
        String line = null;
        int count = 0;
        //重新分配二维数组
        mapInfo.grid = new short[mapInfo.yGrid][mapInfo.xGrid];
        try {
            if(sdGridPath.exists()) {
                FileInputStream fis = new FileInputStream(sdGridPath.getPath());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                //分行读取
                while ((line = br.readLine()) != null) {
                    _load_grid_txt(mapInfo, line, count++);
                    if(count >= mapInfo.yGrid)
                        break;
                }
                br.close();
                isr.close();
                fis.close();
            }
            else {
                InputStream is = getClass().getResourceAsStream(assetsFilePath + "grid.txt");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                //分行读取
                while ((line = br.readLine()) != null) {
                    _load_grid_txt(mapInfo, line, count++);
                    if(count >= mapInfo.yGrid)
                        break;
                }
                br.close();
                isr.close();
                is.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("FeInfoMap.load_grid_txt", "not found : grid.txt");
        } catch (java.io.IOException e) {
            Log.d("FeInfoMap.load_grid_txt", "IOException : grid.txt");
        }
    }

    private void load_map_jpg(FeInfoMap mapInfo){
        File sdBitmapPath = new File(sdFilePath + "map.jpg");
        if(sdBitmapPath.exists())
            mapInfo.bitmap = BitmapFactory.decodeFile(sdBitmapPath.getPath());
        else{
            try{
                InputStream is = getClass().getResourceAsStream(assetsFilePath + "map.jpg");
                if(is != null){
                    mapInfo.bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("FeInfoMap.load_map_jpg", "not found : map.jpg");
            } catch (IOException e) {
                Log.d("FeInfoMap.load_map_jpg", "IOException : map.jpg");
            }
        }
    }

    private void _load_size_txt(FeInfoMap mapInfo, byte[] line){
        String[] dat = new String(line).split(";");
        if(dat.length > 0) mapInfo.xGrid = Integer.parseInt(dat[0]);
        if(dat.length > 1) mapInfo.yGrid = Integer.parseInt(dat[1]);
        if(dat.length > 2) mapInfo.pixelPerGrid = Integer.parseInt(dat[2]);
        if(dat.length > 3) mapInfo.transferGrid = Integer.parseInt(dat[3]);
    }

    private void load_size_txt(FeInfoMap mapInfo){
        File sdSizePath = new File(sdFilePath + "size.txt");
        try {
            if(sdSizePath.exists()) {
                FileInputStream fis = new FileInputStream(sdSizePath.getPath());
                byte[] line = new byte[100];
                if (fis.read(line) > 0)
                    _load_size_txt(mapInfo, line);
                fis.close();
            }
            else {
                InputStream is = getClass().getResourceAsStream(assetsFilePath + "size.txt");
                byte[] line = new byte[100];
                if (is.read(line) > 0)
                    _load_size_txt(mapInfo, line);
                is.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("FeInfoMap.load_size_txt", "not found : size.txt");
        } catch (java.io.IOException e) {
            Log.d("FeInfoMap.load_size_txt", "IOException : size.txt");
        }
    }

    public void getFeMapInfo(FeInfoMap mapInfo, int section) {
        String mapPath = "/map/map" + String.format("%02d/",section);
        assetsFilePath = "/assets" + mapPath;
        sdFilePath = feSdRootPath + mapPath;

        load_map_jpg(mapInfo);
        load_size_txt(mapInfo);
        load_grid_txt(mapInfo);
        load_map_info_txt(mapInfo);
    }

}