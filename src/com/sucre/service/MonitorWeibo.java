package com.sucre.service;

import com.sucre.factor.Factor;
import com.sucre.myNet.OkHttp;
import com.sucre.utils.MyUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class MonitorWeibo {

    public void start(String uid, String cookie) {
        cookie = getcookieIndex(cookie, 2);
        OkHttp okHttp = new OkHttp();
        HashMap<String, String> header = new HashMap<>();
        HashMap<String, String> body = new HashMap<>();
        header = new HashMap<>();
        /*//header.put("X-Log-Uid" , uid );
        header.put("Cache-Control", "max-age=0");
        header.put("x-log-uid", uid);
        header.put("User-Agent", "Xiaomi-MI 4LTE_weibo_7.2.0_android_android6.0.1");
        //body=new HashMap<>();*/

        String url = "https://m.weibo.cn/api/container/getIndex?type=uid&value=" + uid + "&containerid=107603" + uid;
        String ret = okHttp.goGet(url, header);
        if (!MyUtil.isEmpty(ret)) {
            String fristW = MyUtil.midWord("\"card_type\":9", "\"card_type\":9", ret);
            //先找第一条微博ID
            String mid = MyUtil.midWord("\"mid\":\"", "\",\"", fristW);
            MyUtil.print(mid, Factor.getGui());
            if (!checkMid(mid)) {
                String text = MyUtil.midWord("\"text\":\"", "\",\"", fristW);
                text = MyUtil.decodeUnicode(cleanData(text));
                String pics = MyUtil.midWord("\"pics\":", "},\"", fristW);
                String retweeted = MyUtil.midWord("\"retweeted_status\"", "]},\"", fristW);
                if (null != retweeted) {
                    //转发。
                } else {
                    //直发。
                    ArrayList<String> piclist = null;
                    String pids = "";
                    String pidsN = "0";
                    if (null != pics) {
                        piclist = MyUtil.midWordAll("pid\":\"", "\",\"", pics);
                    }
                    if (!MyUtil.isEmpty(piclist)) {
                        pids = piclist.get(0);
                        for (int i = 1; i < piclist.size(); i++) {
                            pids += "|" + piclist.get(i);
                        }
                        pidsN = String.valueOf(piclist.size());
                    } else {
                        pids = "";
                        pidsN = "0";
                    }
                    PublishWeibo publishWeibo = new PublishWeibo();
                    publishWeibo.start(cookie, text, pids, pidsN);
                }
            }


        }
    }

    //取指定的cookie
    public String getcookieIndex(String cookies, int index) {
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[index];
        } else {
            cookie = cookies;
        }
        return cookie;
    }

    //清洗得到的数据
    private String cleanData(String text) {
        String result = text;
        //先清A标签
        ArrayList<String> ret = new ArrayList<>();
        ret = MyUtil.midWordAll("<a", "<\\/a>", result);
        if (!MyUtil.isEmpty(ret)) {
            for (int i = 0;
                 i < ret.size(); i++) {
                String href = MyUtil.midWord("href=\\\"", "\\\" ", ret.get(i));
                String A = MyUtil.midWord(">", "<\\/a>", "<a" + ret.get(i) + "<\\/a>");
                if (href != null && href.indexOf("object_type=video") != -1) {
                    //href=href.replace("\\","");
                    String urls=MyUtil.midWord("data-url=\\\"","\\\" ",ret.get(i));
                    urls=urls.replace("\\","");
                    A += urls ;
                }
                result = result.replace("<a" + ret.get(i) + "<\\/a>", A);
            }
        }
        //清理SPAN
        ret = MyUtil.midWordAll("<span", "<\\/span>", result);
        if (!MyUtil.isEmpty(ret)) {
            for (int i = 0;
                 i < ret.size(); i++) {
                String span = MyUtil.midWord(">", "<\\/span>", "<span" + ret.get(i) + "<\\/span>");
                result = result.replace("<span" + ret.get(i) + "<\\/span>", span);
            }
        }
        //清理IMAGE
        ret = MyUtil.midWordAll("<img", ">", result);
        if (!MyUtil.isEmpty(ret)) {
            for (int i = 0;
                 i < ret.size(); i++) {
                String span = MyUtil.midWord("alt=", " ", "<img" + ret.get(i) + ">");
                if (MyUtil.isEmpty(span)) {
                    result = result.replace("<img" + ret.get(i) + ">", "");
                } else {
                    result = result.replace("<img" + ret.get(i) + ">", span);
                }

            }
        }
        return result;
    }

    /**
     * 检查目前最新的MID是否已经记录上了。如果在记录，返回true，不在记录的话就返回false
     * 并记录起来。
     *
     * @param mid
     * @return
     */
    private boolean checkMid(String mid) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File("mid.properties")));
            String oldmid = properties.getProperty("mid");
            if (oldmid.equals(mid)) {
                return true;
            } else {
                FileOutputStream out = new FileOutputStream("mid.properties");
                properties.setProperty("mid", mid);
                properties.store(out, null);
                out.close();
                return false;
            }
        } catch (FileNotFoundException e) {
            MyUtil.print("导入mid文件错误", Factor.getGui());
            //System.out.println("导入ip文件错误：" + e.getMessage());
            //throw new Exception(e.getMessage());
            //return true;
        } catch (IOException e) {
            MyUtil.print("导入mid文件错误", Factor.getGui());
            //throw new Exception(e.getMessage());
            //return true;
        }
        return true;
    }
}
