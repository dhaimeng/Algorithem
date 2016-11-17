package Batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Print{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/*按照key值升降序输出*/
	public static void printMap1(Map map){
		Object[] key =  map.keySet().toArray();    
		Arrays.sort(key);  
		for(int i = 0; i<key.length; i++)
//		for(int i = key.length-1; i>=0; i--)    //降序输出
		{  
		     System.out.println(key[i]+" "+map.get(key[i]));  
		}
	}
	/*随机输出*/
	public static void printMap2(HashMap map){
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			try {
				Object key = it.next();
				System.out.println(key.toString()+" "+map.get(key));
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
	/*按照Value值排序*/
	public static void sortbyValue(Map<String,Double> map){
		ArrayList<Map.Entry<String,Double>> ID=new ArrayList<Map.Entry<String, Double>>(map.entrySet());		
		Collections.sort(ID, new Comparator<Map.Entry<String, Double>>() {   
		    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {      
		    	if ((o2.getValue() - o1.getValue())>0)  
		            return 1;  
		          else if((o2.getValue() - o1.getValue())==0)  
		            return 0;  
		          else   
		            return -1;  
		    }
		});
		//将HASHMAP中的数据排序
		for (Iterator iter = ID.iterator(); iter.hasNext();) 
		{
		   Map.Entry entry = (Map.Entry)iter.next();
		   String  key = (String)entry.getKey();
		   System.out.println(key.toString()+" "+map.get(key));  
		}
	}

}
