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


public class EnergyBallLauncher extends EnergyLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyBallLauncher");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new EnergyBall();}
	public int energyCost(){return 5;}
	public double mv2(){return 0.01*0.7*0.7;}
	int getCd(){return 8;}
}

