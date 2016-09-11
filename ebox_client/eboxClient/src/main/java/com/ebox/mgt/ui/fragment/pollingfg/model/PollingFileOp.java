package com.ebox.mgt.ui.fragment.pollingfg.model;

import android.os.Environment;

import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.pub.utils.LogUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by prin on 2015/9/30.
 * 将巡检信息写到本地文件中
 */
public class PollingFileOp {
    private static final String savefolder = Environment.getExternalStorageDirectory().getPath() + "/polling";

    /**
     * 巡检结果的json信息写入到本地文件中
     */
    public static void writeJsonToFile(String str, String fileName) {
        try {
            File fileDir = new File(savefolder);
            if (!fileDir.exists() && fileDir.mkdirs()) {
                LogUtil.e("can't create directory:" + savefolder);
            }
            File f = new File(savefolder + "/" + fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            FileWriter out = new FileWriter(f, true);  //如果追加方式用true
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(str);
            bw.flush();
            bw.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取巡检结果
     */
    public static ReqPolling getPollingTotalFromJson(String fileName) {
        ReqPolling rsp = null;
        BufferedReader reader = null;
        StringBuffer tempString = new StringBuffer();
        try {
            File f = new File(savefolder + "/" + fileName);
            if (f.exists()) {


                reader = new BufferedReader(new FileReader(f));

                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                String tempStr = "";
                while ((tempStr = reader.readLine()) != null) {
                    // 显示行号
                    tempString.append(tempStr);
                    line++;
                }
                reader.close();
            }

            if (tempString.length() > 0) {
                rsp=JsonSerializeUtil.json2Bean(tempString.toString(), ReqPolling.class);
            }


        } catch (Exception e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return rsp;
    }

    /**
     * 获得出厂数据
     */
    public static BoardModel getBoardFromJson() {
        BoardModel rsp = null;
        BufferedReader reader = null;
        StringBuffer tempString = new StringBuffer();

        try {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/hardtest" + "/board.json");
            if (f.exists()) {


                reader = new BufferedReader(new FileReader(f));

                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                String tempStr = "";
                while ((tempStr = reader.readLine()) != null) {
                    // 显示行号
                    tempString.append(tempStr);
                    line++;
                }
                reader.close();
            }

            if (tempString.length() > 0) {
                Gson gson = new Gson();
                LogUtil.i("PollingFileOp:" + tempString.toString());
//                rsp = gson.fromJson(tempString.toString(), BoardModel.class);
                rsp = JsonSerializeUtil.json2Bean(tempString.toString(), BoardModel.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }


        return rsp;

    }

    /**
     * 获得生产数据
     */
    public static ProductModel getProductFromJson() {
        ProductModel rsp = null;
        BufferedReader reader = null;
        StringBuffer tempString = new StringBuffer();

        try {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/hardtest" + "/product.json");
            if (f.exists()) {
                reader = new BufferedReader(new FileReader(f));
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                String tempStr = "";
                while ((tempStr = reader.readLine()) != null) {
                    // 显示行号
                    tempString.append(tempStr);
                    line++;
                }
                reader.close();
            }

            if (tempString.length() > 0) {
                Gson gson = new Gson();
                rsp = gson.fromJson(tempString.toString(), ProductModel.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }


        return rsp;

    }

    /**
     * 删除指定文件
     */
    public static void delFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }


}
