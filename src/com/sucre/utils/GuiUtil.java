package com.sucre.utils;

import javax.swing.table.DefaultTableModel;

import com.sucre.listUtil.MutiList;

import java.util.List;

import javax.swing.JTable;

public class GuiUtil {

	public static void loadTable(JTable table, MutiList list) {
		String[] temp = list.get(0).split("\\|");
		String[] columnName = new String[temp.length];

		for (int i = 0; i < columnName.length; i++) {
			columnName[i] = "列数" + i;
		}
        
		String[][] data = new String[list.getSize()][temp.length];
		System.out.println(list.getSize());
		for (int i = 0; i < list.getSize(); i++) {
			temp = list.get(i).split("\\|");

			for (int j = 0; j < temp.length; j++) {
				data[i][j] = temp[j];
			}

		}
		DefaultTableModel d = new DefaultTableModel(data, columnName);
		table.setModel(d);
	}

}
