package com.sucre.controller;


import javax.swing.JTable;

import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.service.VidImpl;
import com.sucre.service.WeiboImpl;
import com.sucre.utils.GuiUtil;
import com.sucre.utils.Info;
import com.sucre.utils.MyUtil;

import com.sucre.utils.accounts;

public class Controller {
	
	
	public static Controller controller=new Controller();
	
	private Controller() {
	}
	
	/**
	 * 导入ip文件配置。
	 */
	public void load(){
		try {
			// 导入换ip配置
			Info info = accounts.getInstance();
			MyUtil.loadADSL("adsl.properties", accounts.getInstance());
			MyUtil.print(info.getADSL() + "<>" + info.getADSLname() + "<>" + info.getADSLpass(),Factor.getGui());
		
		} catch (Exception e) {
			MyUtil.print("导入文件出错：" + e.getMessage(), Factor.getGui());
		}
	}
	
	/**
	 * 加载指定文件到账号列表
	 * @param fileName
	 */
     public WeiboImpl loadWeibo(String fileName) {
    	 WeiboImpl weiboImpl=new WeiboImpl();
    	 weiboImpl.loadList(fileName);
    	 return weiboImpl;
     }
     
     /**
      * 加载指定文件到vid列表
      * @param fileName
      * @return
      */
     public VidImpl loadVid(String fileName) {
    	 VidImpl vidImpl=new VidImpl();
    	 vidImpl.loadVid(fileName);
    	 return vidImpl;
     }
     
     public void addVid(VidImpl v,String data) {
    	 v.add(data);
     }
	/**
	 * 加载list到列表
	 * @param table
	 * @param list
	 */
	public void loadTable(JTable table, MutiList list) {
		GuiUtil.loadTable(table, list);
	}
	
	/**
	 * 拿到controller的对象实例。
	 * @return
	 */
	public static Controller getInstance(){
		return controller;
	}
}
