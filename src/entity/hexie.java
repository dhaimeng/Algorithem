package entity;
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
import com.mysql.jdbc.PreparedStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
public class hexie {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	static HashMap hm= new HashMap();
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		 Connection conn = null;
	     String sql ="";
	     String url = "jdbc:mysql://202.117.16.65:3306/btc2014?"
	                + "user=root&password=btc2014&useUnicode=true&characterEncoding=UTF8";
         String pre=null;
         Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
         System.out.println("成功加载MySQL驱动程序");
	     conn = DriverManager.getConnection(url);
	     PreparedStatement pstmt;
         ResultSet rset;
         String filePath="D:\\学习中\\实验室\\RDF\\Data\\URL\\testdata.txt";
		 FileReader fr = new FileReader(filePath);
		 BufferedReader br = new BufferedReader(fr);
	     pre=br.readLine();
		 while (pre!= null) { 
			   for(int j=1;j<=32;j++){
				   sql="SELECT * from btc"+j+" where Subject='<"+pre+">'";
				   pstmt=(PreparedStatement) conn.prepareStatement(sql);
		           rset=pstmt.executeQuery();
		           String sb;
//				   if(rset.next()){
		           while(rset.next()){
//					   System.out.println(pre);
					   sb=rset.getString("Subject")+" "+rset.getString("Predicate")+" "+rset.getString("Object")+"\r"+"\n";
			           if(!hm.containsKey(sb)){
		            	 hm.put(sb, 1);
			             System.out.print(sb);
			   		   } 
//					   break;
				   }
//		           else{
//					   continue;
//				   }
			   }
//			        try {
//			        	
//			        	pstmt=(PreparedStatement) conn.prepareStatement(sql);
//			        	rset=pstmt.executeQuery();
//			        	String sb=new String();
//			        	while (rset.next()){
//				            sb=rset.getString("Subject")+" "+rset.getString("Predicate")+" "+rset.getString("Object")+"\r"+"\n";				             
//				            if(!hm.containsKey(sb)){
//				            	 hm.put(sb, 1);
//					             System.out.print(sb);
//					   		} 
//			            }				   		 
//				   }catch (SQLException e) {
//			            e.printStackTrace();
//			            continue;
//			       }
//			        pstmt.close();
//			        rset.close();
//			  }				   	
			  pre=br.readLine();	 
//			  hm.clear();
		 }	
   		fr.close();
	    br.close();
		System.gc();
		}
}