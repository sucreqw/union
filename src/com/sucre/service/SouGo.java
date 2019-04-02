package com.sucre.service;

import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

import java.net.URLEncoder;

public class SouGo {
    String vid = "";

    public void getpic(String vid) {

        this.vid = vid;
        Nets net = new Nets();
        String ret = "";
        String cookie = "";
        String type = "1";
        String token = "";
        String userid = "";
        String sec = "";
        String Ips = "";
        String Ipsec = "";
        while (true) {
            try {
               // MyUtil.print("取token", Factor.getGui());
                ret = net.goPost("wap.sogou.com", 443, getpicData(vid));
                if (!MyUtil.isEmpty(ret)) {

                    cookie = MyUtil.getAllCookie(ret);
                    String temp = MyUtil.midWord("var sogoupass = [\"", "\"];", ret);
                    if (!MyUtil.isEmpty(temp)) {
                        String[] Tem = temp.split("\",\"");
                        if (Tem != null && Tem.length >= 6) {
                            token = MyUtil.midWord("uuid': '", "',", ret);
                            userid = Tem[1];
                            sec = Tem[2];
                            Ips = Tem[5];
                            Ipsec = Tem[6];
                        }
                    }
// winhttp1.SendForFile "https://account.sogou.com/captcha?token=" & Token & "&t=1519903745754", "", Cookie, ""
                   // MyUtil.print("取图片", Factor.getGui());
                    ret = net.goPost("account.sogou.com", 443, getpicByte(token));
                    while (true) {
                       // MyUtil.print("投", Factor.getGui());
                        ret = net.goPost("wap.sogou.com", 443, submit(cookie, "12", token, type, userid, sec, Ips, Ipsec));
                        if (!MyUtil.isEmpty(ret)) {
                            //MidWord("popular_remian"":", ",""", gie, 0)
                            temp = MyUtil.midWord("popular_remian\":", ",\"", ret);
                            if (MyUtil.isEmpty(temp) || "0".equals(temp)) {
                                //code":"
                                MyUtil.print(MyUtil.midWord("code\":\"", "\"}", ret), Factor.getGui());

                                break;
                            } else {
                                //System.out.println(MyUtil.midWord("share\":", ",", ret));
                                MyUtil.print("当前分享数量：" + MyUtil.midWord("share\":", ",", ret) + "." + "当前人气数量：" + MyUtil.midWord("popular\":", ",", ret), Factor.getGui());
                                // MyUtil.print(, Factor.getGui());
                                type = type.equals("1") ? "2" : "1";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //getpid
    private byte[] getpicData(String vid) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET https://wap.sogou.com/web/searchList.jsp?vr_abtest=1&keyword=" + URLEncoder.encode(vid) + "&prs=8&rfh=1 HTTP/1.1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Pragma: no-cache\r\n");
        data.append("Host: wap.sogou.com\r\n");
        data.append("Accept-Language: zh-CN\r\n");
        data.append("Referer:  https://wap.sogou.com/web/searchList.jsp\r\n");
        data.append("Connection: close\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //getpic's byte
    private byte[] getpicByte(String token) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        //https://account.sogou.com/captcha?token=" & Token & "&t=1519903745754
        data.append("GET https://account.sogou.com/captcha?token=" + token + "&t=1519903745754 HTTP/1.1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Pragma: no-cache\r\n");
        data.append("Host: account.sogou.com\r\n");
        data.append("Accept-Language: zh-CN\r\n");
        data.append("Referer:  https://wap.sogou.com/web/searchList.jsp\r\n");
        data.append("Connection: close\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //submit
    private byte[] submit(String cookies, String phone, String token, String type,
                          String userid, String sec, String ips, String ipsec) {
        StringBuilder data = new StringBuilder(900);
        String temp = "";
        data.append("GET /reventondc/inner/interaction/interaction?callback=djsonp1519902775502&reqtype=mxbd_action&id=1272907&phone=" + phone + "&cont=" + token + "&type=" + type + "&userid=" + userid + "&sec=" + sec + "&login=0&antiid=" + ips + "&ipsec=" + ipsec + "& HTTP/1.1\r\n");
        data.append("Host: wap.sogou.com\r\n");
        data.append("Connection: close\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://wap.sogou.com/web/searchList.jsp?vr_abtest=1&keyword=%E5%90%B4%E7%A3%8A\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: " + cookies + "\r\n");
        data.append("\r\n");
        data.append("\r\n");
        ;
        return data.toString().getBytes();
    }

}
