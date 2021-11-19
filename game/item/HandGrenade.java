package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;

public class HandGrenade extends ThrowableItem implements DefaultItemContainer{
	static BmpRes bmp[]=BmpRes.load("Item/HandGrenade_",2);
	public BmpRes getBmp(){return bmp[warhead.isEmpty()?0:1];}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public ShowableItemContainer getItems(){return warhead;}

	public Item clone(){
		HandGrenade i1=new HandGrenade();
		Item i2=warhead.popItem();
		i1.warhead.insert(i2);
		return i1;
	}
	
	@Override
	public boolean cmpType(Item item){
		if(item.getClass()==getClass()){
			return ((HandGrenade)item).warhead.isEmpty() && warhead.isEmpty();
		}
		return false;
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return autoInsertAndUse(h,a,warhead);
	}

	public int maxAmount(){return warhead.isEmpty()?1:1;}
	public NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	
	@Override
	protected Entity toEnt(){
		return new game.entity.HandGrenade(this);
	}
	@Override
	int energyCost(){return 10;}
};
class HandGrenadeSlow extends HandGrenade{
	static BmpRes bmp[]=BmpRes.load("Item/HandGrenadeSlow_",2);
	public BmpRes getBmp(){return bmp[warhead.isEmpty()?0:1];}
	
	public Item clone(){
		HandGrenadeSlow i1=new HandGrenadeSlow();
		Item i2=warhead.popItem();
		i1.warhead.insert(i2);
		return i1;
	}
	
	@Override
	protected Entity toEnt(){
		return new game.entity.HandGrenadeSlow(this);
	}
}