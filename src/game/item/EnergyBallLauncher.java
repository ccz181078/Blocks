package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Agent;
import game.entity.Human;
import game.entity.Entity;
import game.entity.EnergyBall;
import game.entity.DroppedItem;

public class EnergyBallLauncher extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyBallLauncher");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public void onDesBlock(Block b){}
	public void onAttack(Agent a){}
	public Entity getBall(){return new EnergyBall();}
	public int energyCost(){return 5;}
	double speed(){return 0.2;}
	public Item clickAt(double x,double y,Agent a){
		if(!(a instanceof Human))return this;
		Human w=(Human)a;
		y-=w.y;
		x-=w.x;
		if(x*w.dir<=w.width())return this;
		if(shootCond())shoot(y/x,w);
		return this;
	}
	public boolean shootCond(){
		return hasEnergy(energyCost());
	}
	public void shoot(double s,Launcher w){
		loseEnergy(energyCost());
		++damage;
		s=max(-1,min(s,1));
		w.launch(getBall(),s,speed());
	}
}

