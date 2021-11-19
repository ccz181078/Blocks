package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class Balloon extends StoneBall implements DefaultItemContainer{
	public static BmpRes bmp=new BmpRes("Item/Balloon");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.PAPER;}
	@Override
	protected Entity toEnt(){
		return new game.entity.Balloon(this);
	}
	@Override
	int energyCost(){return 1;}
	public Item clone(){
		Balloon i1=new Balloon();
		Item i2=warhead.popItem();
		i1.warhead.insert(i2);
		return i1;
	}
	public NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public ShowableItemContainer getItems(){return warhead;}
	public int maxAmount(){return warhead.isEmpty()?16:1;}
};
