package Batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class SQL {
	private String url;
	private String sql;
	private String tbname;
	public void set(String url,String sql,String tbname){
		this.url=url;
		this.sql=sql;
		this.tbname=tbname;
	}
	public void CONNECT(){
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动程序");
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		}catch (ClassNotFoundException e){
			 System.err.println("faq(): " + e.getMessage());
		}catch(SQLException e){
			e.printStackTrace();
		}	
	}
}
