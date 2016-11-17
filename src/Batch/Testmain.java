package Batch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Testmain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader("./data\\a.txt");
		BufferedReader br = new BufferedReader(fr);
		String pre=br.readLine();
		HashSet hs=new HashSet();
		while(pre!=null){
			hs.add(pre);
			pre=br.readLine();
		}
		System.out.println(hs.size());
	}

}
