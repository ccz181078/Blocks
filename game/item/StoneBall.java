package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class StoneBall extends ThrowableItem{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/StoneBall");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	@Override
	protected Entity toEnt(){
		return new game.entity.StoneBall();
	}
	@Override
	int energyCost(){return 10;}
};
