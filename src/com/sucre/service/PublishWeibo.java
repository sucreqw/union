package com.sucre.service;

import com.sucre.factor.Factor;
import com.sucre.myNet.OkHttp;
import com.sucre.utils.MyUtil;

import java.util.HashMap;

public class PublishWeibo {

    public void start(String cookie,String text,String pids,String pidsN){
        OkHttp okHttp = new OkHttp();
        HashMap<String, String> header = new HashMap<>();
        HashMap<String, String> body = new HashMap<>();
        header = new HashMap<>();
        header.put("cookie", cookie);
        header.put("Referer", "https://weibo.com/");
        header.put("Origin", "https://weibo.com/");
        header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        header.put("Sec-Fetch-Dest", "empty");
        header.put("Sec-Fetch-Mode", "cors");
        header.put("Sec-Fetch-Site", "same-origin");
        header.put("X-Requested-With", "XMLHttpRequest");
        header.put("Connection", "keep-alive");
                    /*String st="";
                    url = "https://m.weibo.cn/api/config";
                    ret = okHttp.goGet(url, header);
                    if(!MyUtil.isEmpty(ret)){
                        st=MyUtil.midWord("st\":\"","\",\"",ret);
                    }*/

        body=new HashMap<>();
        body.put("location","v6_content_home");
        body.put("text",text);
        body.put("appkey","");
        body.put("style_type","1");
        body.put("pic_id",pids);
        body.put("tid","");
        body.put("pdetail","");
        body.put("mid","");
        body.put("isReEdit","false");
        body.put("gif_ids","");
        body.put("rank","0");
        body.put("rankid","");
        body.put("module","stissue");
        body.put("pub_source","main_");
        body.put("updata_img_num",pidsN);
        body.put("pub_type","dialog");
        body.put("isPri","null");
        body.put("_t","0");
                    /*body.put("content",text);
                    body.put("picId",pids);
                    body.put("st",st);
                    body.put("_spr","screen:1366x768");*/


        String url = "https://weibo.com/aj/mblog/add?ajwvr=6&__rnd="+ MyUtil.getTimeB();
        String ret = okHttp.goPost(url, header,body);
        if(!MyUtil.isEmpty(ret)){
            if(ret.indexOf("code\":\"100000\"")!=-1){
                MyUtil.print("发送微博成功！", Factor.getGui());
            }else{
                MyUtil.print("发送微博失败！", Factor.getGui());
                System.out.println(ret);
            }
        }
    }
}
