package com.sucre.service;

import java.util.List;

import com.sucre.dao.weiboDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

public class WeiboImpl implements weiboDao {
    
	public static weiboDao dao=new WeiboImpl();
	private MutiList list=new MutiList();
	private WeiboImpl() {
		
	}

	@Override
	public void loadList(String fileName) {
		try {
			// 加载文件
			list.loadFromFile(fileName);
			MyUtil.print("导入成功<==>" + String.valueOf(list.getSize()), Factor.getGui()); 
		} catch (Exception e) {
			MyUtil.print("导入错误：" + e.getMessage(), Factor.getGui());
		}
	}

	@Override
	public Weibo get(int index) {

		return null;
	}

	@Override
	public Weibo load(String id, String pass) {

		return null;
	}

	@Override
	public Weibo load(String inputData) {

		return null;
	}

	@Override
	public List<Weibo> getPage(int page) {
		
		return null;
	}
	
	public static weiboDao getInstance(){
		return dao;
	}
}
