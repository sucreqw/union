package com.sucre.service;

import java.util.Base64;

import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;
import org.xml.sax.helpers.NamespaceSupport;

import com.sucre.entity.Weibo;
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
						return 1;
					}
				}
			}
			// return 1;
			// 取拖动的难码 并验证！
		case "getpicD":
			ret=net.goPost("captcha.weibo.com", 443, getPicD(super.getUid()));
			if(!MyUtil.isEmpty(ret)){
				String picUrl=MyUtil.midWord("backgroundPath\":\"", "\",\"", ret);
				String vid=MyUtil.midWord("id\":\"", "\",\"", ret);
				String Hash=MyUtil.midWord("x\":\"", "\",\"", ret);		
						
				
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

}
