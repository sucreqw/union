package com.sucre.entity;

import com.sucre.utils.MyUtil;

abstract public class Weibo {
	private String id;
	private String pass;
	private String cookie;
	private String uid;
	private String s;
	
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	
	public String toString() {
		return "Weibo [id=" + id + ", pass=" + pass + ", cookie=" + cookie + ", uid=" + uid + ", s=" + s + "]";
	}
	public Weibo() {
		super();
	}
	public Weibo(String id,String pass){
		this.id=id;
		this.pass=pass;
	}
	//从文件数据里加载数据！
	public Weibo(String inputdata){
		try{
		String[] temp = inputdata.split("\\|");
		this.cookie=temp[0];
		this.uid=temp[1];
		this.id = temp[3];
		this.pass = temp[4];
		this.s=temp[5];
		}catch (Exception e) {
			MyUtil.print("导入weibo数据出错!", null);
		}
	}
	
	
}
