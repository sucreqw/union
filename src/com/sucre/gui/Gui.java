package com.sucre.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.sucre.controller.Controller;
import com.sucre.listUtil.MutiList;
import com.sucre.service.WeiboImpl;
import com.sucre.utils.GuiUtil;
import com.sucre.utils.Printer;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Gui extends JFrame implements Printer {

	private JPanel contentPane;
	private JTextField filename;
	private JLabel Label;
	private JButton loadId;
	private JButton LoadCookie;
	public static Gui frame = new Gui();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//加载窗体。
		frame.setVisible(true);
		//导入ip文件。
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
		filename.setBounds(135, 43, 147, 26);
		panel.add(filename);
		filename.setColumns(10);

		Label = new JLabel("");
		Label.setBounds(6, 5, 645, 26);
		panel.add(Label);

		JButton login = new JButton("登录");
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		login.setBounds(663, 16, 103, 26);
		panel.add(login);

		loadId = new JButton("导入id");
		loadId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WeiboImpl weiboId =new WeiboImpl();
				weiboId.loadList(filename.getText());
			}
		});
		loadId.setBounds(6, 43, 117, 29);
		panel.add(loadId);

		LoadCookie = new JButton("导入cookie");
		LoadCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WeiboImpl weiboCookie=new WeiboImpl();
				
				weiboCookie.loadList(filename.getText());
				GuiUtil.loadTable(table, (MutiList)weiboCookie.getlist());
			
			}
		});
		LoadCookie.setBounds(6, 74, 117, 29);
		panel.add(LoadCookie);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 126, 357, 250);
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

	}
	

	/**
	 * 实现打印操作。
	 */
	@Override
	public void print(String data) {
		Label.setText(data);
	}

	public static Gui getInstance() {
		return frame;
	}
}
