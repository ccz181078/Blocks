package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;

public final class HandGrenade extends ThrowableItem implements DefaultItemContainer{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Item/HandGrenade_",2);
	public BmpRes getBmp(){return bmp[warhead.isEmpty()?0:1];}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public BmpRes getUseBmp(){return use_btn;}
	public void onUse(Human a){
		if(!a.items.getSelected().isEmpty())return;
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,getItems()));
		}
	}
	public ShowableItemContainer getItems(){return warhead;}

	public Item clone(){//浅拷贝，可叠加的物品不应有复杂的内部结构
		HandGrenade i1=new HandGrenade();
		Item i2=warhead.popItem();
		i1.warhead.insert(i2);
		return i1;
	}
	
	@Override
	public boolean cmpType(Item item){
		if(item.getClass()==HandGrenade.class){
			return ((HandGrenade)item).warhead.isEmpty() && warhead.isEmpty();
		}
		return false;
	}

	@Override
	public void insert(SingleItem it){getItems().insert(it);}
	@Override
	public SingleItem[] toArray(){return getItems().toArray();}
	
	public int maxAmount(){return warhead.isEmpty()?16:1;}
	public NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<Warhead>(Warhead.class);
	
	@Override
	protected Entity toEnt(){
		return new game.entity.HandGrenade(this);
	}
	@Override
	int energyCost(){return 10;}
};
