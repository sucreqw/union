package com.sucre.service;

import java.util.Base64;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import org.xml.sax.helpers.NamespaceSupport;

import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.JsUtil;
import com.sucre.utils.MyUtil;
import com.sucre.utils.SinaCapchaUtil;

public class WeiboCapcha extends Weibo {
	private String picId = "";
	private String Base64Image = "";
	private String picIndex = "";
	private String data_enc = "";
	private String path_enc = "";
	private String voteId = "";

	@Override
	public int Actions(int index, String mission) {
		Nets net = new Nets();
		String ret = "";
		switch (mission) {
		case "getpic":

			String code = "";
			String t = "";
			while (true) {
				do {
					MyUtil.print("开始取图！", Factor.getGui());
					byte[] retb = null;
					t = String.valueOf(System.currentTimeMillis());
					ret = net.goPost("captcha.weibo.com", 443, getPic(super.getId()));
					if (!MyUtil.isEmpty(ret)) {

						picId = MyUtil.midWord("id\":\"", "\",\"", ret);
						Base64Image = MyUtil.midWord("data:image\\/gif;base64,", "|", ret);
						picIndex = MyUtil.midWord("|", "\",\"", ret);
						if (!MyUtil.isEmpty(Base64Image) && !MyUtil.isEmpty(picIndex)) {
							Base64Image = Base64Image.replaceAll("\\\\", "");
							picIndex = picIndex.replaceAll("\\\\", "");
							retb = SinaCapchaUtil.getPic(picIndex, Base64Image);
							// MyUtil.outPutData(MyUtil.getTime() +".gif",
							// retb);
							MyUtil.print("对比验证码！", Factor.getGui());
							// 对比验证码
						}
					}
					code = "";
					if (null != retb) {
						code = SinaCapchaUtil.compareAll(retb);
						if ("".equals(code)) {
							// MyUtil.outPutData(MyUtil.getTime() + ".gif",
							// retb);
						}
					}
				} while ("".equals(code));

				path_enc = JsUtil.runJS("path_enc", code, picId);
				data_enc = JsUtil.runJS("data_enc", Integer.parseInt(code), t);
				ret = net.goPost("captcha.weibo.com", 443, verify(picId, data_enc, path_enc, super.getId()));
				if (!MyUtil.isEmpty(ret)) {
					if (ret.indexOf("100000") != -1) {
						// 验证成功了。

						return 1;
					}
				}
			}
			// return 1;
			// 取拖动的验证码 并验证！
		case "getpicD":
			try {
				MyUtil.print("开始取图！", Factor.getGui());
				// super.setUid("6828954865");
				ret = net.goPost("captcha.weibo.com", 443, getPicD(super.getUid()));
				if (!MyUtil.isEmpty(ret)) {
					String picUrl = MyUtil.midWord("backgroundPath\":\"", "\",\"", ret);
					picId = MyUtil.midWord("id\":\"", "\",\"", ret);
					String Hash = MyUtil.midWord("x\":\"", "\",\"", ret);
					Base64.Decoder decoder = Base64.getDecoder();
					Hash = new String(decoder.decode(Hash));
					String indexP = MyUtil.midWord("seqo8,", "", Hash);
					String startP = MyUtil.midWord("pos", "end", Hash + "end").substring(1);
					String[] startPs = startP.split(",");
					byte[] pic = net.goPostByte("captcha.weibo.com", 443, getImage(picUrl));
					if (pic != null && indexP != null) {
						// MyUtil.outPutData("temp.jpg", pic);
						byte[] rets = SinaCapchaUtil.recombineShadow(indexP, pic);
						// MyUtil.outPutData("temp.jpg", rets);
						String result = SinaCapchaUtil.grayImage2(rets, Integer.parseInt(startPs[1]));
						// MyUtil.outPutData(result + "a.jpg", rets);
						// System.out.println(result);
						if (!"".equals(result)) {
							MyUtil.print("识别结果：" + result, Factor.getGui());
							String[] p = result.split(",");

							for (int i = 0; i < p.length; i++) {
								if (!"".equals(p[i])) {
									ret = net.goPost("captcha.weibo.com", 443, verifyD(picId, super.getUid(),
											getjs(simulate(startPs[0], p[i])),
											"i00uAnVhbE1vemlsbGEvNS4wIChXaW5kb3dzIE5UIDEwLjA7IFdPVzY0KSBBcHBsZVdlYktpdC81MzcuMzYgKEtIVE1MLCBsaWtlIEdlY2tvKSBDaHJvbWUvNzEuMC4zNTczLjAgU2FmYXJpLzUzNy4zNgRwbGF0BVdpbjMyBGxhbmcFemgtQ04Gc2NyZWVuCDc2OCoxMzY2AmZwCjQxMjk1NDA0NTkFc2NhbGUBMQJsdA0xNTQyODk4ODA0MzY4AmN0DTE1NDI4OTg4MTMyODUCc3QNMTU0Mjg5ODgwNTkzNQ=="));
									if (!MyUtil.isEmpty(ret)) {
										if (ret.indexOf("100000") != -1) {
											// 识别成功！验证超级话话题
											ret = net.goPost("", 443,
													captchareverify(super.getCookie(), voteId, picId));
											if (!MyUtil.isEmpty(ret)) {
												if (ret.indexOf("succ") != -1) {
													MyUtil.print("验证成功" + super.getId() + "|" + super.getPass() + "|",
															Factor.getGui());
													return 1;
												}
											}
										}
										System.out.println(ret);
										MyUtil.print("验证失败！" + super.getId() + "|" + super.getPass() + "|",
												Factor.getGui());
										//System.out.print(ret);
									}else{
										//网络错误，再来一次!
										i--;
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				MyUtil.print("识别验证码出错了！" + e.getMessage(), Factor.getGui());
				System.out.println(ret);
			}
			break;

		}
		return 0;
	}

	public String getEnc() {
		return this.data_enc;
	}

	public String pathEnc() {
		return this.path_enc;
	}

	public String getVid() {
		return this.picId;
	}

	public void setVoteId(String vid) {
		this.voteId = vid;
	}

	public void setVid(String vid) {
		this.picId = vid;
	}

	public static String simulate(String start, String end) {
		// int i=0;
		String r = MyUtil.getRand(48, 35);
		int counts = 0;
		int st = 0;
		int ends = 0;
		if (Integer.parseInt(start) > Integer.parseInt(end)) {

			st = Integer.parseInt(start) + Integer.parseInt(r);

			counts = Integer.parseInt(start) - Integer.parseInt(end);
			ends = st - counts;

		} else {
			st = Integer.parseInt(start) + Integer.parseInt(r);

			counts = Integer.parseInt(end) - Integer.parseInt(start);
			ends = st + counts;

		}
		int Times = 0;
		String ret = "var trace=[[" + st + ", 250, " + Times + "]];\r\n";

		while (true) {
			int temp = Integer.parseInt(MyUtil.getRand(5, 0));
			Times += Integer.parseInt(MyUtil.getRand(50, 20));

			if (Integer.parseInt(start) < Integer.parseInt(end)) {

				st += temp;
				if (st > ends) {
					st = ends;
				}
			} else {
				st -= temp;
				if (st < ends) {
					st = ends;
				}
			}

			ret += "trace.push([" + st + ", 250, " + Times + "]);\r\n";

			if (st == ends) {
				return ret;
			}
			// i+=temp;

		}
	}

	public static String getjs(String js) {
		String nJS = "function getit() {";
		nJS += js;
		nJS += "return x97a57645a3f0e1518f8c9f4d340d4c4f(trace);";
		nJS += "}";

		JsUtil.AddJs(nJS);

		return JsUtil.runJS("getit");
	}

	// 取九宫格图片
	private byte[] getPic(String id) {
		StringBuilder data = new StringBuilder(900);
		data.append(
				"GET https://captcha.weibo.com/api/pattern/get?ver=daf139fb2696a4540b298756bd06266a&source=ssologin&usrname="
						+ id + "&line=160&side=100&radius=30&_rnd=0.0" + MyUtil.makeNumber(16)
						+ "&callback=pl_cb HTTP/1.1\r\n");
		data.append("Host: captcha.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=https%3A%2F%2Fm.weibo.cn%2F\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	// 验证九宫格图片
	private byte[] verify(String vid, String data_enc, String path_enc, String id) {
		StringBuilder data = new StringBuilder(900);

		data.append("GET https://captcha.weibo.com/api/pattern/verify?ver=daf139fb2696a4540b298756bd06266a&id=" + vid
				+ "&usrname=" + id + "&source=ssologin&path_enc=" + path_enc + "&data_enc=" + data_enc
				+ "&callback=pl_cb HTTP/1.1\r\n");
		data.append("Host: captcha.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=https%3A%2F%2Fm.weibo.cn%2F\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	// 取拖动的图片
	private byte[] getPicD(String uid) {
		StringBuilder data = new StringBuilder(900);
		data.append("GET //api/jigsaw/get?ver=1.0.0&source=topic&usrname=" + uid + "&_rnd=0." + MyUtil.makeNumber(16)
				+ "&callback=pl_cb HTTP/1.1\r\n");
		data.append("Host: captcha.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Accept: */*\r\n");

		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	// 验证拖动图片
	private byte[] verifyD(String vid, String uid, String p, String s) {
		StringBuilder data = new StringBuilder(900);

		data.append("GET //api/jigsaw/verify?ver=1.0.0&id=" + vid + "&usrname=" + uid + "&source=topic&p=" + p + "&s="
				+ s + "&callback=pl_cb HTTP/1.1\r\n");
		data.append("Host: captcha.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Accept: */*\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	// 取拖动的图片byte[]
	private byte[] getImage(String url) {
		StringBuilder data = new StringBuilder(900);

		data.append("GET https://captcha.weibo.com/" + url + " HTTP/1.1\r\n");
		data.append("Host: captcha.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Accept: image/webp,image/apng,image/*,*/*;q=0.8\r\n");
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	// 打榜验证码 验证
	private byte[] captchareverify(String cookie, String vid, String pid) {
		StringBuilder data = new StringBuilder(900);
		String temp = "id=" + pid + "&oid=" + vid + "\\r\\n";
		data.append("POST /aj/super/captchareverify HTTP/1.1\r\n");
		data.append("Host: huati.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: 83\r\n");
		data.append("Origin: https://huati.weibo.cn\r\n");
		data.append("X-Requested-With: XMLHttpRequest\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2)\r\n");
		data.append("Content-type: application/x-www-form-urlencoded\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://huati.weibo.cn/super/captcha/?ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&from=1086395010&type=pick&page_id=1008086de98a1a1a398df9761c706bfaac6b00\r\n");
		data.append("Accept-Encoding: gzip,deflate\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: " + cookie + "\r\n");
		data.append("\r\n");
		data.append(temp);
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}
}
