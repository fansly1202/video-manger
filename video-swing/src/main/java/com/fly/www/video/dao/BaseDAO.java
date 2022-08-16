package com.fly.www.video.dao;

import com.fly.www.video.utils.ConnDBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO {
		public int update(String sql,Object[] arr) {
			Connection conn = ConnDBUtils.getConnection();
			PreparedStatement pst = null;
			try {
				pst = conn.prepareStatement(sql);
				//��ռλ����ֵ
				for (int i = 0; i < arr.length; i++) {
					pst.setObject(i+1, arr[i]);
				}
				int row = pst.executeUpdate();
				return row;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				ConnDBUtils.closeDB(null, pst, conn);
			}
			return 0;
		}
		public <T>T select(String sql,Object[] arr){
			Connection conn = ConnDBUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet rs = null;
			T t = null;
			try {
				pst= conn.prepareStatement(sql);
				for (int i = 0; i < arr.length; i++) {
					pst.setObject(i+1, arr[i]);
				}
				rs = pst.executeQuery();
				if(rs.next()) {
					t = this.rowMapper(rs);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				ConnDBUtils.closeDB(null, pst, conn);
			}
			return t;
		}
		public <T> List<T> selectAll(String sql,Object[] arr){
			List<T> list = new ArrayList<T>();
			Connection conn = ConnDBUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet rs = null;
			
			try {
				pst= conn.prepareStatement(sql);
				for (int i = 0; i < arr.length; i++) {
					pst.setObject(i+1, arr[i]);
				}
				rs = pst.executeQuery();
				T t = null;
				while(rs.next()) {
					t = this.rowMapper(rs);
					list.add(t);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				ConnDBUtils.closeDB(null, pst, conn);
			}
			return list;
		}
		public  abstract <T> T rowMapper(ResultSet rs) ;
}
