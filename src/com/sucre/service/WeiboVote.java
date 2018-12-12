package com.sucre.service;

import java.net.CookieHandler;
import java.net.URLEncoder;
import java.nio.file.attribute.AclEntry.Builder;
import java.util.Base64.Encoder;
import java.util.Spliterator;

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
			case "剧赞":
				ret = nets.goPost("api.weibo.cn", 443, gouvote(super.getUid(), super.getCookie(), super.getS(), vid));
				if (!MyUtil.isEmpty(ret)) {
					if (ret.indexOf("result\":1") != -1) {
						MyUtil.print("剧赞成功！", Factor.getGui());
					} else {
						MyUtil.print("剧赞失败！" + MyUtil.midWord("msg\":\"", "\"", ret), Factor.getGui());
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
						if(ret.indexOf("\u4eca\u5929\u7968\u6570\u5df2\u7528\u5b8c")!=-1){
							break;
						}
					}
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
		uid = MyUtil.makeNumber(10);
		data.append("GET /2/page/button?request_url=http%3A%2F%2Fi.dianshi.weibo.com%2Fji_gouvote%3Fmid%3D" + vid
				+ "%26uid%3D" + uid
				+ "%26active_id%3D3015%26na_id%3D1001&networktype=wifi&accuracy_level=0&cardid=Square_DoubleButton&uicode=10000011&moduleID=700&featurecode=10000085&wb_version=3654&c=android&i=f842b7a&s="
				+ s
				+ "&ft=11&ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&wm=9006_2001&aid=01Anlv2XwdpcqURzk"
				+ MyUtil.makeNonce(32) + ".&fid=231219_3015_newartificial_1001&v_f=2&v_p=62&from=1086395010&gsid="
				+ cookie
				+ "&lang=zh_CN&lfid=100303type%3D1%26q%3D%E5%89%A7%E8%B5%9E%26t%3D0&skin=default&oldwm=9006_2001&sflag=1&luicode=10000003 HTTP/1.1\r\n");
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
}
