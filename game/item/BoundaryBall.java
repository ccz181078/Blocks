package game.item;

import util.BmpRes;
import game.entity.*;

public class BoundaryBall extends StoneBall implements DefaultItemContainer{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Entity/BoundaryBall");
	NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public int maxAmount(){return 1;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public BmpRes getBmp(){return bmp;}
	public ShowableItemContainer getItems(){return warhead;}
	public Entity toEnt(){
		return new game.entity.BoundaryBall(warhead);
	}
};
