package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class BloodBall extends Ball{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BloodBall");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.1;}
	public double height(){return 0.1;}
	public double mass(){return 0.2;}
	public boolean chkEnt(){return false;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	
	@Override
	public double gA(){return 0.03;}

	@Override
	public boolean chkRigidBody(){
		return true;
	}

	@Override
	double friction(){
		return 1;
	}
	
	@Override
	public boolean harmless(){return true;}
	Agent src;
	protected BloodBall(Agent _src,double _hp){
		this(_src.x,_src.y,_hp);
		src=_src;
	}
	public BloodBall(double _hp){
		hp=_hp;
	}
	public BloodBall(double _x,double _y,double _hp){
		double x1=rnd_gaussion()*0.1;
		double y1=rnd_gaussion()*0.1;
		x=_x+x1;
		y=_y+y1;
		xv=x1;
		yv=y1;
		hp=_hp;
		src=null;
	}
	public static BloodBall drop(Agent a,double hp,Source src){
		if(!a.hasBlood())return null;
		if(a.hp>0){
			BloodBall b=new BloodBall(a,min(hp,a.hp));
			b.add();
			a.loseHp(hp,SourceTool.make(src,"的吸血效果"));
			return b;
		}
		return null;
	}
	public void update(){
		super.update();
		hp+=0.02;
		if(rnd()<0.02)src=null;
	}
	void touchAgent(Agent a){
		if(a==src)return;
		if(hp>0)a.addHp(hp);
		remove();
	}

}
