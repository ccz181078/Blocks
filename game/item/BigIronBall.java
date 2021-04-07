package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;

public class BigIronBall extends StoneBall{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/IronBall");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	@Override
	public int maxAmount(){return 4;}
	protected Entity toEnt(){
		return new game.entity.BigIronBall();
	}
};
