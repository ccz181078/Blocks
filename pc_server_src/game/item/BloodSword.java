package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.Agent;
import game.entity.BloodBall;

public class BloodSword extends SwordType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BloodSword");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public int maxDamage(){return 20;}
	public void onBroken(double x,double y){
		new IronStick().drop(x,y);
	}

	public void onAttack(Agent a){
		for(int i=0;i<3;++i)BloodBall.drop(a,rnd(1,3));
		super.onAttack(a);
	}
	
	public double repairRate(){return 0.02;}
}

