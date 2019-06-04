package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.entity.Entity;
import game.entity.FireBall;

public class FireBallLauncher extends EnergyBallLauncher{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireBallLauncher");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new FireBall();}
}

