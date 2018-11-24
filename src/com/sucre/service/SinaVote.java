package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.dao.weiboDao;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

public class SinaVote extends Thread4Net {
	private weiboDao weibo;
	private WeiboVote w;
	private String mission;

	public SinaVote(int l, int u, boolean isCircle, weiboDao weibo, String mission) {
		super(l, u, isCircle);
		this.weibo = weibo;
		this.mission = mission;
	}

	@Override
	public int doWork(int index) {
		w = new WeiboVote();
		w = (WeiboVote) weibo.get(index, w);

		int ret = w.Actions(index, mission);

		switch (ret) {
		// 要拖码
		case 382023:
			WeiboCapcha c = new WeiboCapcha();
			c = (WeiboCapcha) weibo.get(index, c);
			c.setUid(w.getUid());
			c.setVoteId(w.getVid());
            
			while((ret=c.Actions(index, "getpicD"))!=1) {
				
			}
			w.setPid(c.getVid());
            ret = w.Actions(index, mission);
            
		}

		if (Controller.getInstance().changeIPcount() != 0) {
			if ((index + 1) % Controller.getInstance().changeIPcount() == 0
					&& "ip".equals(Thread.currentThread().getName())) {
				MyUtil.changIp();
			}
		}

		while (Controller.getInstance().isStop()) {
			MyUtil.sleeps(1000);
		}

		return 0;
	}

}
