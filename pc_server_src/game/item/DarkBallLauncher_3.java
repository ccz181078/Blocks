package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import game.entity.Entity;
import game.entity.DarkBall;

public class DarkBallLauncher_3 extends EnergyBallLauncher_3{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkBallLauncher_3");
	public BmpRes getBmp(){return bmp;}
	public Entity getBall(){return new DarkBall();}
}

