package com.sucre.dao;

import java.util.List;

import com.sucre.entity.Weibo;
/**
 * 微博账号数据dao层接口
 * @author 90650
 *
 */
public interface weiboDao {
	//从文件加载数据
	public void loadList(String fileName);
	//根据索引取一条数据
	public Weibo get(int index,Weibo weibo);
	//根据id,pass加载一个类
	public Weibo load(String id,String pass,Weibo weibo);
	//把文件的数据加载到类
	public Weibo load(String inputData,Weibo weibo);
	//取一页的数据
	public List<Weibo> getPage(int page);
	//取指定数量数据
	public List<Weibo> getCounts(int counts,String mission);
	//取数据总数
	public int getsize();
	//添加一条数据
	public void add(Weibo weibo);
	//更新一条数据
	public void update(Weibo weibo);
	
}