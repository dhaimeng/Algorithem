package Batch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Read {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	public static void Readfolder(String fpath){
		FileReader fr = null;
		try {
			fr = new FileReader(fpath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
	}
	
}
