package com.sucre.controller;


import com.sucre.factor.Factor;
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
	 * 加载指定文件。
	 * @param fileName
	 */
	public void loadList(String fileName){
		Factor.getDao().loadList(fileName);
	}
	/**
	 * 拿到controller的对象实例。
	 * @return
	 */
	public static Controller getInstance(){
		return controller;
	}
}
