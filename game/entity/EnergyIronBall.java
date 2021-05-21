package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class EnergyIronBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public EnergyIronBall(){
		super();
		hp=2000;
	}
	public double mass(){return 1;}
	
	public void touchAgent(Agent a){
		if(hp<=0||a.hp<=0)return;
		double v=2;
		while(a.hp>0&&hp>0){
			v=min(v*1.5,hp);
			a.onAttackedByEnergy(v,this);
			hp-=v;
		}
	}
	
	@Override
	public double gA(){return -0.03;}
	
	public BmpRes getBmp(){return game.item.EnergyIronBall.bmp;}
}
