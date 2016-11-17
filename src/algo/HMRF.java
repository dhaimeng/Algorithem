 package algo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import Jama.Matrix;
/*第三步，生成迭代初始矩阵，network. i->j初始表等*/
public class HMRF {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
	}
	double[] fiy1=new double[10];
	double[] fiy0=new double[10];	
	public void SetInitialTrust(Matrix A) throws IOException {
		double[] inisource=new double[]{0.02,0.42,0.26,0.04,0.26};		
		for(int i=0;i<5;i++){		//数据源提供对象，直接赋初值
			for(int j=5;j<10;j++){
				double exist=A.get(i, j);
				if(exist>0){
					fiy1[i]=inisource[i];
					fiy0[i]=1-inisource[i];
					break;
				}
			}
			
		}
		for(int i=5;i<10;i++){		//对象被多个数据源提供，取平均
			int num=0;
			for(int j=0;j<5;j++){
				double exist=A.get(i, j);				
				if(exist>0){
					fiy1[i]+=fiy1[j];
					num++;
				}
			}
			if(num!=0){
				fiy1[i]=fiy1[i]/num;
				fiy0[i]=1-fiy1[i];
			}
		}
	}
	public double[] getFiy1() {
		return fiy1;
	}
	public double[] getFiy0() {
		return fiy0;
	}

	
	/*public static void setInI() throws IOException {
		DecimalFormat df = new DecimalFormat( "0.00 ");  
		double[][] testA={
			  {0,0,0,0,0,1,0,0,0,0},
			  {0,0,1,0,0,0,1,0,0,0},
			  {0,1,0,0,0,0,1,0,0,0},
			  {0,0,0,0,0,0,0,0,1,0},
			  {0,0,0,0,0,0,0,0,0,1},
			  {1,0,0,0,0,0,0,0,0,0},
			  {0,1,1,0,0,0,0,0,0,1},
			  {0,0,0,0,0,0,0,0,0,0},
			  {0,0,0,1,0,0,0,0,0,0},
			  {0,0,0,0,1,0,1,0,0,0},
		};
		Matrix A=new Matrix(testA);
		double[] inisource=new double[]{0.02,0.42,0.26,0.04,0.26};
		double[] fiy1=new double[10];
		double[] fiy0=new double[10];
		for(int i=0;i<5;i++){		//数据源提供对象，直接赋初值
			for(int j=5;j<10;j++){
				double exist=A.get(i, j);
				if(exist>0){
					fiy1[i]=inisource[i];
					fiy0[i]=1-inisource[i];
					break;
				}
			}
			
		}

		for(int i=5;i<10;i++){		//对象被多个数据源提供，取平均
			int num=0;
			for(int j=0;j<5;j++){
				double exist=A.get(i, j);				
				if(exist>0){					
					fiy1[i]+=fiy1[j];					
					num++;
				}
			}
			if(num!=0){				
				fiy1[i]=fiy1[i]/num;								
				fiy0[i]=1-fiy1[i];
			}

		}
		for(double d:fiy1){
			System.out.println(d);
		}
		System.out.println("hahah");
		for(double d:fiy0){
			System.out.println(d);
		}
	}*/
}
