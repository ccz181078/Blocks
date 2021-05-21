package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class DarkIronBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public DarkIronBall(){
		super();
		hp=2000;
	}
	public double mass(){return 1;}
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	public boolean chkRigidBody(){return false;}
	
	public void touchAgent(Agent a){
		if(a.group()==Agent.Group.DARK||hp<=0||a.hp<=0)return;
		double v=2;
		while(a.hp>0&&hp>0){
			v=min(v*1.5,hp);
			a.onAttackedByDark(v,this);
			hp-=v;
		}
	}
	
	@Override
	public double gA(){return 0;}
	
	public BmpRes getBmp(){return game.item.DarkIronBall.bmp;}
}
