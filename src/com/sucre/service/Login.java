package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.dao.weiboDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myThread.Thread4Net;

public class Login extends Thread4Net{
	private weiboDao weibo;
	private weiboLogin w;
	public Login(int u, boolean isCircle ,weiboDao weibo) {
		super(u, isCircle);
		this.weibo=weibo;
	}

	@Override
	public int doWork(int index) {
		w=(weiboLogin)weibo.get(index);
		int ret=w.Actions(index);
		//登录成功
		if(ret==1){ 
			Controller.getInstance().addCookie(w);
			Controller.getInstance().refresh();
		}
		
		return 0;
	}
	

}
