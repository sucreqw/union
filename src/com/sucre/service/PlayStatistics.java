package com.sucre.service;

import com.sucre.myNet.Nets;
import com.sucre.utils.MyUtil;

public class PlayStatistics {
        
	public int play(String uid,String vid,String cookie){
		Nets net=new Nets();
		//String uid=MyUtil.midWord("uid\":\"", "\",\"", vid);
		String mid=MyUtil.midWord("mid\":\"", "\",\"", vid);
		String keys=MyUtil.midWord("keys\":\"", "\",\"", vid);
	    String ret=net.goPost("weibo.com", 443, playstatistics(cookie,uid,mid,keys));
		if(!MyUtil.isEmpty(ret)){
			if(ret.indexOf("success")!=-1){
				return 1;
			}
			
		}
	    
		return 0;
	}
	
	    
	    
	//电脑端播放
		private byte[] playstatistics(String cookie,String uid,String mid,String keys ) {
			
			/*
			 * {"uid":"5699122112","mid":"4312403095287213","keys":"4312347651901629","type":"feedvideo"}
			 * Dim D As String
			Dim Sig, Ks As String

			'Math.random().toString(36).substring(5, 10) + "_" + +(new Date) +"_"+ Math.floor(Math.random() * 1e4) + d)
			Ks = "H5_" & MakeNonce(5) & "_" & GetTimesB() & MakeNumber(4) & "1"

			Sig = md5("{""uid"":""" & U & """,""mid"":""" & M & """,""keys"":""" & K & """,""type"":""feedvideo""}" & Ks & "yixiong&zhaolong5")

			D = "data={""uid"":""" & U & """,""mid"":""" & M & """,""keys"":""" & K & """,""type"":""feedvideo""}&key=" & Ks & "&sig=" & Sig
*/          
			
			String vid="{\"uid\":\"" + uid + "\",\"mid\":\"" + mid + "\",\"keys\":\"" + keys + "\",\"type\":\"feedvideo\"}";
			String t=MyUtil.getTime() ;
			
			String key="H5_" + MyUtil.makeNonce(5) + "_" + t+ MyUtil.makeNumber(4) + "1";
			String sig=MyUtil.MD5(vid + key + "yixiong&zhaolong5");
			
			StringBuilder data = new StringBuilder();
			String temp ="data="+ vid +"&key="+ key +"&sig="+ sig +"&\r\n";
			
			data.append("POST https://weibo.com/aj/video/playstatistics?ajwvr=6 HTTP/1.1\r\n");
			data.append("Origin: https://weibo.com\r\n");
			data.append("Referer: https://weibo.com/2141853335/H5cEZmbrD?type=comment\r\n");
			data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134\r\n");
			data.append("Content-Type: application/x-www-form-urlencoded\r\n");
			data.append("X-Requested-With: XMLHttpRequest\r\n");
			data.append("Accept: */*\r\n");
			data.append("Accept-Language: zh-CN\r\n");
			data.append("Host: weibo.com\r\n");
			data.append("Content-Length: "+ temp.length()+"\r\n");
			data.append("Connection: Keep-Alive\r\n");
			data.append("Cache-Control: no-cache\r\n");
			data.append("Cookie: "+ cookie +"\r\n");
			data.append("\r\n");
			data.append(temp);
			data.append("\r\n");
			data.append("\r\n");
			
			return data.toString().getBytes();
		}
	
}
