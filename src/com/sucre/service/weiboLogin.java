package com.sucre.service;

import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;
import com.sucre.myNet.Nets;

public class weiboLogin extends Weibo {

	public weiboLogin() {
		super();
	}

	public weiboLogin(String id, String pass) {
		super(id, pass);
	}

	public weiboLogin(String inputdata) {
		super(inputdata);
	}

	public int Login(int index) {
		int rets=0;
		Nets net = new Nets();
		String ret = net.goPost("passport.sina.cn", 443, login(super.getId(), super.getPass()));
		if (!MyUtil.isEmpty(ret)) {
			if (ret.indexOf("20000000") != -1) {
				super.setCookie(MyUtil.getAllCookie(ret));
				super.setUid(MyUtil.midWord("uid\":\"", "\",\"", ret));
				MyUtil.outPutData("cookie.txt",
						super.getCookie() + "|" + super.getUid() + "|" + super.getId() + "|" + super.getPass());
				MyUtil.print("登录成功！" + (index + 1), Factor.getGui());
				// return 1;
				rets=1;
			} else {
				MyUtil.print("登录失败！" + (index + 1), Factor.getGui());
				//MyUtil.print(ret, null);
			}
			if ((index + 1) % 20 == 0 && "ip".equals(Thread.currentThread().getName())) {
				MyUtil.changIp();

			}
		}

		return rets;
	}

	// 登录接口
	private byte[] login(String id, String pass) {
		StringBuilder data = new StringBuilder(900);
		String temp = "savestate=3650&username=" + id + "&password=" + pass
				+ "&pagerefer=https://sina.cn/index/feed?from=touch&Ver=20&entry=wapsso&loginfrom=1";

		data = data.append("POST /sso/login HTTP/1.1\r\n");
		data = data.append("Host: passport.sina.cn\r\n");
		data = data.append("Connection: keep-alive\r\n");
		data = data.append("Content-Length: "+ temp.length() +"\r\n");
		data = data.append("Origin: http://my.sina.cn\r\n");
		data = data.append(
				"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36\r\n");
		data = data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data = data.append("Accept: */*\r\n");
		data = data.append("Referer: http://my.sina.cn/?vt=4&pos=108&his=0\r\n");
		data = data.append("Accept-Language: en-US,en;q=0.9\r\n");
		// data=data.append("Cookie: \r\n");
		data = data.append("\r\n");
		data = data.append(temp);
		data = data.append("\r\n");
		data = data.append("\r\n");

		return data.toString().getBytes();
	}

	@Override
	public int Actions(int index) {
		return Login(index);
	}
}
