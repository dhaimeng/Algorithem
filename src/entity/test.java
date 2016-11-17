package entity;

import java.util.ArrayList;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList sl=new ArrayList();
		sl.add("aaa");
		sl.add("aabc");
		sl.add("aadd");
		sl.add("aada");
		sl.add("aadaf");
		for(int i=0;i<sl.size();i++){
			System.out.println(i+""+sl.get(i));
		}
		int num=0;
		int j=0;
		while(j<sl.size()){		
			String pre=sl.get(j).toString();
			if(pre.contains("aa")){
				sl.remove(pre);
				System.out.println(pre);
			}		
//			System.out.println(i+" "+sl.size());
		}
		System.out.println("@@@");
		for(int i=0;i<sl.size();i++){
			System.out.println(i+""+sl.get(i));
		}
	}

}
