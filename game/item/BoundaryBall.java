package game.item;

import util.BmpRes;
import game.entity.*;

public class BoundaryBall extends StoneBall implements DefaultItemContainer{
	NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public int maxAmount(){return 1;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public ShowableItemContainer getItems(){return warhead;}
	public static BmpRes bmp=new BmpRes("Entity/BoundaryBall");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.BoundaryBall(warhead);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return autoInsertAndUse(h,a,warhead);
	}
};

