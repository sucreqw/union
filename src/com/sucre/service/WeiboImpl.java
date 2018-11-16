package com.sucre.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.sucre.dao.weiboDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.jdbc.JdbcConnector;
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
	public Weibo get(int index,Weibo weibo) {
		//String[] temp =list.get(index).split("[^@.-_\\w]");
		String[] temp =list.get(index).split("\\|");
		switch (temp.length) {
		case 2:
			return load(temp[0],temp[1],weibo);
			//break;
        
		default:
			return load(list.get(index),weibo);
			//break;
		}
		//return null;
	}

	@Override
	public Weibo load(String id, String pass ,Weibo weibo) {
		weibo.setId(id);
		weibo.setPass(pass);
		return weibo;
	}

	@Override
	public Weibo load (String inputData,Weibo weibo) {
		weibo.load(inputData);
        return weibo;
	}

	@Override
	public List<Weibo> getCounts(int counts,String mission) {
		
		
		String SQL="select * from weibo";//weibo limit 0,?" ;
		Weibo w=null;
		switch (mission) {
		case "login":
			//SQL+="weibo ";//where COOKIE1=null or COOKIE1=\"\" limit 0,?";
			//w=new weiboLogin();
			break;
		case "guess":
			
			break;
		}
		
		PreparedStatement pstmt;
		
		try {
			pstmt = (PreparedStatement) JdbcConnector.getConnection().prepareStatement(SQL);
			//pstmt.setInt(1, counts);
			
			ResultSet result=pstmt.executeQuery();
			int c=result.getFetchSize();
			while(result.next()) {
				getWeibo(mission, w);
				w.setNO(result.getInt("NO"));
				w.setId(result.getNString("ID"));
				w.setPass(result.getNString("PSW"));
				w.setUid(result.getNString("UID"));
				w.setName(result.getNString("NAME"));
				w.setCookie(result.getNString("COOKIE1"));
				w.setS(result.getNString("S_PARM"));
				w.setStatus(result.getInt("STATUS"));
				w.setLevel(result.getInt("LEVEL"));
				w.setEvents(result.getNString("EVENTS"));
				list.add(w);
			}
			
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	private void getWeibo(String mission ,Weibo w ) {
		switch (mission) {
		case "login":
			//SQL+="weibo ";//where COOKIE1=null or COOKIE1=\"\" limit 0,?";
			w=new weiboLogin();
			break;
		case "guess":
			
			break;
		}
		
	}
	@Override
	public int getsize() {
		return list.getSize();
	}
	@Override
	public void add(Weibo weibo) {
		list.add(weibo.toString());
	}
	@Override
	public void update(Weibo weibo) {
		
	}
	@Override
	public List<Weibo> getPage(int page) {
		return null;
	}

	/*public static weiboDao getInstance(){
		return dao;
	}*/
}