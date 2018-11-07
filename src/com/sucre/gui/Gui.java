package com.sucre.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sucre.controller.Controller;

import com.sucre.service.VidImpl;
import com.sucre.service.WeiboImpl;

import com.sucre.utils.Printer;

import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Gui extends JFrame implements Printer {

	private JPanel contentPane;
	private JTextField filename;
	private JLabel Label;
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

	VidImpl vidImpl;// =new VidImpl();
	WeiboImpl weiboCookie;// =new WeiboImpl();
	WeiboImpl weiboId;// =new WeiboImpl();
	private JTable cookietable;
	private JTextField thread;
	private JButton resume;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// 加载窗体。
		frame.setVisible(true);
		// 导入ip文件。
		Controller.getInstance().load();
	}

	/**
	 * Create the frame.
	 */
	private Gui() {
		setTitle("main from");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 819, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		filename = new JTextField();
		filename.setText("id.txt");
		filename.setBounds(254, 45, 117, 26);
		panel.add(filename);
		filename.setColumns(10);

		Label = new JLabel("");
		Label.setBounds(6, 6, 645, 26);
		panel.add(Label);

		JButton login = new JButton("登录");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller.getInstance().login(Integer.parseInt(thread.getText()), false);
			}
		});
		login.setBounds(663, 16, 103, 26);
		panel.add(login);

		loadId = new JButton("导入id");
		loadId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// weiboId =
				// Controller.getInstance().loadWeibo(filename.getText());
				// Controller.getInstance().loadTable(table, (MutiList)
				// weiboId.getlist());
				Controller.getInstance().loadWeiboId(filename.getText(), table);
			}
		});
		loadId.setBounds(6, 43, 117, 29);
		panel.add(loadId);

		LoadCookie = new JButton("导入cookie");
		LoadCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// weiboCookie =
				// Controller.getInstance().loadWeibo(filename.getText());
				// Controller.getInstance().loadTable(cookietable, (MutiList)
				// weiboCookie.getlist());
				Controller.getInstance().loadWeiboCookie(filename.getText(), cookietable);
			}
		});
		LoadCookie.setBounds(6, 74, 117, 29);
		panel.add(LoadCookie);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 131, 364, 250);
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		loadvid = new JButton("导入vid");
		loadvid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// vidImpl =
				// Controller.getInstance().loadVid(filename.getText());

				// Controller.getInstance().loadTable(vidtable, (MutiList)
				// vidImpl.getList());
				Controller.getInstance().loadVid(filename.getText(), vidtable);
			}
		});
		loadvid.setBounds(133, 45, 92, 26);
		panel.add(loadvid);

		addvid = new JButton("加入vid");
		addvid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// vidImpl.add(filename.getText());
				Controller.getInstance().addVid(filename.getText(), vidtable);
				// Controller.getInstance().loadTable(vidtable, (MutiList)
				// vidImpl.getList());
			}
		});
		addvid.setBounds(133, 77, 93, 23);
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
		thread.setText("1");
		thread.setBounds(560, 16, 103, 26);
		panel.add(thread);
		thread.setColumns(10);

		begin = new JButton("开始");
		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				switch ((String) mission.getSelectedItem()) {
				case "guess":
					print("guess!!!");
					Controller.getInstance().guess(Integer.parseInt(thread.getText()), false);
					break;
				case "checkIn":
					print("checkIn!!!");

					break;

				}

			}
		});
		begin.setBounds(663, 45, 103, 29);
		panel.add(begin);

		mission = new JComboBox();
		mission.setModel(new DefaultComboBoxModel(new String[] { "guess", "checkIn" }));
		mission.setBounds(560, 46, 103, 27);
		panel.add(mission);

		resume = new JButton("暂停");
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("暂停".equals(begin.getText())) {
					begin.setText("继续");
					Controller.getInstance().stop();
				} else {
					Controller.getInstance().resume();
					begin.setText("暂停");

				}
			}
		});
		resume.setBounds(663, 74, 103, 29);
		panel.add(resume);

	}

	/**
	 * 实现打印操作。
	 */
	@Override
	public void print(String data) {
		Label.setText(data);
	}

	/**
	 * 刷新列表数据。
	 */
	public void refresh() {
		Controller.getInstance().refresh(cookietable);
	}

	/**
	 * 取窗体实例
	 * 
	 * @return
	 */
	public static Gui getInstance() {
		return frame;
	}
}
