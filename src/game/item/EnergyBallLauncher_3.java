package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Agent;
import game.entity.Human;
import game.entity.Entity;
import game.entity.EnergyBall;

public class EnergyBallLauncher_3 extends EnergyBallLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyBallLauncher_3");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public Entity getBall(){return new EnergyBall();}
	public void shoot(double s,Launcher w){
		s=max(-0.8,min(0.8,s));
		for(int i=-1;i<=1&&shootCond();++i)super.shoot(s+i*0.2,w);
	}
}

