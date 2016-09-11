package com.ebox.pub.file;

import android.os.Environment;
import android.util.Log;

import com.ebox.pub.model.RspGetAdvertise;
import com.ebox.pub.service.global.GlobalField;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mafeng on 2015/6/10.
 */
public class AdvFileOp {
    private static final String savefolder = Environment.getExternalStorageDirectory().getPath()+
            "/adv";

    public static void writeJsonToFile(String str)
    {
        try
        {

            File fileDir = new File(savefolder);

            if (!fileDir.exists() && !fileDir.mkdirs()) {
                Log.e(GlobalField.tag, "Can't create directory:" + savefolder);
            }
            File f = new File(savefolder + "/adv.json");
            if(!f.exists())
            {
                f.createNewFile();
            }

            FileOutputStream out=new FileOutputStream(f,false); //如果追加方式用true
            out.write(str.getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static RspGetAdvertise getAdvertiseFromJson()
    {
        RspGetAdvertise rsp = null;
        BufferedReader reader = null;
        StringBuffer tempString =new  StringBuffer();

        try
        {
            File f = new File(savefolder + "/adv.json");
            if(f.exists())
            {


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

            if(tempString.length()>0)
            {
                Gson gson = new Gson();
                rsp = gson.fromJson(tempString.toString(),RspGetAdvertise.class);
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }


        return  rsp;

    }
}
