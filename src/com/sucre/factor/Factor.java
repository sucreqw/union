package com.sucre.factor;

import com.sucre.dao.weiboDao;
import com.sucre.gui.Gui;
import com.sucre.service.WeiboImpl;
import com.sucre.utils.Printer;

public class Factor {
	
	public static Printer getGui() {
		return Gui.getInstance();
	}
	public static weiboDao getDao(){
		return WeiboImpl.getInstance();
	}
}
