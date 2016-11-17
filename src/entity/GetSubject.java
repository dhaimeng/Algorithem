package entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 利用sameAs
 * @author dhm
 *
 */
public class GetSubject {
	static ArrayList afterML=new ArrayList();
	static HashSet<String> Resource=new HashSet<String>();
	static ArrayList<String> oneBestEntity=new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String URI="http://dbpedia.org/resource/Barack_Obama";
//		ArrayList SameAsResult=ExtractSameAs.DataBaseSameAs(URI);
//		afterML=filterML(SameAsResult);
		GetResource();
//		for(int i=0;i<afterML.size();i++){
//			 System.out.println(afterML.get(i));
//		}

	}

	public static ArrayList filterML(ArrayList SameAsResult){
		int num=0;
		for(int i=0;i<SameAsResult.size();i++){
			 String pre=SameAsResult.get(i).toString();
			 String sub=pre.split("/")[2];
			 if(sub.contains(".dbpedia.org")){
//				 System.out.println(num+++" "+pre);
				 continue;
			 }
			 else{
				 afterML.add(pre);
			 }
//			 System.out.println(pre);
		}
		return afterML;
	}	
	public static HashSet GetResource(){
		String filePath="D:\\学习中\\实验室\\RDF\\Data\\URL\\分析数据源情况org.txt";
		try{
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String pre=br.readLine();
			while(pre!=null){
				afterML.add(pre);
				pre=br.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		String pre,sub;
		for(int i=0;i<afterML.size();i++){
			pre=afterML.get(i).toString();
			sub=pre.split("/")[2];
			Resource.add(sub);
		}		
		Iterator it=Resource.iterator(); 
		int i=1;
		while(it.hasNext()) { 
			System.out.println(i+++" "+it.next());
		} 
		return 	Resource;
	}
}
