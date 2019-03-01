package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

import java.net.URLEncoder;

public class WeiboVote extends Weibo {
    private String picId = "";
    private String vid = "";

    @Override
    public int Actions(int index, String mission) {
        Nets nets = new Nets();
        String ret = "";
        String cookie = "";

        // 此处需要循环取vid 投票
        Vid v = new Vid();
        for (int i = 0; i < Controller.getInstance().getVidImpl().getSize(); i++) {
            v = Controller.getInstance().getVidImpl().getVid(i, v);
            vid = v.getVids();
            // 取到vid之后，判断任务
            switch (mission) {
                case "加油":

                    ret = nets.goPost("energy.tv.weibo.cn", 443, incrspt(super.getCookie(), vid));
                    String p = MyUtil.midWord("msg\":\"", "\",\"", ret);
                    MyUtil.outPutData("log.txt", p + (index + 1));
                    MyUtil.print(p + (index + 1), Factor.getGui());
                    break;

                case "搜索s.com":
                    ret = nets.goPost("s.weibo.com", 443, Sserach(super.getCookie(), vid));
                    // String p=MyUtil.midWord("msg\":\"", "\",\"", ret);
                    // MyUtil.outPutData("log.txt", p);
                    MyUtil.print(ret, Factor.getGui());
                    break;

                case "打榜":
                    try {
                        cookie = super.getCookie().split("^")[0];
                        ret = nets.goPost("huati.weibo.cn", 443, getscore(cookie, vid));
                        if (!MyUtil.isEmpty(ret)) {
                            String allScore = MyUtil.midWord("score\":", ",\"", ret);
                            String rank = MyUtil.midWord("rank\":", ",\"", ret);
                            String userScore = MyUtil.midWord("user_total_score\":", ",\"", ret);
                            String name = MyUtil.midWord("topic_name\":\"", "\",\"", ret);
                            MyUtil.print("开始打榜！" + (index + 1), Factor.getGui());
                            while (true) {
                                ret = nets.goPost("huati.weibo.cn", 443,
                                        picktop(cookie, vid, userScore, name, rank, allScore));
                                if (!MyUtil.isEmpty(ret)) {
                                    if (ret.indexOf("100000") != -1) {
                                        MyUtil.print("打榜成功！" + super.getId() + "|" + super.getPass() + "|" + name + "|"
                                                + userScore + "|", Factor.getGui());

                                        return 1;
                                    } else if (ret.indexOf("382023") != -1) {
                                        // 要拖码
                                        MyUtil.print("要拖码" + super.getId() + "|" + super.getPass() + "|" + userScore,
                                                Factor.getGui());
                                        return 382023;

                                    } else {
                                        // 失败，原因不明。
                                        MyUtil.print("打榜失败，原因不明！" + super.getId() + "|" + super.getPass(), Factor.getGui());
                                        return 0;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        MyUtil.print("打榜出错!" + e.getMessage(), Factor.getGui());
                        System.out.println(ret);
                    }
                    // MyUtil.print(ret, Factor.getGui());
                    break;
                case "查分":
                    cookie = super.getCookie().split("^")[0];
                    ret = nets.goPost("huati.weibo.cn", 443, getscore(cookie, vid));
                    if (!MyUtil.isEmpty(ret)) {
                        String userScore = MyUtil.midWord("user_total_score\":", ",\"", ret);
                        MyUtil.print("当前分数：" + userScore + "<=>" + super.getId() + "|" + super.getPass(), Factor.getGui());
                        i = Controller.getInstance().getVidImpl().getSize();
                    }
                    break;

                case "阅读":
                    String s = super.getS();
                    String uid = super.getUid();
                    cookie = super.getCookie();

                    ret = nets.goPost("api.weibo.cn", 443, cardlist(uid, cookie, s, vid));
                    if (!MyUtil.isEmpty(ret)) {
                        if (ret.indexOf("cardlistInfo") != -1) {
                            MyUtil.print("成功！" + index, Factor.getGui());

                        } else {
                            if (Thread.currentThread().getName().equals("ip")) {
                                MyUtil.changIp();
                            }
                        }
                    }

                    break;

                case "游客":
                    SinaVisitor visitor = new SinaVisitor();
                    visitor.doWork(index);
                    break;

                case "播放":
                    PlayStatistics play = new PlayStatistics();
                    int rets = play.play(super.getUid(), vid, super.getCookie());
                    if (rets == 1) {
                        MyUtil.print("成功！！" + (index + 1), Factor.getGui());
                    } else {
                        MyUtil.print("失败！！" + (index + 1) + "<>" + super.getId() + "|" + super.getPass(), Factor.getGui());
                    }
                    break;

                case "沸点":
                    ret = nets.goPost("mbd.baidu.com", 443, starhit(vid));
                    if (!MyUtil.isEmpty(ret)) {
                        if (ret.indexOf("voteSucc\":1") != -1) {
                            MyUtil.succcounts++;
                        }

                        MyUtil.counts++;
                        MyUtil.print("当前票数：" + MyUtil.midWord("vote\":", ",\"", ret) + "<=>软件投出票数：" + MyUtil.counts
                                + "<=>返回成功次数：" + MyUtil.succcounts, Factor.getGui());
                        // }
                        if (Controller.getInstance().getEndCount() != 0
                                && Controller.getInstance().getEndCount() < MyUtil.succcounts) {
                            MyUtil.print("投票完成！", Factor.getGui());
                            return 3;
                        }
                        if (ret.indexOf("errmsg\":\"") != -1) {
                            MyUtil.print(MyUtil.midWord("errmsg\":\"", "\"", ret), Factor.getGui());

                        }
                        System.out.println(MyUtil.midWord("data", "}", ret));
                    }
                    break;
                case "通用":
                    ret = nets.goPost("api.weibo.cn", 443, buttonVote(super.getCookie(), super.getS(), super.getUid(), vid));
                    if (!MyUtil.isEmpty(ret)) {
                        MyUtil.counts++;
                        if (ret.indexOf("result\":1") != -1) {
                            MyUtil.succcounts++;
                            MyUtil.print("成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                    Factor.getGui());
                            //MyUtil.print(MyUtil.midWord("\"name\":\"有机值", ",", ret),Factor.getGui());
                        } else {
                            MyUtil.print("失败！" + MyUtil.midWord("msg\":\"", "\"", ret), Factor.getGui());
                        }
                    }
                    break;

                case "微博之夜":
                    for (int j = 0; j < 20; j++) {
                        ret = nets.goPost("huodong.weibo.cn", 443, netchina2018(super.getUid(), vid, super.getCookie()));
                        MyUtil.counts++;
                        if (!MyUtil.isEmpty(ret)) {
                            if (ret.indexOf("u6295\\u7968\\u6210\\u529f") != -1) {
                                MyUtil.succcounts++;
                                MyUtil.print("投票成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                        Factor.getGui());

                            } else {
                                MyUtil.print("投票失败！" + MyUtil.decodeUnicode(MyUtil.midWord("msg\":\"", "\"", ret)),
                                        Factor.getGui());
                            }
                            if (ret.indexOf("\u4eca\u5929\u7968\u6570\u5df2\u7528\u5b8c") != -1) {
                                break;
                            }
                        }
                    }
                    break;

                case "秒拍领分":
                    for (int j = 1; j < 3; j++) {
                        ret = nets.goPost("n.miaopai.com", 443, task(super.getCookie(), String.valueOf(j)));
                        if (!MyUtil.isEmpty(ret)) {
                            MyUtil.print(MyUtil.midWord("data\":", "}", ret) + "<>" + (index + 1), Factor.getGui());
                        }
                    }
                    break;

                case "秒拍查分":
                    ret = nets.goPost("n.miaopai.com", 443, fansvotes(super.getCookie()));
                    if (!MyUtil.isEmpty(ret)) {
                        MyUtil.print(MyUtil.midWord("data\":", "}", ret) + "<>" + (index + 1), Factor.getGui());
                    }
                    break;

                case "秒拍送分":
                    vid = URLEncoder.encode(vid);
                    for (int j = 0; j < 5; j++) {
                        ret = nets.goPost("n.miaopai.com", 443, miaovote(super.getCookie(), vid, MyUtil.makeNumber(10)));
                        if (!MyUtil.isEmpty(ret)) {
                            MyUtil.counts++;

                            if (ret.indexOf("consume\":\"1\"}") != -1) {
                                MyUtil.succcounts = MyUtil.succcounts + 10;
                                MyUtil.print("投票成功！软件运行次数：" + MyUtil.counts + "<>返回成功次数：" + MyUtil.succcounts + "<>"
                                        + (index + 1), Factor.getGui());
                            } else {
                                MyUtil.print("投票失败，跳过！" + "<>" + (index + 1), Factor.getGui());
                                break;
                            }
                        }
                    }
                    break;


                case "回放":

                    /*
                     * PlayStatistics plays = new PlayStatistics(); int retss =
                     * plays.play2(super.getUid(), vid, super.getCookie()); if
                     * (retss == 1) { MyUtil.print("成功！！" + (index + 1),
                     * Factor.getGui()); } else { MyUtil.print("失败！！" + (index + 1)
                     * + "<>" + super.getId() + "|" + super.getPass(),
                     * Factor.getGui()); }
                     */

                    ret = nets.goPost("", 443, flashplayinfo(super.getCookie(), vid, super.getUid()));
                    MyUtil.counts++;
                    MyUtil.print(String.valueOf(MyUtil.counts), Factor.getGui());
                    break;
                case "读书":
                    ret = nets.goPost("m.weibo.cn", 443, readbookVid(super.getCookie(), super.getUid()));
                    if (!MyUtil.isEmpty(ret)) {

                        String temp = MyUtil.midWord("option_id=" + vid, "luicode", ret);
                        String sig = "";

                        sig = temp != "" && temp != null ? MyUtil.midWord("sig=", "&", temp) : "";


                        ret = nets.goPost("movie.weibo.com", 443, readbook(super.getCookie(), vid, sig));
                        if (!MyUtil.isEmpty(ret)) {
                            MyUtil.counts++;
                            if (ret.indexOf("投票成功") != -1) {
                                MyUtil.succcounts++;
                                MyUtil.print("成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                        Factor.getGui());

                            } else {
                                MyUtil.print("失败！<==>" + (index + 1), Factor.getGui());
                            }
                        }

                    }
                    break;

                case "跑步" :
                    ret = nets.goPost("utils.sports.sina.cn", 443, paotuan(super.getCookie(),super.getUid(),vid));
                    if (!MyUtil.isEmpty(ret)) {
                        if (ret.indexOf("msg\":\"Succ") != -1) {
                            MyUtil.succcounts++;
                            MyUtil.print("成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                    Factor.getGui());

                        } else {
                            MyUtil.print("失败！<==>" + (index + 1), Factor.getGui());
                        }
                    }
                    break;

                case "春节":
                    ret = nets.goPost("movie.weibo.com", 443, votepopvid(super.getCookie(),vid));
                    if (!MyUtil.isEmpty(ret)) {

                        //String temp = MyUtil.midWord("option_id=" + vid, "&", ret);
                        String sig = "";

                        sig =MyUtil.midWord("sig=", "&", ret) ;


                        ret = nets.goPost("movie.weibo.com", 443, votepop(super.getCookie(),sig,vid));
                        if (!MyUtil.isEmpty(ret)) {
                            MyUtil.counts++;
                            if (ret.indexOf("success") != -1) {
                                MyUtil.succcounts++;
                                MyUtil.print("成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                        Factor.getGui());

                            } else {
                                MyUtil.print("失败！<==>" + (index + 1), Factor.getGui());
                            }
                        }

                    }
                    break;

                case "LPL战队":
                    ret=nets.goPost("sports.weibo.com",443,lpl(super.getCookie(),vid));
                    if (!MyUtil.isEmpty(ret)) {
                        MyUtil.counts++;
                        if (ret.indexOf("送奖杯成功") != -1) {
                            MyUtil.succcounts++;
                            MyUtil.print("成功！" + "<=>软件投出票数：" + MyUtil.counts + "<=>返回成功次数：" + MyUtil.succcounts,
                                    Factor.getGui());

                        } else {
                            MyUtil.print("失败！<==>" + (index + 1), Factor.getGui());
                        }
                    }
                    break;

                case "秒拍关注":
                    //因为秒拍cookie只有两项，以前代码设计直接当成id导入 ，为了兼容，这里直接取id就是cookie.
                    ret=nets.GoHttp("b-api.ins.miaopai.com",80,miaopaiFollow(super.getId(),vid));
                    if(!MyUtil.isEmpty(ret)){
                        String msg=MyUtil.midWord("msg\":\"","\",\"",ret);
                        if(msg!=null && !"".equals(msg)){
                            MyUtil.print("关注失败："+ MyUtil.decodeUnicode(msg) +"<>" +(index-1) ,Factor.getGui());
                        }else{
                            MyUtil.print("关注成功："+ (index-1) ,Factor.getGui());
                        }
                    }
                    break;
                case "秒拍取消关注":
                    //因为秒拍cookie只有两项，以前代码设计直接当成id导入 ，为了兼容，这里直接取id就是cookie.
                    ret=nets.GoHttp("b-api.ins.miaopai.com",80,miaopaiUnFollow(super.getId(),vid));
                    if(!MyUtil.isEmpty(ret)){
                        String msg=MyUtil.midWord("msg\":\"","\",\"",ret);
                        if(msg!=null && !"".equals(msg)){
                            MyUtil.print("取消关注失败："+ MyUtil.decodeUnicode(msg) +"<>" +(index-1) ,Factor.getGui());
                        }else{
                            MyUtil.print("取消关注成功："+ (index-1) ,Factor.getGui());
                        }
                    }
                    break;
            }// end of switch

        }

        return 0;
    }
    //取指定的cookie
    public String getcookieIndex(String cookies,int index){
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[index];
        }else{
            cookie=cookies;
        }
        return cookie;
    }
    // 设置识别完成后的pid
    public void setPid(String pid) {
        this.picId = pid;
    }

    // 取投票的vid
    public String getVid() {
        return this.vid;
    }

    // 加油卡，点亮
    private byte[] incrspt(String cookie, String vid) {
        String[] tempCookie = cookie.split("\\^");
        cookie = tempCookie.length > 2 ? tempCookie[2] : "";
        StringBuilder data = new StringBuilder(900);
        String temp = "eid=10270&suid=" + vid
                + "&spt=1&send_wb=&send_text=&follow_uid=&page_type=tvenergy_index_star\r\n";
        data.append("POST http://energy.tv.weibo.cn/aj/incrspt HTTP/1.1\r\n");
        data.append("Host: energy.tv.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: " + temp.length() + "\r\n");
        data.append("Accept: */*\r\n");
        data.append("Origin: http://energy.tv.weibo.cn\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n");
        data.append("Referer: http://energy.tv.weibo.cn/e/10270/index\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // s.weibo.com 搜索
    private byte[] Sserach(String cookie, String vid) {
        StringBuilder data = new StringBuilder(900);
        data.append("GET https://s.weibo.com/weibo?q=" + vid + "&Refer=index HTTP/1.1\r\n");
        data.append("Host: s.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append(
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://s.weibo.com/\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 打榜送分
    private byte[] picktop(String cookie, String vid, String score, String name, String rank, String topic_score) {
        StringBuilder data = new StringBuilder(900);
        name = URLEncoder.encode(name);
        String temp = "topic_name=" + name + "&page_id=" + vid + "&score=" + score + "&is_pub=0&cur_rank=" + rank
                + "&ctg_id=2&topic_score=" + topic_score + "&index=select256&user_score=" + score + "\r\n";
        data.append("POST /aj/super/picktop HTTP/1.1\r\n");
        data.append("Host: huati.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: " + temp.length() + "\r\n");
        data.append("Accept: application/json, text/plain, */*\r\n");
        data.append("Origin: https://huati.weibo.cn\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2)\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append(
                "Referer: https://huati.weibo.cn/super/pick?ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&from=1086395010&page_id=1008086de98a1a1a398df9761c706bfaac6b00\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    // 打榜取分
    private byte[] getscore(String cookie, String vid) {
        StringBuilder data = new StringBuilder(900);

        data.append("GET /aj/super/getscore?page_id=" + vid + " HTTP/1.1\r\n");
        data.append("Host: huati.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Accept: application/json, text/plain, */*\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2)\r\n");
        data.append(
                "Referer: https://huati.weibo.cn/super/pick?ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&from=1086395010&page_id=1008086de98a1a1a398df9761c706bfaac6b00\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 阅读
    private byte[] cardlist(String uid, String cookie, String s, String vid) {
        StringBuilder data = new StringBuilder(900);

        data.append(
                "GET https://api.weibo.cn/2/guest/cardlist?networktype=wifi&uicode=10000198&moduleID=708&checktoken=null&wb_version=3332&lcardid=4083046808752770&c=android&i=null&s="
                        + s
                        + "&ua=Xiaomi-MI%204LTE_weibo_7.2.0_android_android6.0.1&wm=4209_8001&aid=01Anull.&did=null&fid=107603"
                        + vid + "_-_WEIBO_SECOND_PROFILE_WEIBO&uid=" + uid + "&v_f=2&v_p=44&from=1072095010&gsid="
                        + cookie
                        + "&imsi=null&lang=zh_CN&lfid=230584&page=1&skin=default&count=20&oldwn=4209_8001&sflag=1&containerid=107603"
                        + vid + "_-_WEIBO_SECOND-PROFILE_WEIBO&luicode=10000228&need_head_cards=0 HTTP/1.1\r\n");
        data.append("Host: api.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Cache-Control: max-age=0\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append(
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");

        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 百度沸点
    private byte[] starhit(String vid) {
        StringBuilder data = new StringBuilder(900);
        String temp = MyUtil.makeNonce(12);
        // String temp = "";
        // data.append("GET
        // https://mbd.baidu.com/webpage?type=starhit&action=vote&format=json&starid="+
        // vid +"&votetype=1 HTTP/1.1\r\n");
        // GET
        // /webpage?type=starhit&action=vote&format=json&starid=3772&votetype=1
        // HTTP/1.1
        // String[] vids=vid.split("\\|");
        String t = String.valueOf(System.currentTimeMillis());
        String buids = temp + MyUtil.makeNonce(20) + ":FG=1";
        String token = MyUtil.MD5(buids + "a$4#1G9g6^" + t);
        // laHsagO_2ilr82uAgi-Yujuh28_zuvu_YuHmag8fv8lqa2iC_iH1ilfvWaJa9QPJ897mA
        // bcPwZt5gc9zEr0GUbG-1n0uoAMSguzr1FYb4J0aAJzH7GBCd0pcfrV6Buj5Zw7yH-JBlJB2gw85-mZS5j-g9row
        String buid2 = MyUtil.makeNonce(8) + "-1n0uoAMSguzr1FYb4J0aAJzH7GBCd0pcfrV6Buj5Zw7yH-JBlJB2gw85-mZS5j-g9row";
        data.append("GET /webpage?type=starhit&action=vote&format=json&starid=" + vid
                + "&votetype=1&risk%5Baid%5D=1461&risk%5Bapp%5D=ios&risk%5Bver%5D=11.0.0.12&risk%5Bto%5D=20$"
                + MyUtil.makeNumber(10)
                + "884545611417567399154452887242957845a5e5d7e397af0d39de2576011039ae6619a929c06a908cb96311a2447c976b307cbf4571544528847061&risk%5Bvw%5D=021571413240000000000000000000000000000000000000000000008401ff80037"
                + buids
                + "0000000000000037e60dab5eda3376732b7a9345252a2fc64e6b2&risk%5Bua%5D=Mozilla%2F5.0+(iPhone%3B+CPU+iPhone+OS+12_1+like+Mac+OS+X)+AppleWebKit%2F605.1.15+(KHTML,+like+Gecko)+Mobile%2F16B92+light%252F1.0%2528WKWebView%2529+baiduboxapp%2F11.0.0.12+(Baidu%3B+P2+12.1)&risk%5Bz%5D="
                + buid2 + "&stoken=" + token + "&ts=" + t + " HTTP/1.1\r\n");
        data.append("Host: mbd.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 12_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16B92 light%2F1.0%28WKWebView%29 baiduboxapp/11.0.0.12 (Baidu; P2 12.1)\r\n");
        data.append("Content-Type: \r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://mbd.baidu.com/webpage?type=starhit&action=starhome&starid=" + vid
                + "&from=alading\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.9\r\n");
        // data.append("Cookie: BAIDUCUID=_avet_P1SulP"+
        // MyUtil.makeNonce(12)+"-"+
        // MyUtil.makeNonce(12)+"_iE-NN"+MyUtil.makeNonce(10) +"Pf_uL28gha2tDA;
        // BAIDUID="+ buids +"; BIDUPSID="+ temp+"9759B8EAEA14CFC65FCC; BDUSS="+
        // MyUtil.makeNonce(12)+"Vk9XLXVtNHFWRlhKTXF4MkxmU3NwMX54dld"+
        // MyUtil.makeNonce(6)+"2llRWNYUkpjQVFBQUFBJCQAAAAAAAAAAAEAAA"+
        // MyUtil.makeNonce(26)+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+
        // MyUtil.makeNonce(14)+"S;\r\n");
        // BAIDUCUID=_avet_P1SuNN(8)SNuluP-aYVuNN(8)_iE-88La28ngu2Pf_uL28gha2tDA;
        // BAIDUID=NN(8)180E9759B8EAEA14CFC65FCC:FG=1;
        // BIDUPSID=NN(8)180E9759B8EAEA14CFC65FCC;
        // BDUSS=dNenoyUjNrdlA4dmhGR3lncXJFVHJTamw0Qm93YlptVjZoOH53cHhvaGNyVEZjQVFBQUFBJCQAAAAAAAAAAAEAAANN(6)AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFwgClxcIApcak;
        data.append("Cookie: x-logic-no=5; BDPASSGATE=" + MyUtil.makeNonce(10)
                + "_yiU4VOu3kIN8efBUrKAAevFSlp636SPfCaWmhH33bUrWz0HSieXBDP6wZTXebZda5XKXlVXa_1ajQMtfyJrbCWyzNSotMPdJ222y2QZCb4jKUE0wA4HkPNjwOEd0f6NKTsubRDghQw4ivKl73JXb38076nomMrs1TCG0Grqr83IKHRfQGLYKuuR94rSjEdgXJmfA4H6TS0dmUs4QGUY8K0fdME-A3v5rnW; BAIDULOC=12613216.419904_2619693.4042002_1000_257_1544528602986; BDORZ=AE84CDB3A529C0F8A2B9DCDD1D18B695; BAIDUCUID="
                + buid2
                + "; WISE_HIS_PM=1; MBD_UL=U10uJVMHwyAdx5JRNydpyw; BDUSS=duZFVxfk8zdUthODF-U0U0VEcxOXJRaU1TeWdobzB3"
                + MyUtil.makeNonce(10) + "hHOXBtMXhiQVFBQUFBJCQAAAAAAAAAAAEAAA" + MyUtil.makeNonce(6)
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGkONVtpDjVbWE; BIDUPSID=04C5CB99B5324855EFE99ABD93C00B58; MSA_WH=414_613; MBD_AT=1528471206; BAIDUID="
                + buids + "; \r\n");
        // data.append("Cookie:
        // BAIDUCUID=_aHMi_ikHi_m8v8Mg8v0fliK2u_D8vunl8Seija9HiiLu28hgu278_aj2tjRa2tHA;
        // BAIDUID=" + buids +"; MBD_AT=1544365777;
        // BDORZ=FAE1F8CFA4E8841CC28A015FEAEE495D;
        // BDPASSGATE=IlPT2AEptyoA_yiU4V4v3kIN8ejBVrmAH4PJSEptQlePdCyWmhTOAqVyEzChZm_YHi4hzp3acMAJxyndVi2ed0YudR1JojBLfEOdxq_Kr0vtKx2-fK1w0a4jKUE2sA8YbKBzx4w0RQVGZzEub0v5h2druhKl73JQb4r5y5C7gKrl_oGm2X8My88b28RkPGbAPNu594rXhkdTNkSdUuL2LTTtmXA9Ypku5LWziLoQ1eD-zDELDfLoOPMaG705JiW;
        // delPer=0; PSINO=5; BAIDULOC=13432834_3639127_66_224_1544506576339;
        // BDUSS=GJBZjFodlQ0MnJXelFkLTczUlkzfmhMbFFYNHV3U0xsSGMzUXN4RGpwMHM0VFpjQVFBQUFBJCQAAAAAAAAAAAEAAADiRmoqt-e46DExMTExAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACxUD1wsVA9cM;
        // \r\n");
        data.append("X-Forwarded-For: " + MyUtil.getIp() + "\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 剧赞
    private byte[] gouvote(String uid, String cookie, String s, String vid) {
        cookie = MyUtil.midWord("SUB=", ";", cookie);
        StringBuilder data = new StringBuilder(900);
        //uid = MyUtil.makeNumber(10);
        // http://i.topic.tv.weibo.com/module/relivevote?oid=6412297026&mod_id=1471&from_mod_id=0&name=%E6%AF%95%E9%9B%AF%E7%8F%BA
        data.append("GET /2/page/button?request_url=http%3A%2F%2Fi.dianshi.weibo.com%2Fji_gouvote%3Fmid%3D4310580644387329%26active_id%3D3015%26na_id%3D1001&networktype=wifi&accuracy_level=0&cardid=Square_DoubleButton&sensors_device_id=45a4fef39d2c744d&uicode=10000011&moduleID=700&featurecode=10000085&wb_version=3797&lcardid=2310020003_hotweibo_wbcard&c=android&i=f703d9e&s=08544313&ft=0&ua=QiKU-8681-M02__weibo__8.12.1__android__android5.1&wm=14056_0004&aid=01AhYm6FPWTx0v0lrvRcLCaRRjhnYQKA3lkHceZ5jZ9bjUJ_g.&fid=231219_3015_newartificial_1001&v_f=2&v_p=70&from=108C195010&gsid=_2A25xHIA8DeRxGeBG7VEU9ybOyjuIHXVTi5T0rDV6PUJbkdAKLVT-kWpNRhJ03h2jNbY8CzDmT5O1L8r2MXEnnTXo&lang=zh_CN&lfid=2302836157271643&skin=default&oldwm=14056_0004&sflag=1&luicode=10000198&sensors_is_first_day=true HTTP/1.1\r\n");
        data.append("Host: api.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: 8681-M02_5.1_weibo_8.12.1_android\r\n");
        data.append("X-Log-Uid: 6863578217\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    // 综艺大赏
    private byte[] relivevote(String name, String cookie, String s, String vid) {
        cookie = MyUtil.midWord("SUB=", ";", cookie);
        StringBuilder data = new StringBuilder(900);
        // uid = MyUtil.makeNumber(10);
        // http://i.topic.tv.weibo.com/module/relivevote?oid=6412297026&mod_id=1471&from_mod_id=0&name=%E6%AF%95%E9%9B%AF%E7%8F%BA
        // http://i.topic.tv.weibo.com/module/relivevote?oid=1978841494&mod_id=1471&from_mod_id=0&name=%E6%9D%8E%E5%AD%90%E7%92%87
        name = URLEncoder.encode(name);
        name = URLEncoder.encode(name);
        data.append(
                "GET https://api.weibo.cn/2/page/button?request_url=http%3A%2F%2Fi.topic.tv.weibo.com%2Fmodule%2Frelivevote%3Foid%3D"
                        + vid + "%26mod_id%3D1471%26from_mod_id%3D0%26name%3D%" + name
                        + "&networktype=wifi&accuracy_level=0&cardid=yanyuan_relive_vote_button&uicode=10000011&moduleID=700&featurecode=10000085&wb_version=3654&c=android&i=f842b7a&s="
                        + s
                        + "&ft=11&ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&wm=9006_2001&aid=01Anlv2XwdpcqURzkYptXmiLjU9xJv2UnDaKr12aNYUkBHuVU.&fid=231610_standard_cardlist_1463_yanyuan_tabs&v_f=2&v_p=62&from=1086395010&gsid="
                        + cookie
                        + "&lang=zh_CN&lfid=231610_standard_cardlist_1463_yanyuan_tabs&skin=default&oldwm=9006_2001&sflag=1&luicode=10000011 HTTP/1.1\r\n");
        data.append("Host: api.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");

        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 微博之夜
    private byte[] netchina2018(String uid, String vid, String cookies) {
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[2];
        }
        StringBuilder data = new StringBuilder(900);
        String temp = "item_no=" + vid + "&clientType=mobile&type=2\r\n";
        data.append("POST /netchina2018/aj_vote?currentuid=" + uid + "&item_no=" + vid + " HTTP/1.1\r\n");
        data.append("Host: huodong.weibo.cn\r\n");
        data.append("Accept: application/json\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append("Accept-Language: en-us\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("Origin: https://huodong.weibo.cn\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "Referer: https://huodong.weibo.cn/netchina2018/h5_voteresult?item_id=pfwe%2FgFHecXZL%2FsHLTMeEw%3D%3D&sinainternalbrowser=topnav&luicode=10000011&lfid=100165_-_netchina2018_-_index_-_toolbar\r\n");
        data.append("Content-Length: " + temp.length() + "\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    // 秒拍签到+0秒视频领分 type为1时为签到 为2时为视频领分
    private byte[] task(String cookie, String type) {

        StringBuilder data = new StringBuilder(900);
        // String temp = "";
        data.append("GET /api/aj_wbstory/task.json?appid=530&suid=" + cookie + "&type=" + type
                + "&_cb=_jsonp4r2gf10n9gp HTTP/1.1\r\n");
        data.append("Host: n.miaopai.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 8.0.0; EDI-AL10 Build/HUAWEIEDISON-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36 miaopai_android/INSVERSION-7.1.40\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: http://n.miaopai.com/weibo2018\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.9\r\n");
        data.append("X-Requested-With: com.yixia.videoeditor\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 秒拍微博之夜送分
    private byte[] miaovote(String cookie, String vid, String uid) {
        StringBuilder data = new StringBuilder(900);
        // String temp = "";
        data.append("GET /api/aj_wbstory/vote.json?appid=530&suid=" + cookie + "&uid=" + uid + "&id=" + vid
                + "&_cb=_jsonph6n0z9xrvgi HTTP/1.1\r\n");
        data.append("Host: n.miaopai.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 8.0.0; EDI-AL10 Build/HUAWEIEDISON-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36 miaopai_android/INSVERSION-7.1.40\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: http://n.miaopai.com/weibo2018\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.9\r\n");
        data.append("X-Requested-With: com.yixia.videoeditor\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 秒拍账号查分
    private byte[] fansvotes(String cookie) {
        StringBuilder data = new StringBuilder(900);
        // String temp = "";
        data.append(
                "GET /api/aj_wbstory/fansvotes.json?appid=530&suid=" + cookie + "&_cb=_jsonpd8awzj7t72d HTTP/1.1\r\n");
        data.append("Host: n.miaopai.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 8.0.0; EDI-AL10 Build/HUAWEIEDISON-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/70.0.3538.110 Mobile Safari/537.36 miaopai_android/INSVERSION-7.1.40\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: http://n.miaopai.com/weibo2018\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.9\r\n");
        data.append("X-Requested-With: com.yixia.videoeditor\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // v峰会投票 1为宣传，2为拉票，3为想见他
    private byte[] vfh2018(String cookies, String vid, String type, String source) {
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        // if (cookiess.length > 0) {
        // cookie = cookiess[2];
        // }
        cookie = cookiess[0];
        StringBuilder data = new StringBuilder(900);
        // (3e5 + s + o + t + 1 + e.uid + "me.verified.weibo.com")
        String t = MyUtil.getTime();
        String h = MyUtil.MD5("300000" + source + t + type + "1" + vid + "me.verified.weibo.com");
        String temp = "type=" + type + "&vuid=" + vid + "&v_score=1&source=" + source + "&ts=" + t
                + "&expire=300000&sign=" + h + "&security_id=7b4f070032225cfb93308c976e8ac54e&\r\n";
        data.append("POST /vfh2018/ajax/vote HTTP/1.1\r\n");
        data.append("Host: me.verified.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: " + temp.length() + "\r\n");
        data.append("Accept: application/json, text/plain, */*\r\n");
        data.append("Origin: https://me.verified.weibo.com\r\n");
        data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2)\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("Referer: https://me.verified.weibo.com/vfh2018/vote2018\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("X-Requested-With: com.sina.weibo\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    // 回放
    private byte[] flashplayinfo(String cookie, String vid, String uid) {
        StringBuilder data = new StringBuilder(900);
        // (3e5 + s + o + t + 1 + e.uid + "me.verified.weibo.com")
        vid = URLEncoder.encode(vid);
        String t = String.valueOf(System.currentTimeMillis() + (int) 36e5);
        String h = MyUtil.MD5("id=" + vid + "&uid=" + uid + "&fr=h5&expires=" + t + "22f18e62a05fb5e4fe4000e6e97ced10");
        // String temp = "type="+ type +"&vuid="+ vid
        // +"&v_score=1&source=32&ts="+ t+"&expire=300000&sign="+ h+"&\r\n";

        data.append("GET /api/live/flashplayinfo?id=" + vid + "&uid=" + uid + "&fr=h5&expires=" + t + "&sign=" + h
                + " HTTP/1.1\r\n");
        data.append("Host: ing.weibo.com\r\n");
        data.append("Origin: http://live.weibo.com\r\n");

        data.append("Cookie: \r\n");
        data.append("Accept: */*\r\n");
        data.append(
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15\r\n");
        data.append("Accept-Language: en-us\r\n");
        data.append("Referer: http://live.weibo.com/show\r\n");
        data.append("Connection: keep-alive\r\n");

        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    private byte[] buttonVote(String cookie, String s, String uid, String vid) {
        StringBuilder data = new StringBuilder(900);
        cookie = MyUtil.midWord("SUB=", ";", cookie) == null ? cookie : MyUtil.midWord("SUB=", ";", cookie);

        String olds = MyUtil.midWord("&s=", "&", vid);
        String oldgsid = MyUtil.midWord("&gsid=", "&", vid);
        String oldi = MyUtil.midWord("&i=", "&", vid);
        String oldua = MyUtil.midWord("&ua=", "&", vid);
        String oldfrom = MyUtil.midWord("&from=", "&", vid);

        String temp = "";
        temp = vid.replace(olds, s);
        temp = temp.replace(oldgsid, cookie);
        temp = temp.replace(oldi, "f842b7a");
        temp = temp.replace(oldua, "HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2");
        temp = temp.replace(oldfrom, "1086395010");


        data.append(temp + "\r\n");
        data.append("Host: api.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2\r\n");
        data.append("X-Log-Uid: " + uid + "\r\n");
        //data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //读书取vid
    private byte[] readbookVid(String cookies, String uid) {
        StringBuilder data = new StringBuilder(900);
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[2];
        }

        data.append("GET /api/container/getIndex?uid=" + uid + "&wm=9847_0002&sourcetype=weixin&from=singlemessage&isappinstalled=0&sudaref=login.sina.com.cn&containerid=1059030002_7095_7 HTTP/1.1\r\n");
        data.append("Host: m.weibo.cn\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://m.weibo.cn/\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    //读书
    private byte[] readbook(String cookies, String vid, String sig) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[2];
        }

        data.append("GET /movie/commonvote?theme_id=234&option_id=" + vid + "&sig=" + sig + "&luicode=10000011&lfid=1059030002_7095_7 HTTP/1.1\r\n");
        data.append("Host: movie.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://m.weibo.cn/\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: " + cookie + "\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();

    }


    //跑步活动 http://sports.sina.cn/running/paotuan/2018/vip.d.html?tid=14745&from=groupmessage

    private byte[] paotuan(String cookies,String uid,String vid) {
        String[] cookiess = cookies.split("\\^");
        String cookie = "";
        if (cookiess.length > 2) {
            cookie = cookiess[2];
        }

        StringBuilder data = new StringBuilder(900);
        String temp = "app_res_id="+ vid +"&uid="+ uid +"&callback=ijax_"+ MyUtil.getTime()+"_82491727&\r\n";
        //paotuan2018_14745
        data.append("POST /digg/index/dayMultiIncrMany?callback=ijax_"+ MyUtil.getTime()+ "_82491727& HTTP/1.1\r\n");
        data.append("Host: utils.sports.sina.cn\r\n");
        data.append("Content-Length: "+ temp.length() +"\r\n");
        data.append("Cache-Control: max-age=0\r\n");
        data.append("Origin: http://sports.sina.cn\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: http://sports.sina.cn/running/paotuan/2018/vip.d.html?tid=14745&from=groupmessage\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    private byte[] votepopvid(String cookie ,String vid){
        StringBuilder data =new StringBuilder(900);

        cookie=getcookieIndex(cookie,2);

        data.append("GET https://movie.weibo.com/movie/commonvote/votepop?ua=HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1&from=108A395010&option_id="+ vid +"&theme_id=241 HTTP/1.1\r\n");
        data.append("Host: movie.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Linux; Android 6.0.1; VKY_AL00 Build/V417IR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36 Weibo (HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1)\r\n");
        data.append("x-user-agent: HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    private byte[]  votepop(String cookie,String sig ,String vid){
        StringBuilder data =new StringBuilder(900);

        cookie=getcookieIndex(cookie,2);

        data.append("GET https://movie.weibo.com/movie/commonvote/voteByH5PopWindows?theme_id=241&option_id="+ vid +"&sig="+ sig +"&num=8& HTTP/1.1\r\n");
        data.append("Host: movie.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Accept: application/json, text/plain, */*\r\n");
        data.append("User-Agent: Mozilla/5.0 (Linux; Android 6.0.1; VKY_AL00 Build/V417IR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36 Weibo (HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1)\r\n");
        data.append("Referer: https://movie.weibo.com/movie/commonvote/votepop?ua=HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1&from=108A395010&option_id=2906&theme_id=241\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    private byte[] lpl(String cookie,String vid){
        StringBuilder data =new StringBuilder(900);

        cookie=getcookieIndex(cookie,0);

        data.append("GET https://sports.weibo.com/vote/h5voteresult?vid="+ vid +"&themeid=92&status=100000&sinainternalbrowser=topnav HTTP/1.1\r\n");
        data.append("Host: sports.weibo.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Linux; Android 6.0.1; VKY_AL00 Build/V417IR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/52.0.2743.100 Mobile Safari/537.36 Weibo (HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1)\r\n");
        data.append("x-user-agent: HUAWEI-VKY_AL00__weibo__8.10.3__android__android6.0.1\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
        data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    //miaopai follow......
    private byte[] miaopaiFollow(String cookies, String uid) {
        StringBuilder data = new StringBuilder(900);
        String temp = "suid="+ uid +"\r\n";

        data.append("POST http://b-api.ins.miaopai.com/1/relation/follow.json HTTP/1.1\r\n");
        data.append("cp_vend: miaopai\r\n");
        data.append("cp_uniqueId: cacb31a4-572a-3685-9fc8-eec43313129f\r\n");
        data.append("cp_abid: 2-1,1-102,18-100,1-10,24-101,5-200,2-201\r\n");
        data.append("cp_os: android\r\n");
        data.append("cp_channel: miaopai\r\n");
        data.append("cp_sign: 302bdfe800805e14a56ac7c73034928d\r\n");
        data.append("cp_sver: 6.0.1\r\n");
        data.append("cp_time: 1551360014\r\n");
        data.append("cp_uuid: cacb31a4-572a-3685-9fc8-eec43313129f\r\n");
        data.append("cp_appid: 424\r\n");
        data.append("cp_ver: 7.1.92\r\n");
        data.append("cp_token: "+ cookies +"\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded;charset=utf-8\r\n");
        data.append("Host: b-api.ins.miaopai.com\r\n");
        data.append("Connection: Keep-Alive\r\n");
        data.append("User-Agent: okhttp/3.3.1\r\n");
        data.append("Content-Length: "+ temp.length() +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }
    //miaopai Unfollow......
    private byte[] miaopaiUnFollow(String cookies, String uid) {
        StringBuilder data = new StringBuilder(900);
        String temp = "suid="+ uid +"\r\n";

        data.append("POST http://b-api.ins.miaopai.com/1/relation/unfollow.json HTTP/1.1\r\n");
        data.append("cp_vend: miaopai\r\n");
        data.append("cp_uniqueId: cacb31a4-572a-3685-9fc8-eec43313129f\r\n");
        data.append("cp_abid: 2-1,1-102,18-100,1-10,24-101,5-200,2-201\r\n");
        data.append("cp_os: android\r\n");
        data.append("cp_channel: miaopai\r\n");
        data.append("cp_sign: eb5149721cb39ab7cd73f731f4152bd5\r\n");
        data.append("cp_sver: 6.0.1\r\n");
        data.append("cp_time: 1551267841\r\n");
        data.append("cp_uuid: cacb31a4-572a-3685-9fc8-eec43313129f\r\n");
        data.append("cp_appid: 424\r\n");
        data.append("cp_ver: 7.1.92\r\n");
        data.append("cp_token: "+ cookies +"\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded;charset=utf-8\r\n");
        data.append("Host: b-api.ins.miaopai.com\r\n");
        data.append("Connection: Keep-Alive\r\n");
        data.append("User-Agent: okhttp/3.3.1\r\n");
        data.append("Content-Length: "+ temp.length() +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

}
