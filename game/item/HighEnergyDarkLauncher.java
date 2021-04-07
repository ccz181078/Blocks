package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.entity.Entity;

public class HighEnergyDarkLauncher extends EnergyBallLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/HighEnergyDarkLauncher");
	public BmpRes getBmp(){return bmp;}
	public int energyCost(){return super.energyCost()*5;}
	public Entity getBall(){return new game.entity.HE_DarkBall();}
}

