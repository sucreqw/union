package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

public class WeiboVote extends Weibo {

	@Override
	public int Actions(int index, String mission) {
		Nets nets = new Nets();
		String ret = "";
		String cookie = "";
		String vid = "";
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
				ret = nets.goPost("s.weibo.com", 443, Sserach(super.getCookie(), vid));
				// String p=MyUtil.midWord("msg\":\"", "\",\"", ret);
				// MyUtil.outPutData("log.txt", p);
				MyUtil.print(ret, Factor.getGui());
				break;

			}
		}
		return 0;
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
	private byte[] picktop(String cookie, String vid,String score) {
		StringBuilder data = new StringBuilder(900);
		String temp = "topic_name=%E5%90%B4%E7%A3%8A&page_id=1008086de98a1a1a398df9761c706bfaac6b00&score=5&is_pub=0&cur_rank=21&ctg_id=2&topic_score=1176938&index=select256&user_score=5";
		data.append("POST /aj/super/picktop HTTP/1.1\r\n");
		data.append("Host: huati.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: 163\r\n");
		data.append("Accept: application/json, text/plain, */*\r\n");
		data.append("Origin: https://huati.weibo.cn\r\n");
		data.append("X-Requested-With: XMLHttpRequest\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2)\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Referer: https://huati.weibo.cn/super/pick\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: " + cookie + "\r\n");
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}
}
