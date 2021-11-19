package game.item;

import util.BmpRes;
import game.world.World;
import game.block.Block;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;


public abstract class BasicBall extends LaunchableItem{
private static final long serialVersionUID=1844677L;
	abstract game.entity.Entity getBall();
	protected Entity toEnt(){return getBall();}
	public double launchValue(){return 100;}
};