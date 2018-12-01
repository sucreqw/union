package com.sucre.service;

public class PlayStatistics {
        
	
	
	// 加油卡，点亮
		private byte[] incrspt(String cookie, String vid) {
			String[] tempCookie = cookie.split("\\^");
			cookie = tempCookie.length > 2 ? tempCookie[2] : "";
			StringBuilder data = new StringBuilder(900);
			String temp ="{\"uid\":\"5699122112\",\"mid\":\"4312403095287213\",\"keys\":\"4312347651901629\",\"type\":\"feedvideo\"}&key=H5_o814g_154364091935018541&sig=72e17bd5371a75acd501d5457182dd6d\r\n";
			data.append("POST https://weibo.com/aj/video/playstatistics?ajwvr=6 HTTP/1.1\r\n");
			data.append("Origin: https://weibo.com\r\n");
			data.append("Referer: https://weibo.com/2141853335/H5cEZmbrD?type=comment\r\n");
			data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134\r\n");
			data.append("Content-Type: application/x-www-form-urlencoded\r\n");
			data.append("X-Requested-With: XMLHttpRequest\r\n");
			data.append("Accept: */*\r\n");
			data.append("Accept-Language: zh-CN\r\n");
			data.append("Accept-Encoding: gzip, deflate, br\r\n");
			data.append("Host: weibo.com\r\n");
			data.append("Content-Length: 164\r\n");
			data.append("Connection: Keep-Alive\r\n");
			data.append("Cache-Control: no-cache\r\n");
			data.append("Cookie: Ugrow-G0=56862bac2f6bf97368b95873bc687eef; YF-V5-G0=a5a6106293f9aeef5e34a2e71f04fae4; YF-Page-G0=324e50a7d7f9947b6aaff9cb1680413f; _s_tentry=-; Apache=7395404894553.641.1543640847298; wb_view_log_5699122112=1366*7681; wvr=6; SINAGLOBAL=7879238193834.23.1539498546932; ALF=1545640383; ULV=1543640847589:4:1:2:7395404894553.641.1543640847298:1543475796139; wb_timefeed_5699122112=1; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWqHPXnXZMHV1UPxkRLhDBT5JpX5oz75NHD95QfSo.4eKzEeK2EWs4DqcjMi--NiK.Xi-2Ri--ciKnRi-zNSKq41K2Eeo2pentt; SUB=_2A252_XzvDeRhGeNI4lsQ8izNyj6IHXVSHgSnrDV8PUJbkNBeLVn6kW1NS-M3L5SYzTJeNAdJrGGa-fZbgcai6atL\r\n");
			data.append("\r\n");
			data.append(temp);
			data.append("\r\n");
			data.append("\r\n");
			return data.toString().getBytes();
		}
	
}
