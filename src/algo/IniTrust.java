package algo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import Jama.Matrix;

/*第三步，生成迭代初始矩阵，network. i->j初始表等*/
public class IniTrust {
	static int count=0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		double[] trust= SelectTrust();
		
//		System.out.println(B.);
	}
	public static double[] SelectTrust() throws IOException{
		double[][] testA = { 
				{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, 
				{ 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0, 1, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 1, 1, 0, 0, 0, 0, 0, 0, 0.88 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 1, 0, 0.88, 0, 0, 0 },
				};
		Matrix A = new Matrix(testA);
		double[] fiy1 = new double[] { 0.02, 0.42, 0.26, 0.04, 0.26, 0.02, 0.34, 0.00, 0.04, 0.26 };
		double[] fiy0 = new double[] { 0.98, 0.58, 0.74, 0.96, 0.74, 0.98, 0.66, 1.00, 0.96, 0.74 };

		Matrix B=setMij(A,fiy1,fiy0);
		double[] trust=new double[10];
		for (int i = 0; i < 10; i++) {
			double neighbor=1;
			for (int j = 0; j < 10; j++) {
				double exist = B.get(i, j);
				
				if (exist > 0.00001) {
					neighbor *= exist;
				}
		
					trust[i]=fiy1[i]*neighbor;				
			}
			System.out.println(trust[i]);
		}
		return trust;
	}
	

	// 生成初始矩阵后，迭代计算信息传递直到所有Mij稳定。即初始矩阵的生出矩阵各项不变。
	public static Matrix setMij(Matrix A,double[] fiy1,double[] fiy0) throws IOException {

		// 数据源相似度矩阵，用于ss的势能函数
		double[][] testS = { 
				{ 0.000, 0.000, 0.000, 0.430, 0.013 }, 
				{ 0.000, 0.000, 0.131, 0.000, 0.040 },
				{ 0.000, 0.131, 0.000, 0.000, 0.000 }, 
				{ 0.430, 0.000, 0.000, 0.000, 0.011 },
				{ 0.013, 0.040, 0.000, 0.011, 0.000 },
				};
		Matrix S = new Matrix(testS);
		
		Matrix B = new Matrix(10, 10); // 生成Mij矩阵

		double yipisilou; // 从矩阵S中获取
		double sim; // 从矩阵A中获得
		double bata = 0.7;
		double sigma = 0.1; // bata.sigma调参，这里取最优
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) { // 源源之间
				double exist = A.get(i, j);
				if (exist > 0) {
					yipisilou = S.get(i, j);
					double neighbor = GetNeighborMij(j, A);
					double delivery = yipisilou * fiy1[j] * neighbor + (1 - yipisilou) * fiy0[j] * neighbor;
					B.set(i, j, delivery);
				}
			}
			for (int j = 5; j < 10; j++) { // 源值之间
				double exist = A.get(i, j);
				if (exist > 0) {
					double neighbor = GetNeighborMij(j, A);
					double delivery = bata * fiy1[j] * neighbor + (1 - bata) * fiy0[j] * neighbor;
					B.set(i, j, delivery);
				}
			}

		}
		for (int i = 5; i < 10; i++) {
			for (int j = 0; j < 5; j++) { // 值源之间
				double exist = A.get(i, j);
				if (exist > 0) {
				
					double neighbor = GetNeighborMij(j, A);
					double delivery = bata* fiy1[j] * neighbor + (1 - sigma) * fiy0[j] * neighbor;
					B.set(i, j, delivery);
				}
			}
			for (int j = 5; j < 10; j++) { // 值值之间
				double exist = A.get(i, j);
				if (exist > 0) {
					sim = exist;
					double neighbor = GetNeighborMij(j, A);
					double delivery = sim * fiy1[j] * neighbor + (1 - sim) * fiy0[j] * neighbor;
					B.set(i, j, delivery);
				}
			}

		}
//		if(count>10){
//			return B;
//		}
		if(!MEqual(A,B)){			
			count++;
			System.out.println("迭代次数："+count);
			B.print(6, 5);
			B=setMij(B, fiy1, fiy0);
		}
		return B;
	}
	public static boolean MEqual(Matrix A,Matrix B){
		Matrix C=A.minus(B);
		for (int i = 0; i < 10; i++) { // 数据源提供对象，直接赋初值
			for (int j = 0; j < 10; j++) {
				if(Math.abs(C.get(i,j))>0.01){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 
	 * @param j
	 *            返回j这个列的j行的有值的结果
	 */
	public static double GetNeighborMij(int j, Matrix B) {
		double neighbor = 1;
		for (int k = 0; k < 10; k++) {
			double exist = B.get(j, k);
			if (exist > 0) {
				neighbor *= exist;
			}
		}
		return neighbor;
	}

	// 被iniMetrice调用，生出每个实体每个属性的10个节点初始trust值
		public static double[] SetInitialTrust(Matrix A) throws IOException {
			double[] fiy1 = new double[10];
			// double[] fiy0=new double[10];
			double[] inisource = new double[] { 0.02, 0.42, 0.26, 0.04, 0.26 };
			for (int i = 0; i < 5; i++) { // 数据源提供对象，直接赋初值
				for (int j = 5; j < 10; j++) {
					double exist = A.get(i, j);
					if (exist > 0) {
						fiy1[i] = inisource[i];
						break;
					}
				}

			}
			for (int i = 5; i < 10; i++) { // 对象被多个数据源提供，取平均
				int num = 0;
				for (int j = 0; j < 5; j++) {
					double exist = A.get(i, j);
					if (exist > 0) {
						fiy1[i] += fiy1[j];
						num++;
					}
				}
				if (num != 0) {
					fiy1[i] = fiy1[i] / num;
				}
			}
			return fiy1;
		}
}
