package com.sucre.service;

import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;
import com.sucre.myNet.Nets;

public class weiboLogin extends Weibo {
	public String vid = "";

	public weiboLogin() {
		super();
	}

	public weiboLogin(String id, String pass) {
		super(id, pass);
	}

	public weiboLogin(String inputdata) {
		super(inputdata);
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public int Login(int index) {
		int rets = 0;
		Nets net = new Nets();
		String ret = "";

		ret = net.goPost("passport.weibo.cn", 443, login(super.getId(), super.getPass()));
		if (!MyUtil.isEmpty(ret)) {
			if (ret.indexOf("20000000") != -1) {

				// MyUtil.outPutData("cookie.txt",
				// super.getCookie() + "|" + super.getUid() + "|" +
				// super.getId() + "|" + super.getPass());
				// MyUtil.print("登录成功！" + (index + 1), Factor.getGui());
				String url = MyUtil.midWord("crossdomain", "\",\"", ret);
				ret = net.goPost("passport.weibo.com", 443, loginsso(url));
				if (ret.indexOf("20000000") != -1) {
					super.setCookie(MyUtil.getAllCookie(ret));
					super.setUid(MyUtil.midWord("uid\":\"", "\",\"", ret));
					MyUtil.outPutData("cookie.txt", super.toString());
					// return 1;
					rets = 1;
				}
			} else {
				// 请输入验证码
				if (ret.indexOf("50011005") != -1) {
					rets = -1;
				} else {
					MyUtil.print("登录失败！" + (index + 1), Factor.getGui());
					rets = 0;

				}
			}

		}

		return rets;
	}

	// 登录接口
	private byte[] login(String id, String pass) {
		StringBuilder data = new StringBuilder(900);
		String temp = "username=" + id + "&password=" + pass
				+ "&savestate=1&r=https%3A%2F%2Fm.weibo.cn%2F&ec=2&pagerefer=https%3A%2F%2Fm.weibo.cn%2Flogin%3FbackURL%3Dhttps%25253A%25252F%25252Fm.weibo.cn%25252F&entry=mweibo&wentry=&loginfrom=&client_id=&code=&qq=&mainpageflag=1&vid="
				+ vid + "&hff=&hfp=\r\n";

		data.append("POST https://passport.weibo.cn/sso/login HTTP/1.1\r\n");
		data.append("Host: passport.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: " + temp.length() + "\r\n");
		data.append("Origin: https://passport.weibo.cn\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Accept: */*\r\n");
		data.append("Referer: https://m.weibo.cn/\r\n");

		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");

		data.append(temp);
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}

	// 第二级接口
	private byte[] loginsso(String url) {
		StringBuilder data = new StringBuilder(900);
		data.append("GET https://passport.weibo.com/sso/crossdomain" + url + " HTTP/1.1\r\n");
		data.append("Host: passport.weibo.com\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Origin: https://passport.weibo.com\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Accept: */*\r\n");
		data.append("Referer: https://m.weibo.cn/\r\n");

		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	@Override
	public int Actions(int index, String mission) {
		return Login(index);
	}
}
