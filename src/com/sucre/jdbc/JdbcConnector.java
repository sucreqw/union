package com.sucre.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;
import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;

public class JdbcConnector {

    // 数据库连接实例
    private static Connection con = null;
    // 数据库ip地址
    private static String jdbcip = "";
    // 数据库账号
    private static String id = "";
    // 数据库账号密码
    private static String pass = "";
    // 数据库表名
    public static String source = "";

    public JdbcConnector() {
    }

    public static Connection getConnection() throws Exception// 连接数据库的函数
    {
        String DBDRIVER = "org.gjt.mm.mysql.Driver";
        String DBURL = "jdbc:mysql://" + jdbcip + ":3306/" + source + "?useSSL=false&serverTimezone=GMT%2B8";// 这里的world是你的具体数据库的名字，定义数据库的url
        String username = id;
        String password = pass;// 这里填写你自己的密码
        try {
            if (null == con) {
                Class.forName(DBDRIVER);// 登陆到数据库上
                con = (Connection) DriverManager.getConnection(DBURL, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyUtil.print("getConnection()函数出错", Factor.getGui());
        }

        return con;
    }

    public static void Load(String filename) throws Exception {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(filename)));
            jdbcip = (properties.getProperty("ip"));
            id = (properties.getProperty("id"));
            pass = (properties.getProperty("pass"));
            source = (properties.getProperty("source"));
        } catch (FileNotFoundException e) {
            // System.out.println("导入ip文件错误：" + e.getMessage());
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            // System.out.println("导入ip文件错误：" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
