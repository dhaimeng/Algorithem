package algo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import Jama.Matrix;

/** 对btc搜索到的记录化简成S_O格式 */
public class testRD {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Read2Net();
	}

	public static void Read2Net() {
		File directory = new File("E:\\entity\\obResolution\\121\\");
		String PathTO = "E:\\entity\\obResolution\\Iteraftet121\\";
		File[] files = directory.listFiles();
		// String domainSP =
		// "Predicate,data.nytimes.com,dbpedia.org,rdf.freebase.com,sws.geonames.org,yago-knowledge.org";
		try {
			for (File f : files) {
				ArrayList<String> pso = new ArrayList<String>();
				String filename = f.getName();
				File fileTo = new File(PathTO + filename);
				List<String> list = FileUtils.readLines(f);
				for (String lines : list) {
					if (!lines.equals("")) {
						if (!lines.startsWith("<http")) {
							String predicate = lines.substring(0, lines.indexOf(" "));
							pso.add(predicate);
						} else {
							int have = StringUtils.countMatches(lines, "<"); // 记录是否提供了该对象的值
							// System.out.println(lines + " " + have);
							if (have > 1) { // 提供了，有至少两个<
								String object = lines.substring(lines.lastIndexOf("> ") + 2, lines.length()).trim();
								pso.add(object);
							} else {
								pso.add("null");
							}
						}
					}
				}
				createNetwork2(pso, fileTo);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//值间相似度和相同值的处理。
	public static void createNetwork2(ArrayList<String> pso,File fileTo){
		
		try{
			for (int i = 0; i < pso.size(); i++) {
				if(i%6==0){				
					String property=pso.get(i);
					FileUtils.write(fileTo, pso.get(i), true);	
					Matrix A = new Matrix(10,10);	//实体下的初始矩阵
					ArrayList<String> obsame=new ArrayList<String>();	//null占位，去重后的对象值0,1,2,3,4,5
					ArrayList<String> obSIM=new ArrayList<String>();	//null+不去重复
					i++;
					do{		
						
						String oValue=pso.get(i);
						obSIM.add(oValue);
						if(!oValue.equals("null")){
							if(!obsame.contains(oValue)){	//第一次出现的ovalue直接置1							
								obsame.add(oValue);								
								int row=i%6-1;
								int columm=i%6+4;
								A.set(row, columm, 1);
								A.set(columm, row, 1);
							}else{					//重复出现的，s间置1，o保留
								int colum2=obsame.indexOf(oValue)+5;		//保留1个o的列序号
//								int now=i%6+4;					//当前应填的列o，因只出现一次这列空
								int row2=i%6-1;					//当前应填的行，填到1个o
								int row1=obsame.indexOf(oValue);
								A.set(row2, colum2, 1);
								A.set(colum2, row2, 1);								
								A.set(row2, row1, 1);
								A.set(row1, row2, 1);								
							}
						}else{
							obsame.add(oValue);							
						}						
						i++;
					}while(i%6!=0);
//					System.out.println(fileTo+" "+property+" "+obSIM.size());
					A=CulculateSimilarity(property,obSIM,A,fileTo);
					PrintWriter pw=new PrintWriter(new FileWriter(fileTo,true));
					A.print(pw,1,0);
					pw.close();

					i--;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Matrix CulculateSimilarity(String property,ArrayList<String> obSIM, Matrix A,File fileTo){
		for(int i=0;i<obSIM.size()-1;i++){
			String a=obSIM.get(i);
			int anum=obSIM.indexOf(a);
			if(a.equals("null")||anum<i){
				continue;
			}else{
				for(int j=i+1;j<obSIM.size();j++){
					String b=obSIM.get(j);
					int bnum=obSIM.indexOf(b);
					if(b.equals("null")||bnum<j){
						continue;
					}else{
						double sim=0;
						if(property.equals("<label>")||property.equals("<type>")||property.equals("<area>")){
							sim=EditDis.simString(a, b);
						}else{
							double ad=Double.parseDouble(a);
							double bd=Double.parseDouble(b);					
							sim=EditDis.simDouble(ad, bd);
						}
						if(sim>0.8){
//							System.out.println(property+" "+a+" =="+b+" "+fileTo);
							int row=i+5;
							int column=j+5;
//							System.out.println(i+" "+j); 
							A.set(row, column, 1);
							A.set(column, row, 1);
						}
					}
					
				}
			}
			
		}
		return A;
	}
	//已处理完存到after，没什么用了
		public static void createNetwork(ArrayList<String> pso, File fileTo) throws IOException {
			System.out.println(pso.size());
			for (int i = 0; i < pso.size(); i++) {
				if (i % 6 == 0) {
					FileUtils.write(fileTo, pso.get(i) + "\n", true);
					i++;
					do {
						FileUtils.write(fileTo, pso.get(i) + "<> ", true);
						i++;
					} while (i % 6 != 0);
					i--;
					FileUtils.write(fileTo, "\n", true);
				}
			}
		}
		//由查询结果直接获取初始矩阵，提供值的so处理完
		public static void createNetwork1(ArrayList<String> pso,File fileTo){
//			System.out.println(pso.size());	
			try{
				for (int i = 0; i < pso.size(); i++) {
					if(i%6==0){				
						FileUtils.write(fileTo, pso.get(i), true);	
						Matrix A = new Matrix(10,10);	//实体下的初始矩阵
						i++;
						do{		
							String oValue=pso.get(i);
							if(!oValue.equals("null")){
								int row=i%6-1;
								int columm=i%6+4;
//								System.out.println(row+" "+columm);
								A.set(row, columm, 1);
								A.set(columm, row, 1);
							}
							i++;
						}while(i%6!=0);
						PrintWriter pw=new PrintWriter(new FileWriter(fileTo,true));
						A.print(pw,1,0);
						pw.close();
						i--;
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
}
