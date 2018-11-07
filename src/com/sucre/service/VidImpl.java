package com.sucre.service;

import java.util.List;

import com.sucre.dao.vidDao;
import com.sucre.entity.Vid;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

public class VidImpl implements vidDao{
	MutiList list=new MutiList();
	@Override
	public Vid getvid(int index) {
		Vid vid=new Vid(list.get(index));
		return vid;
	}

	@Override
	public void add(String vid) {
		list.add(vid);
	}
	@Override
	public void remove(int index) {
		list.remove(index);
	}
	public void loadVid(String fileName) {
		try {
			// 加载文件
			list.loadFromFile(fileName);
			MyUtil.print("导入成功<==>" + String.valueOf(list.getSize()), Factor.getGui()); 
		} catch (Exception e) {
			MyUtil.print("导入错误：" + e.getMessage(), Factor.getGui());
		}
		
	}
	public List getlist() {
		return list;
	}

}
