package com.sucre.gui;

import javax.swing.*;
import java.awt.*;


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
		dtrpnUnion.setText("# union\r\n必需文件：\r\nadsl.properties\r\n文件格式：\r\nid=0279876148@ADSL\r\npass=258852\r\nname=\\u5bbd\\u5e26\\u8fde\\u63a5\r\n\r\njs.js//为新浪滑动验证的加密算法实现\r\n\r\njdbc.properties\r\n文件格式：\r\nid=root\r\npass=root\r\nsource=source//数据名\r\nip=127.0.0.1//数据库地址\r\n\r\n\r\n无论导入账号还是cookie,分隔符统一固定为|\r\n登录账号默认为三个一换ip。\r\nguess任务默认了50个换ip.\r\n\r\n任务说明\r\nguess:穷举s参数\r\ncheckin:超级话题签到，关注，必须导入相应的超话vid.】\r\n打榜：签到后送分，默认是直接送完账号里的分，vid是超话的vid.\r\n游客：生成游客cookie,格式按照以前yd的key,开始任务前随便导入cookie和vid\r\n阅读：直接导入yd的key.txt的格式的cookie,vid导入微博vid,换ip设置为0让它自动换。\r\n播放：vid要抓包，类似这样的：{\"uid\":\"5699122112\",\"mid\":\"4312403095287213\",\"keys\":\"4312347651901629\",\"type\":\"feedvideo\"}\n一整串放进去去就可以。\n\n\r\n注意事项：\r\n点击导入账号之前，请一定先选好具体任务。\r\n起始账号数从0开始计算 *\r\n换ip账号数量设置为0表示不换ip\r\n\r\n版本1.1\n\r\n修复了载入大文件卡死，设定了大于5万条数据不显示。\n内存小的机器 打开大文件时，用以下命令在命令行打开：\njava -Xms1024m\n\n -jar union1.0.jar \r\n\r\n版本1.2\n\r\n增加了播放量功能，用的是电脑端的加密接口。经测试游客账号无法返回成功。正常账号测试可以成功，高危账号也可以成功。每个账号限制大概20次。\n恢复了登录功能。\r\n\r\n版本1.3\r\n修复了登录功能，重新收集了24个js.js的trace参数。添加了更详细的登录提示信息。\r\n修复了多线程账号重复刷的问题，修复了登录不能设置起始位置的问题。\r\n\r\n版本1.4\r\n添加了取码多过自动换ip功能。\r\n\r\n版本1.5 \r\n新浪更改了js算法，1.5版本也对应更新了js.js，以及完善了打榜功能。\r\n修复了投票无法暂停的问题。\r\n\r\n版本1.6\r\n增加百度沸点投票功能，vid格式：3772\r\n可以抓包查找starid=3772，软件要求cookie列表不为空，可以随便导入。\r\n更新了投票成功判断，增加了设置软件投出数量 到达后自动停止功能！\n\n增加了剧赞投票，高危号是没办法投票，但是发现客户端一个号可以多投，而且不会发出微博。\nvid格式为微博的mid,cookie必须带有新的s参数。\n\r\n版本1.6.2\r\n增加了百度沸点投票的刷邀请人分数功能。\r\nvid格式更改为：vid|分享码   例如：3772|NctN1v-JZok7YXVf1VkR8g\r\n分享码为连接上的uk参数。\r\n\r\n版本1.6.3\r\n修复了百度沸点投票算法问题，增加了送分功能。\r\n增加了返回成功票数的显示，更改了设置票数停止为返回成功的数量。\r\n修复了guess穷举16次都失败会卡死的问题。\r\n修复了guess 无法定义开始位置和换ip数量问题。\r\n\r\n版本1.7\r\n增加微博之夜投票功能，vid格式：6whyPfAKPXOq1jsrF9mxTQ \r\n抓包查找：item_no=6whyPfAKPXOq1jsrF9mxTQ\r\n目前测试高危号可以投票，每天每号20票。\n\n版本1.7.1\n增加了微博之夜投票设置指定票数功能，增加了更详细的返回提示信息。\r\n\r\n版本1.7.1\r\n增加了秒拍微博之夜投票功能，秒拍领分功能，每天可领50分，秒拍查分功能，可查看账号还有多少分。\r\n查分跟领分可以随便导入vid\r\n投票功能vid格式跟之前版本一样,软件内置自动编码功能。\r\n投票功能每次固定只能投10分。\r\n\n\n\r\n版本1.7.2\n\r\n增加了v峰会投票，高危号可以投票，vid就是用户的uid.\n软件自动每个号投三票再进行下一个。\n秒拍领分，查分，投票功能 添加索引提醒信息。\r\n\r\n版本1.7.3\r\n增加了定时换ip功能，时间为秒数，在线程数量里设置好秒数，点开始就能自动循环换ip.\r\n增加综艺赞投票功能，vid格式为：vid=用户uid&name=用户在页面上显示的名字&\r\n注意后面&也不能少，cookie必须带s参数\r\n\r\n版本1.7.4\r\n清除了某些过期的投票。\r\n增加了通用的m.weibo.cn的投票。vid格式为：\r\nGET https://api.weibo.cn/2/page/button?request_url=XXXXXXXXX HTTP/1.1\r\n类似这样的整个报头。cookie必须包括s参数。\r\n"
				+"版本1.7.5\r\n"
				+"增加了读书投票功能,vid格式为：2781\r\n"
				+"需要抓包得到，可在网页端抓包。找到关键字：option_id=2781\r\n"
				+"增加了跑步投票功能，vid格式为：paotuan2018_14745\r\n"
				+"可以网页端抓包得到：http://sports.sina.cn/running/paotuan/2018/vip.d.html?tid=14745&from=groupmessage\r\n"
				+"版本1.7.6\r\n"
				+"增加了详细的登录提示。\r\n"
				+"增加了春节投票活动：https://m.weibo.cn/p/1059030002_7107_2\r\n"
				+"vid格式为：2906\r\n"
				+"每个账号能投8票，软件默认一次全投完，经测试高危号可以投，cookie不用带s参数。\r\n"
				+"版本1.7.7\r\n"
				+"开发工具正式全面转到idea,生成jar的大小是eclipse生成的三分之一。\r\n"
				+"清除了微博之夜活动的相关任务选项。增加了lpl投票，vid格式为：5436\r\n"
				+"高危号可以投，每个每天5票，cookie不需要s参数\r\n"
				+"版本1.8\r\n"
				+"利用秒拍漏洞新增秒拍关注与取消关注功能。cookie为原来刷秒拍红心的cookie,格式为：cookie|id\r\n"
				+"uid格式为：6llRdRoWfDXMHT4K 目前uid只能通过手机版抓包的形式取得。\r\n"
				+"版本1.8.1\r\n"
                +"增加了搜狗投票功能，vid格式：吴磊\r\n"
                +"内置了验证码为12，默认分享投5次，人气投5次。\r\n"



		);
		scrollPane.setViewportView(dtrpnUnion);
		
		this.setVisible(true);
		
	}


}