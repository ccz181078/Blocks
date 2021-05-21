package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.world.World;
import game.block.Block;


public class PureEnergyBall extends Ball{
	private static final long serialVersionUID=1844677L;
	public double mass(){return 0.01*(1+sqrt(max(1e-8,hp/10)));}
	public double radius(){
		return min(1,sqrt(max(1e-8,hp/10))*0.2);
	}
	public boolean isPureEnergy(){return true;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public double fluidResistance(){return 0;}
	public boolean chkEnt(){return false;}
	public boolean chkBlock(){return false;}
	@Override
	public double light(){
		return 0.5;
	}
	public void update(){
		super.update();
		checkSlow();
	}
	protected void checkSlow(){
		hp-=0.04/(hypot(xv,yv)+0.01);
	}
	double _fc(){return 0;}
	double friction(){return 0.01;}
	@Override
	protected boolean useRandomWalk(){return false;}
	@Override
	public AttackFilter getAttackFilter(){return special_filter;}//攻击过滤器
}