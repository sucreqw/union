package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.dao.weiboDao;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

public class Guess extends Thread4Net{
	private weiboDao weibo;
	private weiboGuess w;
	public Guess(int u, boolean isCircle,weiboDao weibo) {
		super(u, isCircle);
		this.weibo=weibo;
	}

	@Override
	public int doWork(int index) {
		w=(weiboGuess) weibo.get(index);
		int ret=w.Actions(index);
		while(Controller.getInstance().isStop()){
			MyUtil.sleeps(1000);
		}
		return 0;
	}
	

}