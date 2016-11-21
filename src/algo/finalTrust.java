package algo;

import java.awt.Container;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import Jama.Matrix;

/*第三步，生成迭代初始矩阵，network. i->j初始表等*/
/*
 * 输入：初始矩阵A，（边以及OO间相似度simcan参数），初始可信度fiy1和fiy0,数据源相似矩阵S（Episilou）,bata,sigma等两个SO参数目前固定。
迭代后输出：
稳定矩阵B，10个节点的可信度数组Trust
 */
public class finalTrust {
	static int count=0;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String searchA="http://dbpedia.org/resource/Bushwick,_Brooklyn";
		File f = new File("E:\\entity\\obResolution\\121\\5.txt");
		String searchB="lat";
		String property="<"+searchB+">";
		ArrayList<String> pso=TruthDiscovery(property,f);
		Matrix A=createNetwork2(pso,property);
		System.out.println("初始关联矩阵：");
		A.print(4, 3);
		double[] fiy1=SetInitialTrust(A);
		double[] fiy0=new double[10];
		System.out.println("初始可信度：");
		for (int i = 0; i < fiy1.length; i++) {
			System.out.print(fiy1[i]+"  ");
			fiy0[i]=1-fiy1[i];
		}
		System.out.println();
		double[] trust= SelectTrust(A,fiy1,fiy0);
	}
	public static void FindTruth(String property,File f) throws IOException{
		ArrayList<String> pso=TruthDiscovery(property,f);
		Matrix A=createNetwork2(pso,property);
		System.out.println("初始关联矩阵：");
		A.print(4, 3);
		double[] fiy1=SetInitialTrust(A);
		double[] fiy0=new double[10];
		System.out.println("初始可信度：");
		for (int i = 0; i < fiy1.length; i++) {
			System.out.print(fiy1[i]+"  ");
			fiy0[i]=1-fiy1[i];
		}
		System.out.println();
		double[] trust= SelectTrust(A,fiy1,fiy0);
	}
	/*API里判断实体是否存在，读取相应属性，这里是必须存在的A,B */
	public static ArrayList<String> TruthDiscovery(String property,File f ){
		
		ArrayList<String> pso = new ArrayList<String>();
		
		try {
			List<String> list = FileUtils.readLines(f);
			for (int i=0; i<list.size();i++) {
				if (list.get(i).contains(property)) {
					for(int j=0;j<5;j++){		//取5行
						i++;
						String lines=list.get(i);
						int have = StringUtils.countMatches(lines, "<"); // 记录是否提供了该对象的值
						if (have > 1) { // 提供了，有至少两个<
							String object = lines.substring(lines.lastIndexOf("> ") + 2, lines.length()).trim();
							pso.add(object);
						} else {
							pso.add("null");
						}
					}
					break;
				}
			}
			
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return pso;
	}
	// 值间相似度和相同值的处理。
		public static Matrix createNetwork2(ArrayList<String> pso,String property) {
			Matrix A = new Matrix(10, 10); // 实体下的初始矩阵
			ArrayList<String> obsame = new ArrayList<String>(); // null占位，去重后的对象值0,1,2,3,4,5
			ArrayList<String> obSIM = new ArrayList<String>(); // null+不去重复
			try {
				for (int i = 0; i < pso.size(); i++) {
					String oValue = pso.get(i);
					obSIM.add(oValue);
					if (!oValue.equals("null")) {
						if (!obsame.contains(oValue)) { // 第一次出现的ovalue直接置1
							obsame.add(oValue);
							int row = i ;
							int columm = i +5;
							A.set(row, columm, 1);
							A.set(columm, row, 1);
						} else { // 重复出现的，s间置1，o保留
							int colum2 = obsame.indexOf(oValue) + 5; // 保留1个o的列序号
							int row2 = i; // 当前应填的行，填到1个o,即重复提供o的数据源序列
							int row1 = obsame.indexOf(oValue);
							A.set(row2, colum2, 1);
							A.set(colum2, row2, 1);
							A.set(row2, row1, 1);
							A.set(row1, row2, 1);
							/* 计算数据源相似度用于episilou赋值用 */
							// System.out.println(fileTo+" "+property);
							//System.out.println(row1 + " " + row2);
						}
					} else {
							obsame.add(oValue);
					}											
					// System.out.println(fileTo+" "+property+" "+obSIM.size());					
				}
				/* 输出初始矩阵用 */
				A=CulculateSimilarity(property,obSIM,A);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return A;
		}
		//值间相似度置到矩阵里
		public static Matrix CulculateSimilarity(String property, ArrayList<String> obSIM, Matrix A) {
			for (int i = 0; i < obSIM.size() - 1; i++) {
				String a = obSIM.get(i);
				int anum = obSIM.indexOf(a);
				if (a.equals("null") || anum < i) {
					continue;
				} else {
					for (int j = i + 1; j < obSIM.size(); j++) {
						String b = obSIM.get(j);
						int bnum = obSIM.indexOf(b);
						if (b.equals("null") || bnum < j) {
							continue;
						} else {
							double sim = 0.0;
							if (property.equals("<label>") || property.equals("<type>") || property.equals("<area>")) {
								sim = EditDis.simString(a, b);
							} else {
								double ad = Double.parseDouble(a);
								double bd = Double.parseDouble(b);
								sim = EditDis.simDouble(ad, bd);
							}
							if (sim > 0.8) {
								int row = i + 5;
								int column = j + 5;
								//System.out.println(i+" "+j);
//								System.out.println(sim);
								A.set(row, column, sim);
								A.set(column, row, sim);
							}
						}

					}
				}

			}
			return A;
		}
	public static double[] SelectTrust(Matrix A,double[] fiy1,double[] fiy0 ) throws IOException{
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
			System.out.print(trust[i]+"  ");
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
