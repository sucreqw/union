package com.sucre.service;

import java.util.List;

import com.sucre.dao.weiboDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

/**
 * service层，微博dao层的实现类。
 * @author 90650
 *
 */
public class WeiboImpl implements weiboDao {
    
	//public static weiboDao dao=new WeiboImpl();
	private MutiList list=new MutiList();
	
	/*private WeiboImpl() {
		
	}
*/
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
	public List getlist() {
		return this.list;
	}
	@Override
	public Weibo get(int index) {
		//String[] temp =list.get(index).split("[^@.-_\\w]");
		String[] temp =list.get(index).split("\\|");
		switch (temp.length) {
		case 2:
			return load(temp[0],temp[1]);
			//break;
        
		default:
			return load(list.get(index));
			//break;
		}
		//return null;
	}

	@Override
	public weiboLogin load(String id, String pass) {
		return new weiboLogin(id,pass){};
	}

	@Override
	public weiboLogin load (String inputData) {
        return new weiboLogin(inputData){};
	}

	@Override
	public List<Weibo> getPage(int page) {
		
		return null;
	}
	@Override
	public int getsize() {
		return list.getSize();
	}
	@Override
	public void add(Weibo weibo) {
		list.add(weibo.toString());
	}
	
	/*public static weiboDao getInstance(){
		return dao;
	}*/
}
