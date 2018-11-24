package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

public class WeiboTopic extends Weibo {

	@Override
	public int Actions(int index, String mission) {
		Nets nets = new Nets();
		switch (mission) {
		case "checkin":
			// MyUtil.print("正在签到。.." + index, null);
			String allCookie=super.getCookie().split("^")[0];
			
			MyUtil.print("正在签到。.." + (index + 1), Factor.getGui());
			String ret = nets.goPost("huati.weibo.cn", 443, score(allCookie));

			if (!MyUtil.isEmpty(ret)) {

				// MyUtil.print("uid:" + uid + "<"+ id +";" + pass +">" +
				// "目前分数：" + MyUtil.midWord("value\":", ",", ret), null);
				MyUtil.print("uid:" + super.getUid() + "<" + super.getId() + ";" + super.getPass() + ">" + "目前分数："
						+ MyUtil.midWord("value\":", ",", ret), Factor.getGui());
			}
			// 此处需要循环取vid 关注和签到
			Vid v = new Vid();
			for (int i = 0; i < Controller.getInstance().getVidImpl().getSize(); i++) {
				v = Controller.getInstance().getVidImpl().getVid(i, v);
				String vid = v.getVids();

				MyUtil.print("正在关注话题。。" + i + "<>" + index,Factor.getGui());

				ret = nets.goPost("api.weibo.cn", 443,
						follow(midCookie(allCookie), super.getUid(), vid, super.getS()));

				ret = nets.goPost("api.weibo.cn", 443,
						checkin(midCookie(allCookie), super.getUid(), vid, super.getS()));
				// MyUtil.print("正在签到话题.." + i + "<>" + index, null);
				MyUtil.print("正在签到话题.." + (index + 1), Factor.getGui());
			}
			break;
		case "checktopic":

			break;
		case "followtopic":

			break;
		}
		return 0;
	}

	private String midCookie(String cookie) {
		return MyUtil.isEmpty(cookie) ? "" : MyUtil.midWord("SUB=", ";", cookie);
	}

	private byte[] score(String cookie) {
		StringBuilder data = new StringBuilder(900);
		String temp = "type=REQUEST&user_score=0\r\n";

		data.append("POST https://huati.weibo.cn/aj/super/receivescore HTTP/1.1\r\n");
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
				"Referer: https://huati.weibo.cn/super/taskcenter?ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&from=1086395010\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: " + cookie + "\r\n");
		data.append("\r\n");
		data.append(temp);
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}

	private byte[] follow(String cookie, String uid, String vid, String s) {

		StringBuilder data = new StringBuilder(900);
		// String temp = "";
		cookie = MyUtil.trimNull(cookie);
		uid = MyUtil.trimNull(uid);
		vid = MyUtil.trimNull(vid);

		data.append(
				"GET https://api.weibo.cn/2/friendships_pages/create?networktype=wifi&extparam=tabbar_follow%234296204685089364&cardid=1008080013_0&able_recommend=0&uicode=10000011&moduleID=700&wb_version=3654&c=android&i=f842b7a&s="
						+ s + "&ft=0&id=1022%3A" + vid
						+ "&ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&wm=9006_2001&aid=01Anlv2XwdpcqURzkYptXmiLgF3XZdgmTqaHowQEvwWF5xAFc.&fid="
						+ vid + "&v_f=2&v_p=62&from=1086395010&gsid=" + cookie
						+ "&lang=zh_CN&lfid=100803_-_recentvisit&skin=default&oldwm=9006_2001&sflag=1&cum=AFE0892D HTTP/1.1\r\n");
		data.append("Host: api.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		// data.append("X-Sessionid: 4a7f855c-2177-4edf-8932-9f24f7ab044b\r\n");
		// data.append("X-Validator:
		// qJAP41mNoIR0BCOfORW1xUqpRwIp4ZzBpuAEdfnXwl8=\r\n");
		data.append("X-Log-Uid: " + uid + "\r\n");
		data.append("User-Agent: Che2-TL00_4.4.2_weibo_8.6.3_android\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

	private byte[] checkin(String cookie, String uid, String vid, String s) {
		StringBuilder data = new StringBuilder(900);

		cookie = MyUtil.trimNull(cookie);
		uid = MyUtil.trimNull(uid);
		vid = MyUtil.trimNull(vid);

		data.append(
				"GET https://api.weibo.cn/2/page/button?request_url=http%3A%2F%2Fi.huati.weibo.com%2Fmobile%2Fsuper%2Factive_checkin%3Fpageid%3D"
						+ vid
						+ "&networktype=wifi&extparam=tabbar_follow%234296204685089364&accuracy_level=0&uicode=10000011&moduleID=700&wb_version=3654&c=android&i=f842b7a&s="
						+ s
						+ "&ft=0&ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&wm=9006_2001&aid=01Anlv2XwdpcqURzkYptXmiLgF3XZdgmTqaHowQEvwWF5xAFc.&fid="
						+ vid + "&v_f=2&v_p=62&from=1086395010&gsid=" + cookie
						+ "&lang=zh_CN&lfid=100803_-_recentvisit&skin=default&oldwm=9006_2001&sflag=1&cum=E0214B2C HTTP/1.1\r\n");
		data.append("Host: api.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		// data.append("X-Sessionid: 254d6771-e273-4459-a1f6-16ded81e4c68\r\n");
		// data.append("X-Validator:
		// dCVMGLasu0eSgtHMYLonQCrDupmj9d5RNY1sosXJWc8=\r\n");
		data.append("X-Log-Uid: " + uid + "\r\n");
		data.append("User-Agent: Che2-TL00_4.4.2_weibo_8.6.3_android\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}

}
