package com.sucre.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sucre.controller.Controller;
import com.sucre.factor.Factor;
import com.sucre.service.VidImpl;
import com.sucre.service.WeiboImpl;
import com.sucre.utils.JsUtil;
import com.sucre.utils.MyUtil;
import com.sucre.utils.Printer;
import com.sucre.utils.SinaCapchaUtil;

import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.ScrollPaneConstants;

public class Gui extends JFrame implements Printer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField filename;
	private JLabel lblUnion;
	private JButton loadId;
	private JButton LoadCookie;
	public static Gui frame = new Gui();
	private JTable table;
	private JButton loadvid;
	private JButton addvid;
	private JScrollPane scrollPane_1;
	private JTable vidtable;
	private JComboBox mission;
	private JButton begin;
    private JCheckBox IsCircle;
    
	VidImpl vidImpl;// =new VidImpl();
	WeiboImpl weiboCookie;// =new WeiboImpl();
	WeiboImpl weiboId;// =new WeiboImpl();
	private JTable cookietable;
	private JTextField thread;
	private JButton resume;
	private JTextField ipcount;
	private JTextField startpoint;
	private JButton setTime;
	private JTextField Times;
	private JEditorPane feedback;
	private JTextField counts;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// 加载窗体。
		frame.setVisible(true);
		// 导入ip文件。
		Controller.getInstance().load();
      
		/*byte[] t=SinaCapchaUtil.getPic("5_5_24_5_22_15_1_18_19_8_0_11_6_17_16_3_2_12_13_10_7_4_23_9_21_14_20", "R0lGODdhoACgAIQAAJSSlNTS1KyurOzq7KSipMTCxOTi5Pz6/JyanLS2tNza3PTy9KyqrJSWlNTW1LSytOzu7KSmpMTGxOTm5Pz+/JyenLy6vAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACwAAAAAoACgAAAF/iAljmRpnmiqqgpgrXAsr+0733iun22y/7secEgk1opIlivJbKaETic0SmUqGrYq8prVen/TLzAsLsuOZvAyzaYBfO0cOU6ncOu3Ox4/36P6fmVogUpdhGaAhyKJilQODQ+Nf1iSZgkACJUmjJpICw0ADQ6dI4OkURYAqhUHp5ynQKqgABKdE3aUsFGzoQ0Lmgx2b7pRsqoAkZIBAMKGxFvDsK/PctGua9RJetdw2dDdpNveRtjh1uND036m6GC53O3k4J3i8Trsmur2f+f05ZoWLASQ8s7cvEqrBlrpl69gvgL/yDmrpG/foQUJQDWwyFHFBAbMYCgYSbKkyZMo/kt2NKNwRQONMHvJfDkzJk1QKxVZSLCzJ8+fPoMCHbozp9GjSJMqXcq0qdOnUKNKnUq1qtWrWLNq3cq1q9evYMOKHUu2rNmzaNOqXcu27ZCecHnGDSi3Lt27Rd2mOMa3r9+/f/U+SUm4sErBaeohjoLPqeHHhKtUXHnTpuWamHFSadxUqGeioIVKZri4ieLSCw+i1hZx9TfXjB3CLjJ5tsjWttWozh1ENu8gpH9XmygcR+3iJU4jn3F8+SLczm/vjk7DN3U306/zI66dX/buya2D3xR8fHLo5kuVT79IvO2WzNfPbsDgVnzu7PPr38+/v///AAYo4IAEFmjggQgmsqjgggw26OCDEEYo4YQUOjXXhXZhiNd3bQHm4Yd9zQbZiCbZdgWHTmhkwS9kKVCAe3JAxksDCTxGzAAKUAFKBfARUVlmgF1GEzGpUJFAAj0S8ZlQGvUiwE9Q7sQTMQGgmM0xFYzy30sStAJgAixWKOaYZJZp5plopqnmmmy26eabcMb5FUYBHiCBZv4pUIEs2m244QMI9KUhXFTiJwmIfDWJaEiwFPnMYw5kdIwoNuqCYwgAOw==");
	
		try {
			OutputStream out=new FileOutputStream(new File("q.gif"));
			out.write(t);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		
	/*	try {
			InputStream inputStream=new FileInputStream(new File("t3142.gif"));
			InputStream inputStream1=new FileInputStream(new File("3124.gif"));
			try {
			byte[] buffer=new byte[inputStream.available()];
			byte[] buffer1= new byte[inputStream1.available()];
		
				int read=inputStream.read(buffer);
				
				int read1=inputStream1.read(buffer1);
				inputStream.close();
				inputStream1.close();
				System.out.println(SinaCapchaUtil.compareImage(buffer, buffer1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Base64.Encoder encoder=Base64 .getEncoder();
			
			String ret=encoder.encodeToString(buffer);
			
			
			//System.out.println(ret);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	/*	File file=new File("img/");
		//for (File temp : file.listFiles()) {
			String f=file.getName().replaceAll(".gif", "");
			System.out.print("maps.put(\"" +f +"\",\"");
			
			try {
				InputStream inputStream=new FileInputStream(new File("img/3241.gif"));
				//InputStream inputStream1=new FileInputStream(new File("3124.gif"));
				try {
				byte[] buffer=new byte[inputStream.available()];
				//byte[] buffer1= new byte[inputStream1.available()];
			
					int read=inputStream.read(buffer);
					
					//int read1=inputStream1.read(buffer1);
					inputStream.close();
					//inputStream1.close();
					//System.out.println(SinaCapchaUtil.compareImage(buffer, buffer1));
					Base64.Encoder encoder=Base64 .getEncoder();
					
					String ret=encoder.encodeToString(buffer);
					System.out.println(ret +"\");");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				//System.out.println(ret);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		//}
/*	JsUtil.loadJs("js.txt");
	JsUtil.runJS("path_enc","1234","e52d6b5fc16ffed41deaef7a302a225211a302a22521");
	JsUtil.runJS("data_enc",System.currentTimeMillis()-1000);*/
	//System.out.println(JsUtil.runJS("data_enc", 1423,System.currentTimeMillis()));
	}

	/**
	 * Create the frame.
	 */
	private Gui() {
		setTitle("main from");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1096, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		filename = new JTextField();
		filename.setBounds(254, 45, 117, 26);
		filename.setText("id.txt");
		panel.add(filename);
		filename.setColumns(10);

		lblUnion = new JLabel("");
		lblUnion.setBounds(6, 6, 364, 26);
		panel.add(lblUnion);

		JButton login = new JButton("登录");
		login.setBounds(663, 16, 103, 26);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().login(Integer.parseInt(thread.getText()), false,Integer.parseInt(startpoint.getText()));
			}
		});
		panel.add(login);

		loadId = new JButton("导入");
		loadId.setBounds(6, 43, 117, 29);
		loadId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// weiboId =
				// Controller.getInstance().loadWeibo(filename.getText());
				// Controller.getInstance().loadTable(table, (MutiList)
				// weiboId.getlist());
				String m=(String)mission.getSelectedItem();
				Controller.getInstance().loadWeiboId(filename.getText(), table,m);
			}
		});
		panel.add(loadId);

		LoadCookie = new JButton("导入cookie");
		LoadCookie.setBounds(6, 74, 117, 29);
		LoadCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// weiboCookie =
				// Controller.getInstance().loadWeibo(filename.getText());
				// Controller.getInstance().loadTable(cookietable, (MutiList)
				// weiboCookie.getlist());
				Controller.getInstance().loadWeiboCookie(filename.getText(), cookietable);
			}
		});
		panel.add(LoadCookie);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 131, 364, 250);
		scrollPane.setToolTipText("");
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		loadvid = new JButton("导入vid");
		loadvid.setBounds(133, 45, 92, 26);
		loadvid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// vidImpl =
				// Controller.getInstance().loadVid(filename.getText());

				// Controller.getInstance().loadTable(vidtable, (MutiList)
				// vidImpl.getList());
				Controller.getInstance().loadVid(filename.getText(), vidtable);
			}
		});
		panel.add(loadvid);

		addvid = new JButton("加入vid");
		addvid.setBounds(133, 77, 93, 23);
		addvid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// vidImpl.add(filename.getText());
				Controller.getInstance().addVid(filename.getText(), vidtable);
				// Controller.getInstance().loadTable(vidtable, (MutiList)
				// vidImpl.getList());
			}
		});
		panel.add(addvid);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(378, 131, 122, 250);
		panel.add(scrollPane_1);

		vidtable = new JTable();
		scrollPane_1.setViewportView(vidtable);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(507, 131, 296, 248);
		panel.add(scrollPane_2);

		cookietable = new JTable();
		scrollPane_2.setViewportView(cookietable);

		thread = new JTextField();
		thread.setBounds(560, 16, 103, 26);
		thread.setText("1");
		panel.add(thread);
		thread.setColumns(10);

		begin = new JButton("开始");
		begin.setBounds(663, 45, 103, 29);
		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String m=(String)mission.getSelectedItem();
				switch (m) {
				case "guess":
					print("guess!!!");
					Controller.getInstance().guess(Integer.parseInt(startpoint.getText()),Integer.parseInt(thread.getText()), false);
					break;
				case "checkin":
					//Controller.getInstance().getPic(Integer.parseInt(thread.getText()), false);
					Controller.getInstance().doMission(m, 0, Integer.parseInt(thread.getText()), false);
					print("checkIn!!!");
					break;
				case "加油":
				    Controller.getInstance().vote(Integer.parseInt(startpoint.getText()), Integer.parseInt( thread.getText()), IsCircle.isSelected(), m);
					break;
				case "搜索s.com":
					 Controller.getInstance().vote(Integer.parseInt(startpoint.getText()), Integer.parseInt( thread.getText()), IsCircle.isSelected(), m);
						
					break;
                default:
                	Controller.getInstance().vote(Integer.parseInt(startpoint.getText()), Integer.parseInt( thread.getText()), IsCircle.isSelected(), m);
                	break;
				}

			}
		});
		panel.add(begin);

		mission = new JComboBox();
		mission.setBounds(560, 46, 103, 27);
		mission.setModel(new DefaultComboBoxModel(new String[] {"login", "guess", "checkin", "打榜","查分", "加油", "搜索s.com","游客","阅读","播放","沸点","剧赞","微博之夜","秒拍领分","秒拍查分","秒拍送分","v峰会"}));
		panel.add(mission);

		resume = new JButton("暂停");
		resume.setBounds(663, 74, 103, 29);
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("暂停".equals(resume.getText())) {
					resume.setText("继续");
					Controller.getInstance().stop();
				} else {
					Controller.getInstance().resume();
					resume.setText("暂停");

				}
			}
		});
		panel.add(resume);
		
		ipcount = new JTextField();
		ipcount.setBounds(484, 19, 66, 21);
		ipcount.setText("0");
		panel.add(ipcount);
		ipcount.setColumns(10);
		
		JButton 关于 = new JButton("关于");
		关于.setBounds(6, 391, 119, 23);
		关于.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About about=new About();
				
			}
		});
		panel.add(关于);
		
		IsCircle = new JCheckBox("循环");
		IsCircle.setBounds(560, 77, 103, 23);
		panel.add(IsCircle);
		
		JLabel lblNewLabel = new JLabel("多少账号更换ip:");
		lblNewLabel.setBounds(378, 16, 101, 26);
		panel.add(lblNewLabel);
		
		startpoint = new JTextField();
		startpoint.setBounds(484, 47, 66, 21);
		startpoint.setText("0");
		panel.add(startpoint);
		startpoint.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("开始位置：");
		lblNewLabel_1.setBounds(381, 50, 75, 15);
		panel.add(lblNewLabel_1);
		
		setTime = new JButton("定时");
		setTime.setBounds(133, 391, 93, 23);
		setTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				MyUtil.print("开始计时！", Factor.getGui());
				//lblUnion.setText(t);
				Thread thread=new Thread() {
					SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
					String t="";
					 public void run() {
						 while(true) {
						 t=sdf.format(new Date());
						 //MyUtil.print(t, Factor.getGui());
						 
						 if(t.equals(Factor.getGuiFrame().Times.getText())) {
							 Factor.getGuiFrame().begin.doClick();
						 }
						 MyUtil.sleeps(1000);
						 }
					 }
				};
				thread.start();
			}
		});
		panel.add(setTime);
		
		Times = new JTextField();
		Times.setBounds(236, 392, 66, 21);
		Times.setText("10:00:00");
		panel.add(Times);
		Times.setColumns(10);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_3.setBounds(807, 6, 253, 375);
		panel.add(scrollPane_3);
		//scroll.setViewportView(feedBack);
		feedback = new JEditorPane();
		scrollPane_3.setViewportView(feedback);
		
		counts = new JTextField();
		counts.setText("0");
		counts.setBounds(484, 78, 66, 21);
		panel.add(counts);
		counts.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("结束票数：");
		lblNewLabel_2.setBounds(378, 81, 78, 15);
		panel.add(lblNewLabel_2);
       
	}

	/**
	 * 实现打印操作。
	 */
	@Override
	public void print(String data) {
		lblUnion.setText(data);
		feedback.setText(feedback.getText()+data+"\r\n");
		
	}

	/**
	 * 刷新列表数据。
	 */
	public void refresh() {
		Controller.getInstance().refresh(cookietable);
	}
	/**
	 * 取换ip的数量。
	 * @return
	 */
    public int getIPcount() {
    	return Integer.parseInt(ipcount.getText());
    }
	/**
	 * 取窗体实例
	 * 
	 * @return
	 */
	public static Gui getInstance() {
		return frame;
	}
	
	public int getCounts() {
		return Integer.parseInt(counts.getText());
	}
}
