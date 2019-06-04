package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class BloodBall extends Entity{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BloodBall");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.1;}
	public double height(){return 0.1;}

	protected BloodBall(Agent _src,double _hp){
		x=_src.x+rnd(-0.1,0.1);
		y=_src.y+rnd(-0.1,0.1);
		xv=rnd(-0.2,0.2);
		yv=rnd(-0.2,0.2);
		hp=_hp;
		src=_src;
	}
	public static void drop(Agent a,double hp){
		if(a.hp>0){
			new BloodBall(a,min(hp,a.hp)).add();
			a.hp-=hp;
		}
	}
	public void update(){
		super.update();
		hp-=0.01;
		if(rnd()<0.05)src=null;
	}
	void touchAgent(Agent a){
		if(a==src)return;
		if(hp>0)a.hp=min(a.maxHp(),a.hp+hp);
		remove();
	}

	double gA(){return 0.015;}
	
}
