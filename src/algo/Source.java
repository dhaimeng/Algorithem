package algo;
/*第一步。对PageRank后的资源置信度进行归一化*/
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class Source {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadRank();
		System.out.println("数据源归一化完成");
	}
	public static void ReadRank(){
		HashMap<String, Double> sourcepage=new HashMap<String,Double>();
		String originPath="E:\\entity\\obResolution\\BeliefRank1.txt";
		File afterPath=new File("E:\\entity\\obResolution\\UniformSourceRank1.txt");
		double s=0;
		try {
			List<String>  list = FileUtils.readLines(new File(originPath));
			for (String lines : list) {
	        	String[] nx=lines.split("	");
	        	double ini_score=Double.parseDouble(nx[1]);
	        	s+=ini_score;
	        	sourcepage.put(nx[0], ini_score);
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}             
		Object[] key =  sourcepage.keySet().toArray();    
		for(int i = 0; i<key.length; i++){
			double uniform=sourcepage.get(key[i])/s;
			sourcepage.put(key[i].toString(), uniform);
			try {
				FileUtils.write(afterPath, key[i].toString()+","+uniform, true);
				FileUtils.write(afterPath, "\n", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

}
