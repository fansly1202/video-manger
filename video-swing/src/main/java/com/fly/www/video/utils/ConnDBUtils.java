package com.fly.www.video.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnDBUtils {
	// Connection
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url ="jdbc:mysql://localhost:3306/video-maganger?useSSL=false&serverTimezone=Asia/Shanghai";
			String user = "root";
			String password = "localhost123456";

			conn = DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException  e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public  static void closeDB(ResultSet rs,Statement pst,Connection conn) {
		try {
			if(rs!=null) {
				rs.close();
			}
			if(pst!=null) {
				pst.close();
			}
			if(conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	public static void main(String[] args) {
//		System.out.println(ConnDB.getConnection());
//	}
}
