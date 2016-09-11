package com.ebox.pub.file;

import android.os.Environment;

import com.ebox.Anetwork.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class FileOp {
    private static final String config_filename = Environment.getExternalStorageDirectory().getPath() +
            "/config.properties";

    public static Config getConfig() {
        Config config = new Config();
        try {
            File f = new File(config_filename);

            if (!f.exists()) {
                f.createNewFile();
            }

            Properties prop = PropertiesFile.loadConfig(config_filename);

            config.setTerminal_code(prop.getProperty("terminal_code"));
            String box_cnt = prop.getProperty("box_cnt");
            if (box_cnt != null) {
                config.setCount(Integer.valueOf(box_cnt));
            }

            String dot_id = prop.getProperty("dot");
            if (dot_id != null) {
                config.setDot(Integer.valueOf(dot_id));
            }
            config.setLed_ip(prop.getProperty("led_ip"));
            String port = prop.getProperty("led_port");
            if (port != null) {
                config.setLed_port(Integer.valueOf(port));
            }
            config.setLed_open_time(prop.getProperty("led_open_time"));
            config.setLed_close_time(prop.getProperty("led_close_time"));
            String serviceCtrl = prop.getProperty("serviceCtrl");
            if (serviceCtrl != null) {
                config.setServiceCtrl(Integer.valueOf(serviceCtrl));
            }
            String uart485 = prop.getProperty("uart485");
            if (uart485 !=null)
            {
                config.setUart485(prop.getProperty("uart485"));
            }

            String time1 = prop.getProperty("time1");
            if (time1 != null) {
                config.setTime1(Integer.valueOf(time1));
            }
            String time2 = prop.getProperty("time2");
            if (time2 != null) {
                config.setTime2(Integer.valueOf(time2));
            }
            String backlight1 = prop.getProperty("backlight1");
            if (backlight1 != null) {
                config.setBacklight1(Integer.valueOf(backlight1));
            }
            String backlight2 = prop.getProperty("backlight2");
            if (backlight2 != null) {
                config.setBacklight2(Integer.valueOf(backlight2));
            }

            String CameraCtrl = prop.getProperty("CameraCtrl");
            if (CameraCtrl != null) {
                config.setCameraCtrl(Integer.valueOf(CameraCtrl));
            }

            String water = prop.getProperty("water");
            if(water != null)
            {
                config.setWater(Integer.valueOf(water));
            }

            String platform = prop.getProperty("platform");
            if (platform != null) {
                config.setIsPlatform(Integer.valueOf(platform));
            }

            String scanTimer = prop.getProperty("scanTimer");
            if (scanTimer != null) {
                config.setScanTimer(Integer.valueOf(scanTimer));
            }

            String theme = prop.getProperty("theme");
            if (theme != null) {
                config.setTheme(Integer.valueOf(theme));
            }

            String main_board = prop.getProperty("main_board");
            if (main_board != null) {
                config.setMaim_board(Integer.valueOf(main_board));
            }

            String njums = prop.getProperty("njums");
            if (njums != null) {
                config.setNjUms(Integer.valueOf(njums));
            }

            String screen = prop.getProperty("screen");
            if (screen != null) {
                config.setScreen(Integer.valueOf(screen));
            }
            String web_nj = prop.getProperty("web_nj");
            if (web_nj != null) {
                config.setWeb_nj(Integer.valueOf(web_nj));
            }

            String communityId = prop.getProperty("communityId");
            if (communityId != null) {
                config.setCommunityId(communityId);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (config.getCount() == null || config.getCount().equals(0)) {
                config.setCount(4);
            }
            if (config.getLed_ip() == null || config.getLed_ip().equals("")) {
                config.setLed_ip("192.168.1.99");
            }
            if (config.getLed_port() == null || config.getLed_port().equals(0)) {
                config.setLed_port(10000);
            }
            if (config.getLed_open_time() == null) {
                config.setLed_open_time("0800");
            }
            if (config.getLed_close_time() == null) {
                config.setLed_close_time("2200");
            }
            if (config.getServiceCtrl() == null) {
                config.setServiceCtrl(0);
            }

            /*if (config.getUart485() == null || config.getUart485().equals("")) {
                config.setUart485("/dev/ttyO1");
            }*/
            if (config.getTime1() == null || config.getTime1().equals(0)) {
                config.setTime1(7);
            }
            if (config.getTime2() == null || config.getTime2().equals(0)) {
                config.setTime2(18);
            }
            if (config.getBacklight1() == null || config.getBacklight1().equals(0)) {
                config.setBacklight1(250);
            }
            if (config.getBacklight2() == null || config.getBacklight2().equals(0)) {
                config.setBacklight2(120);
            }


            if (config.getCameraCtrl() == null ) {
                config.setCameraCtrl(1);
            }

            /*if (config.getIsPlatform() == null ) {
                config.setIsPlatform(0);
            }*/

            if (config.getScanTimer() == null ) {
                config.setScanTimer(30);
            }

            if(config.getWater() == null )
            {
                config.setWater(0);
            }

            if (config.getTheme() == null ) {
                config.setTheme(0);
            }

            if (config.getDot() == null ) {
                config.setDot(0);
            }
            if (config.getMaim_board() == null ) {

                if(config.getUart485()!=null && config.getUart485().equals("/dev/ttyO3"))
                {
                    config.setMaim_board(1);
                }
                else if (config.getUart485()!=null && config.getUart485().equals("/dev/ttyS3"))
                {
                    config.setMaim_board(2);
                }
                else
                {
                    config.setMaim_board(0);
                }
            }

            if (config.getNjUms() == null) {

                if(config.getWater()!=null && config.getWater()==1)
                {
                    config.setNjUms(1);
                }
                else
                {
                    config.setNjUms(0);
                }
            }
            if (config.getCommunityId() == null)
            {
                config.setCommunityId("36");
            }
            if (config.getScreen()==null)
            {
                config.setScreen(2);
            }
            if (config.getWeb_nj()==null)
            {
                config.setWeb_nj(0);
            }
        }
        return config;
    }

    public static void saveTemp(Config config) {
        if (config.getTerminal_code() != null) {
            PropertiesFile.saveConfig(config_filename, "terminal_code", config.getTerminal_code());
        }
        if (config.getCount() != null) {
            PropertiesFile.saveConfig(config_filename, "box_cnt", config.getCount() + "");
        }
        if (config.getLed_ip() != null) {
            PropertiesFile.saveConfig(config_filename, "led_ip", config.getLed_ip());
        }
        if (config.getLed_port() != null) {
            PropertiesFile.saveConfig(config_filename, "led_port", config.getLed_port() + "");
        }
        if (config.getLed_open_time() != null) {
            PropertiesFile.saveConfig(config_filename, "led_open_time", config.getLed_open_time());
        }
        if (config.getLed_close_time() != null) {
            PropertiesFile.saveConfig(config_filename, "led_close_time", config.getLed_close_time());
        }
        if (config.getServiceCtrl() != null) {
            PropertiesFile.saveConfig(config_filename, "serviceCtrl", config.getServiceCtrl() + "");
        }
        if (config.getUart485() != null) {
            PropertiesFile.saveConfig(config_filename, "uart485", config.getUart485());
        }
        if (config.getTime1() != null) {
            PropertiesFile.saveConfig(config_filename, "time1", config.getTime1() + "");
        }
        if (config.getTime2() != null) {
            PropertiesFile.saveConfig(config_filename, "time2", config.getTime2() + "");
        }
        if (config.getBacklight1() != null) {
            PropertiesFile.saveConfig(config_filename, "backlight1", config.getBacklight1() + "");
        }

        if (config.getBacklight2() != null) {
            PropertiesFile.saveConfig(config_filename, "backlight2", config.getBacklight2() + "");
        }

        if (config.getCameraCtrl() != null) {
            PropertiesFile.saveConfig(config_filename, "CameraCtrl", config.getCameraCtrl() + "");
        }

        if(config.getWater()!= null)
        {
            PropertiesFile.saveConfig(config_filename, "water", config.getWater()+"");
        }

        if (config.getIsPlatform() != null) {
            PropertiesFile.saveConfig(config_filename, "platform", config.getIsPlatform() + "");
        }
        if (config.getScanTimer() != null) {
            PropertiesFile.saveConfig(config_filename, "scanTimer", config.getScanTimer() + "");
        }

        if (config.getTheme() != null) {
            PropertiesFile.saveConfig(config_filename, "theme", config.getTheme() + "");
        }

        if (config.getDot() != null) {
            PropertiesFile.saveConfig(config_filename, "dot", config.getDot() + "");
        }

        if (config.getMaim_board() != null) {
            PropertiesFile.saveConfig(config_filename, "main_board", config.getMaim_board() + "");
        }

        if (config.getNjUms() != null) {
            PropertiesFile.saveConfig(config_filename, "njums", config.getNjUms() + "");
        }

        if (config.getCommunityId() != null) {
            PropertiesFile.saveConfig(config_filename, "communityId", config.getCommunityId());
        }
        if (config.getScreen() != null) {
            PropertiesFile.saveConfig(config_filename, "screen", config.getScreen()+"");
        }
        if (config.getWeb_nj() != null) {
            PropertiesFile.saveConfig(config_filename, "web_nj", config.getWeb_nj()+"");
        }
    }
}
