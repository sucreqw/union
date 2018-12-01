package com.sucre.service;

public class PlayStatistics {
        
	
	
	// 加油卡，点亮
		private byte[] incrspt(String cookie, String vid,String uid ) {
			
			/*Dim D As String
			Dim Sig, Ks As String

			'Math.random().toString(36).substring(5, 10) + "_" + +(new Date) +"_"+ Math.floor(Math.random() * 1e4) + d)
			Ks = "H5_" & MakeNonce(5) & "_" & GetTimesB() & MakeNumber(4) & "1"

			Sig = md5("{""uid"":""" & U & """,""mid"":""" & M & """,""keys"":""" & K & """,""type"":""feedvideo""}" & Ks & "yixiong&zhaolong5")

			D = "data={""uid"":""" & U & """,""mid"":""" & M & """,""keys"":""" & K & """,""type"":""feedvideo""}&key=" & Ks & "&sig=" & Sig
*/
			StringBuilder data = new StringBuilder(900);
			String temp ="data="+ vid +"&key=H5_o814g_154364091935018541&sig=72e17bd5371a75acd501d5457182dd6d\r\n";
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
			data.append("Cookie: "+cookie +"\r\n");
			data.append("\r\n");
			data.append(temp);
			data.append("\r\n");
			data.append("\r\n");
			return data.toString().getBytes();
		}
	
}
