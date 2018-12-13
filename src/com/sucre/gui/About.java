package com.sucre.gui;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Window.Type;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class About extends JFrame{
	public About() {
		setTitle("About");
		setType(Type.POPUP);
		
		setAlwaysOnTop(true);
		setBounds(100, 100, 350, 500);
		setBounds(100, 100, 560, 395);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		//JTextArea txtrUnion = new JTextArea();
		//txtrUnion.setText("# union\r\n必需文件：\r\nadsl.properties\r\n文件格式：\r\nid=0279876148@ADSL\r\npass=258852\r\nname=\\u5bbd\\u5e26\\u8fde\\u63a5\r\n\r\njs.js//为新浪滑动验证的加密算法实现\r\n\r\njdbc.properties\r\n文件格式：\r\nid=root\r\npass=root\r\nsource=source//数据名\r\nip=127.0.0.1//数据库地址\r\n\r\n\r\n无论导入账号还是cookie,分隔符统一固定为|\r\n登录账号默认为三个一换ip。\r\nguess任务默认了50个换ip.\r\n\r\n任务说明\r\nguess:穷举s参数\r\ncheckin:超级话题签到，关注，必须导入相应的超话vid.】\r\n打榜：签到后送分，默认是直接送完账号里的分，vid是超话的vid.\r\n\r\n注意事项：\r\n点击导入账号之前，请一定先选好具体任务。\r\n起始账号数从0开始计算\r\n换ip账号数量设置为0表示不换ip,");
		//getContentPane().add(txtrUnion, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 523, 339);
		panel.add(scrollPane);
		
		JEditorPane dtrpnUnion = new JEditorPane();
		dtrpnUnion.setText("# union\r\n必需文件：\r\nadsl.properties\r\n文件格式：\r\nid=0279876148@ADSL\r\npass=258852\r\nname=\\u5bbd\\u5e26\\u8fde\\u63a5\r\n\r\njs.js//为新浪滑动验证的加密算法实现\r\n\r\njdbc.properties\r\n文件格式：\r\nid=root\r\npass=root\r\nsource=source//数据名\r\nip=127.0.0.1//数据库地址\r\n\r\n\r\n无论导入账号还是cookie,分隔符统一固定为|\r\n登录账号默认为三个一换ip。\r\nguess任务默认了50个换ip.\r\n\r\n任务说明\r\nguess:穷举s参数\r\ncheckin:超级话题签到，关注，必须导入相应的超话vid.】\r\n打榜：签到后送分，默认是直接送完账号里的分，vid是超话的vid.\r\n游客：生成游客cookie,格式按照以前yd的key,开始任务前随便导入cookie和vid\r\n阅读：直接导入yd的key.txt的格式的cookie,vid导入微博vid,换ip设置为0让它自动换。\r\n播放：vid要抓包，类似这样的：{\"uid\":\"5699122112\",\"mid\":\"4312403095287213\",\"keys\":\"4312347651901629\",\"type\":\"feedvideo\"}\n一整串放进去去就可以。\n\n\r\n注意事项：\r\n点击导入账号之前，请一定先选好具体任务。\r\n起始账号数从0开始计算 *\r\n换ip账号数量设置为0表示不换ip\r\n\r\n版本1.1\n\r\n修复了载入大文件卡死，设定了大于5万条数据不显示。\n内存小的机器 打开大文件时，用以下命令在命令行打开：\njava -Xms1024m\n\n -jar union1.0.jar \r\n\r\n版本1.2\n\r\n增加了播放量功能，用的是电脑端的加密接口。经测试游客账号无法返回成功。正常账号测试可以成功，高危账号也可以成功。每个账号限制大概20次。\n恢复了登录功能。\r\n\r\n版本1.3\r\n修复了登录功能，重新收集了24个js.js的trace参数。添加了更详细的登录提示信息。\r\n修复了多线程账号重复刷的问题，修复了登录不能设置起始位置的问题。\r\n\r\n版本1.4\r\n添加了取码多过自动换ip功能。\r\n\r\n版本1.5 \r\n新浪更改了js算法，1.5版本也对应更新了js.js，以及完善了打榜功能。\r\n修复了投票无法暂停的问题。\r\n\r\n版本1.6\r\n增加百度沸点投票功能，vid格式：3772\r\n可以抓包查找starid=3772，软件要求cookie列表不为空，可以随便导入。\r\n更新了投票成功判断，增加了设置软件投出数量 到达后自动停止功能！\n\n增加了剧赞投票，高危号是没办法投票，但是发现客户端一个号可以多投，而且不会发出微博。\nvid格式为微博的mid,cookie必须带有新的s参数。\n\r\n版本1.6.2\r\n增加了百度沸点投票的刷邀请人分数功能。\r\nvid格式更改为：vid|分享码   例如：3772|NctN1v-JZok7YXVf1VkR8g\r\n分享码为连接上的uk参数。\r\n\r\n版本1.6.3\r\n修复了百度沸点投票算法问题，增加了送分功能。\r\n增加了返回成功票数的显示，更改了设置票数停止为返回成功的数量。\r\n修复了guess穷举16次都失败会卡死的问题。\r\n修复了guess 无法定义开始位置和换ip数量问题。\r\n\r\n版本1.7\r\n增加微博之夜投票功能，vid格式：6whyPfAKPXOq1jsrF9mxTQ \r\n抓包查找：item_no=6whyPfAKPXOq1jsrF9mxTQ\r\n目前测试高危号可以投票，每天每号20票。\n\n版本1.7.1\n增加了微博之夜投票设置指定票数功能，增加了更详细的返回提示信息。\r\n\r\n版本1.7.1\r\n增加了秒拍微博之夜投票功能，秒拍领分功能，每天可领50分，秒拍查分功能，可查看账号还有多少分。\r\n查分跟领分可以随便导入vid\r\n投票功能vid格式跟之前版本一样,软件内置自动编码功能。\r\n投票功能每次固定只能投10分。\n\n版本1.7.2\n增加了v峰会投票，高危号可以投票，vid就是用户的uid.\n软件自动每个号投三票再进行下一个。");
		scrollPane.setViewportView(dtrpnUnion);
		
		this.setVisible(true);
		
	}
	

}