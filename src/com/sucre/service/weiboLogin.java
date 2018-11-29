package com.sucre.service;

import java.awt.List;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;

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
		String cookie = "";
		try {
			ret = net.goPost("passport.sina.cn", 443, login2(super.getId(), super.getPass()));
			if (!MyUtil.isEmpty(ret)) {
				if (ret.indexOf("20000000") != -1) {
					String uid = MyUtil.midWord("uid\":\"", "\"", ret);
					// MyUtil.outPutData("cookie.txt",
					// super.getCookie() + "|" + super.getUid() + "|" +
					// super.getId() + "|" + super.getPass());
					// MyUtil.print("登录成功！" + (index + 1), Factor.getGui());
					//密码对，直接记录
					rets=1;
					if(rets==1) {
						MyUtil.outPutData("cookie.txt", super.toString());
					return rets;
					}
					//密码对，直接记录
					ArrayList<String> url = MyUtil.midWordAll("crossdomain?", "\"", ret);
					String[] host = { "passport.weibo.com", "login.sina.com.cn", "passport.weibo.cn" };

					for (int i = 0; i < host.length; i++) {
						ret = net.goPost(host[i], 443, loginsso(url.get(i), host[i]));
						if (ret.indexOf("20000000") != -1) {
							cookie += MyUtil.getAllCookie(ret) + "^";
							if (uid == null || "null".equals(uid)) {
								uid = MyUtil.midWord("uid\":\"", "\"", ret);
							}

							// return 1;
							rets = 1;
						}
					}
					super.setCookie(cookie);
					super.setUid(uid);
					MyUtil.outPutData("cookie.txt", super.toString());
				} else {
					// 请输入验证码
					if (ret.indexOf("50011005") != -1) {
						//不识别验证码，直接记录
						MyUtil.outPutData("cookie.txt", super.toString());
						rets=1;
						//不识别验证码，直接记录
						//rets = -1;
					} else if(ret.indexOf("50060000")!=-1){
						/*MyUtil.print("再次换ip！" + (index + 1), Factor.getGui());
						rets = 2;*/
						rets=0;

					}else {
						//System.out.println(ret);
						MyUtil.print("登录失败！" + (index + 1) , Factor.getGui());
						rets = 0;
					}
				}

			}
		} catch (Exception e) {
			System.err.println(ret);
			MyUtil.print("登录出错！" + e.getMessage(), Factor.getGui());
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
	// 登录接口2
		private byte[] login2(String id, String pass) {
			StringBuilder data = new StringBuilder(900);
			String temp = "savestate=3650&username="+ id +"&password="+ pass +"&pagerefer=https://sina.cn/index/feed?from=touch&Ver=20&entry=wapsso&loginfrom=1";
			
			data=data.append("POST /sso/login HTTP/1.1\r\n");
			data=data.append("Host: passport.sina.cn\r\n");
			data=data.append("Connection: keep-alive\r\n");
			data=data.append("Content-Length: "+ temp.length() +"\r\n");
			data=data.append("Origin: http://my.sina.cn\r\n");
			data=data.append(
					"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36\r\n");
			data=data.append("Content-Type: application/x-www-form-urlencoded\r\n");
			data=data.append("Accept: */*\r\n");
			data=data.append("Referer: http://my.sina.cn/?vt=4&pos=108&his=0\r\n");
			data=data.append("Accept-Language: en-US,en;q=0.9\r\n");
			//data=data.append("Cookie: \r\n");
			data=data.append("\r\n");
			data=data.append(temp);
			data=data.append("\r\n");
			data=data.append("\r\n");

			return data.toString().getBytes();
		}

	// 第二级接口
	private byte[] loginsso(String url, String host) {
		StringBuilder data = new StringBuilder(900);
		data.append("GET /sso/crossdomain?" + url + " HTTP/1.1\r\n");
		data.append("Host: " + host + "\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Origin: https://" + host + "\r\n");
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
