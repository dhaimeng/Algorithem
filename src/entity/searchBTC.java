package entity;

/*
 * 20160926处理生成spo三元组供给CreateJson生成json对象
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
import com.mysql.jdbc.PreparedStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
public class searchBTC {
	static HashMap<String,ArrayList<String>> hm= new HashMap<String,ArrayList<String>>();
	public static void main(String[] args){
		// TODO Auto-generated method stub
		Search(GetEntity());
	}
	public static HashMap<String, ArrayList<String>> Search(ArrayList<String> key){
		 Connection conn = null;
	     String sql =null;
	     String url = "jdbc:mysql://202.117.16.65:3306/btc2014?"
	                + "user=root&password=btc2014&useUnicode=true&characterEncoding=UTF8";
	     PreparedStatement pstmt;
	     ResultSet rset;
	     String wpath="D:\\BeijingInfo1.txt";
         try{
	       	 Class.forName("com.mysql.jdbc.Driver");
	       	 conn = DriverManager.getConnection(url);
	       	 FileWriter fw = new FileWriter(wpath,false);
			 BufferedWriter  bw = new BufferedWriter(fw);
	       	 for(String i:key){
	       		ArrayList<String> oneValue=new ArrayList<String>();
	       		ArrayList<String> polist=new ArrayList<String>();
	       		for(int j=1;j<=32;j++){
	   				 sql="SELECT * from btc"+j+" where Subject='<"+i+">'";
	   		         pstmt=(PreparedStatement) conn.prepareStatement(sql);
	   		         rset=pstmt.executeQuery();
	   		         String s,p,o,predicate;	   		         
	   		         while (rset.next()){
	   		        	s=rset.getString("Subject");
	   		        	p=rset.getString("Predicate");
   		        	 	o=rset.getString("Object");
   		        	 	predicate=p.substring(1,p.length()-1 ); 	
   		        	 	if(!oneValue.contains(predicate)){		//属性值只取一个
   		        	 		 oneValue.add(predicate);
   		        	 		 polist.add(p+" "+o);
				             bw.write(s+" "+p+" "+o);
				             bw.newLine();
   		        	 	} 
	   		         }	
	          	 }
	       		if(polist.size()>=1){
	       			hm.put(i, polist);
	       		}
	       		
	          }
	       	 bw.flush();
	       	 fw.close();
         }catch (Exception e){
	       	 e.printStackTrace();	     
   		}
       	return hm;
	}
	public static ArrayList<String> GetEntity(){
		String rpath="D:\\Beijing.txt";
		ArrayList<String> key=new ArrayList<String>();
		try{
			FileReader fr = new FileReader(rpath);
			BufferedReader br = new BufferedReader(fr);
			String pre=br.readLine();
			String s;
			while(pre!=null){
//				s=ExtractSource(pre);
				key.add(pre);
//				Source.add(s);
				pre=br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return key;
	}
}