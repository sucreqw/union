package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.dao.weiboDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

public class Login extends Thread4Net {
	private weiboDao weibo;
	private weiboLogin w;
	private WeiboCapcha c;

	public Login(int l, int u, boolean isCircle, weiboDao weibo) {
		super(l, u, isCircle);
		this.weibo = weibo;
	}

	@Override
	public int doWork(int index) {

		w = new weiboLogin();
		w = (weiboLogin) weibo.get(index, w);
		int counts = 0;
		int ret = 0;
		while (true) {
			ret = w.Actions(index, "");
			// 登录成功
			if (ret == 1) {
				Controller.getInstance().addCookie(w);
				Controller.getInstance().refresh();
				break;
			} else if (ret == -1) {

				c = new WeiboCapcha();
				c = (WeiboCapcha) weibo.get(index, c);
				c.Actions(index, "getpic");
				w.setVid(c.getVid());
			} else if (ret == 2) {
				// 要换ip.
				counts=0;
				if ( "ip".equals(Thread.currentThread().getName())) {
					MyUtil.changIp();
				}
			} else {
				break;
			}
			counts++;
			if (counts == 5) {
				break;
			}

		}
		if (Controller.getInstance().changeIPcount() != 0) {
			if ((index + 1) % Controller.getInstance().changeIPcount() == 0
					&& "ip".equals(Thread.currentThread().getName())) {
				MyUtil.changIp();
			}
		}
		return 0;
	}

}
