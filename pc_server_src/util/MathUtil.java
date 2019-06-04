package util;
import java.util.Random;

public final class MathUtil{
	private static final long serialVersionUID=1844677L;
	//这个工具类一般在用到时使用 import static 方式导入

	private static Random __rnd=new Random();
	
	//double to int randomly
	//随机舍入到最接近的两个整数之一，保持期望值不变
	public static int rf2i(double x){
		double y=Math.floor(x);
		if(__rnd.nextDouble()<x-y)return (int)y+1;
		return (int)y;
	}
	
	//double to int
	//向下取整
	public static int f2i(double x){
		return (int)Math.floor(x);
	}
	
	//random double between l,r
	//区间内均匀随机浮点数
	public static double rnd(double l,double r){
		return l+(r-l)*__rnd.nextDouble();
	}
	
	public static double rnd(double a){
		return a*__rnd.nextDouble();
	}
	
	public static double rnd(){
		return __rnd.nextDouble();
	}
	
	public static double rnd_gaussion(){
		return __rnd.nextGaussian();
	}
	
	//random int between l,r
	//闭区间内均匀随机整数
	public static int rndi(int l,int r){
		return __rnd.nextInt(r-l+1)+l;
	}
}
