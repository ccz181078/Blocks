package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class BurningCoalBall extends StoneBall{
	public static BmpRes bmp=new BmpRes("Item/BurningCoalBall");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	@Override
	protected Entity toEnt(){
		return new game.entity.BurningCoalBall();
	}
	@Override
	int energyCost(){return 10;}
};
