package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class DarkBall extends PureEnergyBall{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return bmp;}
	static BmpRes bmp=new BmpRes("Entity/DarkBall");
	
	public DarkBall(){
		hp=rnd_exp(5)+rnd_exp(5);
	}

	@Override
	public double light(){
		return 0;
	}
	@Override
	public double RPG_ExplodeProb(){return 0;}
	
	void touchAgent(Agent a){
		if(a.group()==Agent.Group.DARK||hp<=0||a.hp<=0)return;
		a.onAttackedByDark(hp*0.6,this);
		kill();
	}
}
