package com.sucre.service;

import com.sucre.dao.weiboDao;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

/**
 * 新浪滑动验证类
 * @author 90650
 *
 */
public class Capcha extends Thread4Net {
	private weiboDao weibo;
	private WeiboCapcha w;
	private String result="";
	public Capcha(int u, boolean isCircle,weiboDao weibo) {
		super(u, isCircle);
		this.weibo=weibo;
	}

	@Override
	public int doWork(int index) {
		w=new WeiboCapcha();
		w=(WeiboCapcha)weibo.get(index,w);
		
			int ret=w.Actions(index,"getpic");	
			result=w.getVid();
			MyUtil.print(String.valueOf(index), Factor.getGui());
		
		
		return 0;
	}
    
	public String Rev() {
		return this.result;
	}
}
