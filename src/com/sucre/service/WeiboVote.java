package com.sucre.service;

import java.net.CookieHandler;
import java.net.URLEncoder;
import java.util.Base64.Encoder;

import javax.swing.text.TabStop;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

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
					i=Controller.getInstance().getVidImpl().getSize();
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
					MyUtil.print("失败！！" + (index + 1)+"<>"+super.getId() +"|" + super.getPass(), Factor.getGui());
				}
				break;
				
			case "沸点":
				ret=nets.goPost("mbd.baidu.com", 443, starhit(vid));
				if(!MyUtil.isEmpty(ret)) {
					MyUtil.counts++;
					MyUtil.print("当前票数："+ MyUtil.midWord("vote\":", ",\"", ret)+"<==>软件投出票数：" +MyUtil.counts , Factor.getGui());
					
					
				}
				break;
			}// end of switch

		}

		return 0;
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
	
	//百度沸点
	private byte[] starhit(String vid) {
		StringBuilder data = new StringBuilder(900);
		//String temp = "";
		data.append("GET https://mbd.baidu.com/webpage?type=starhit&action=vote&format=json&starid="+ vid +"&votetype=1 HTTP/1.1\r\n");
		data.append("Host: mbd.baidu.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/11.1 light/1.0 baiduboxapp/11.1.0.10 (Baidu; P1 4.4.2)\r\n");
		data.append("Content-Type: \r\n");
		data.append("Accept: */*\r\n");
		data.append("Referer: https://mbd.baidu.com/webpage?type=starhit&action=starhome&starid="+ vid +"&from=alading\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.9\r\n");
		data.append("Cookie: BDUSS="+ MyUtil.makeNonce(12)+"Vk9XLXVtNHFWRlhKTXF4MkxmU3NwMX54dld"+ MyUtil.makeNonce(6)+"2llRWNYUkpjQVFBQUFBJCQAAAAAAAAAAAEAAA"+ MyUtil.makeNonce(26)+"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ MyUtil.makeNonce(14)+"S;\r\n");
		data.append("X-Forwarded-For: "+ MyUtil.getIp()+"\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}
	
}
