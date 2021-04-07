package game.entity;

public class Attack implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//描述攻击
	public Source src;
	public double val;
	public Attack(Source s,double v){
		src=s;
		val=v;
		if(s==null)debug.Log.printStackTrace();
	}
}
