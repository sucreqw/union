package com.sucre.controller;

import javax.swing.JTable;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.service.Guess;
import com.sucre.service.Login;
import com.sucre.service.VidImpl;
import com.sucre.service.WeiboImpl;
import com.sucre.utils.GuiUtil;
import com.sucre.utils.Info;
import com.sucre.utils.MyUtil;
import com.sucre.utils.accounts;

public class Controller {

	public static Controller controller = new Controller();
	private WeiboImpl weiboImplId;
	private WeiboImpl weiboImplCookie;
	private VidImpl vidImpl;
    private boolean stop=false;
	private Controller() {
	}

	/**
	 * 导入ip文件配置。
	 */
	public void load() {
		try {
			// 导入换ip配置
			Info info = accounts.getInstance();
			MyUtil.loadADSL("adsl.properties", accounts.getInstance());
			MyUtil.print(info.getADSL() + "<>" + info.getADSLname() + "<>" + info.getADSLpass(), Factor.getGui());

		} catch (Exception e) {
			MyUtil.print("导入文件出错：" + e.getMessage(), Factor.getGui());
		}
	}

	/**
	 * 加载指定文件到账号列表
	 * 
	 * @param fileName
	 */
	public void loadWeiboId(String fileName, JTable table) {
		weiboImplId = new WeiboImpl();
		weiboImplId.loadList(fileName);
		loadTable(table, (MutiList) weiboImplId.getlist());
	}

	/**
	 * 加载指定文件到cookie列表
	 * 
	 * @param fileName
	 */
	public void loadWeiboCookie(String fileName, JTable table) {
		weiboImplCookie = new WeiboImpl();
		weiboImplCookie.loadList(fileName);
		loadTable(table, (MutiList) weiboImplCookie.getlist());
	}

	/**
	 * 加载指定文件到vid列表
	 * 
	 * @param fileName
	 * @return
	 */
	public void loadVid(String fileName, JTable table) {
		vidImpl = new VidImpl();
		vidImpl.loadVid(fileName);
		loadTable(table, (MutiList) vidImpl.getlist());

	}

	public void addVid(String data,JTable table) {
		vidImpl.add(data);
		loadTable(table, (MutiList) vidImpl.getlist());
	}

	/**
	 * 加载list到列表
	 * 
	 * @param table
	 * @param list
	 */
	public void loadTable(JTable table, MutiList list) {
		GuiUtil.loadTable(table, list);
	}
    
	public void addCookie(Weibo weibo){
		if(weiboImplCookie==null){weiboImplCookie=new WeiboImpl();}
		weiboImplCookie.add(weibo);
	}
	/**
	 * 调用批量登录功能
	 * 
	 * @param thread
	 *            线程数量
	 * @param limit
	 *            账号总数
	 * @param isCircle
	 *            是否循环
	 */
	public void login(int thread,  boolean isCircle) {
		int limit =weiboImplId==null? 0: weiboImplId.getsize();
		if(limit==0){MyUtil.print("ID未导入！", Factor.getGui());}
		for (int i = 1; i <= thread; i++) {
			Login login = new Login(limit, isCircle,weiboImplId);
			Thread t = new Thread(login);
			if(i==1) {t.setName("ip");}
			t.start();
		}
	}
   public void guess(int thread,  boolean isCircle){
	    int limit =weiboImplCookie==null? 0: weiboImplCookie.getsize();
	    if(limit==0){MyUtil.print("Cookie未导入！", Factor.getGui());}
		for (int i = 1; i <= thread; i++) {
			Guess guess = new Guess(limit, isCircle,weiboImplCookie);
			Thread t = new Thread(guess);
			if(i==1) {t.setName("ip");}
			t.start();
		}
   }
	/**
	 * 拿到controller的对象实例。
	 * 
	 * @return
	 */
	public static Controller getInstance() {
		return controller;
	}
	/**
	 * 刷新指定数据列表
	 * @param cookietable
	 */
	public void refresh(JTable table) {
		if(weiboImplCookie==null){return ;}
		loadTable(table, (MutiList) weiboImplCookie.getlist());
	}
	/**
	 * 刷新指定数据列表(线程回调)
	 * @param cookietable
	 */
	public void refresh() {
		Factor.getGuiFrame().refresh();
	}
	/**
	 * 继续任务
	 */
	public void resume(){
		this.stop=false;
	}
	/**
	 * 暂停任务
	 */
	public void stop(){
		this.stop=true;
	}
	/**
	 * 判断是否为暂停状态
	 * @return 当前状态。
	 */
	public boolean isStop(){
		return this.stop;
	}
}
