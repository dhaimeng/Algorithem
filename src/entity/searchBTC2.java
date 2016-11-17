package entity;
/**
 * 对sameAs后的实体进行数据库查询
 */
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class searchBTC2 {
	static Connection conn = null;
	static String sql =null;
	static String url = "jdbc:mysql://202.117.16.65:3306/btc2014?"
               + "user=root&password=btc2014&useUnicode=true&characterEncoding=UTF8";
	static PreparedStatement pstmt;
	static ResultSet rset;
	public static void main(String[] args){
		// TODO Auto-generated method stub
		Con();
		readentity("D:\\学习中\\工程代码\\SPO处理\\data\\oneexample\\1.txt");		
	}
	public static void Con(){
		try{
       	 	Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
       	 	conn = DriverManager.getConnection(url);     	 	
        }catch (ClassNotFoundException e){
       	 	e.printStackTrace();
        }catch(SQLException e){
			 e.printStackTrace();
		}
	}
	public static void readentity(String tpath){
		try{
			FileReader fr = new FileReader(tpath);
			BufferedReader br = new BufferedReader(fr);
			String pre=br.readLine();			
			while(pre!=null){
				HashMap preSPO=search(pre);
				if(preSPO.size()>0){
					printMap2(preSPO);
				}
//				System.out.println(pre+" @ "+preSPO.size());
				pre=br.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static HashMap search(String pre){
//		String pre="http://data.nytimes.com/20767311849320512541";
		int num=0;
		HashMap hm= new HashMap();
		try{
			for(int j=1;j<=32;j++){
	   		     String indexname=pre;
				 sql="SELECT * from btc"+j+" where Subject='<"+indexname+">'";
		         pstmt=(PreparedStatement) conn.prepareStatement(sql);
		         rset=pstmt.executeQuery();
		         String sb=new String();
		         while (rset.next()){
			            sb=rset.getString("Subject")+" "+rset.getString("Predicate")+" "+rset.getString("Object");				             
			            if(!hm.containsKey(sb)){
			            	 hm.put(sb, 1);
			            	 num++;
//				             System.out.println(num+sb.toString());
				   		} 
		         }	
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return hm;
	}
	public static void printMap2(HashMap map){
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			try {
				Object key = it.next();
				System.out.println(key.toString());
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
}