package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class Glider extends StoneBall implements DefaultItemContainer{
	public static BmpRes bmp[]=BmpRes.load("Item/Glider_",2);
	public BmpRes getBmp(){return bmp[rev?0:1];}
	public double hardness(){return game.entity.NormalAttacker.PAPER;}
	public boolean rev;
	@Override
	protected Entity toEnt(){
		return new game.entity.Glider(this);
	}
	@Override
	public BmpRes getUseBmp(){return rotate_btn;}
	@Override
	int energyCost(){return 1;}
	@Override
	public void onCarried(Agent a){
		rev=(a.dir==1);
		super.onCarried(a);
	}
	public Item clone(){
		Glider i1=new Glider();
		Item i2=warhead.popItem();
		i1.warhead.insert(i2);
		return i1;
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return autoInsertAndUse(h,a,warhead);
	}
	public NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	public ShowableItemContainer getItems(){return warhead;}
	public int maxAmount(){return warhead.isEmpty()?1:1;}
};
