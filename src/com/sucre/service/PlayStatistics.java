package com.sucre.service;

public class PlayStatistics {
        
	
	
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
	
}
