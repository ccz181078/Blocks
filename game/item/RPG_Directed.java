package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;

public class RPG_Directed extends RPGItem implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/RPG_Directed");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 1;}
	
	public NonOverlapSpecialItem<Warhead> warhead=new NonOverlapSpecialItem<>(Warhead.class,2);
	public ShowableItemContainer getItems(){return warhead;}
	
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Directed(this);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(warhead.isEmpty())
		for(SingleItem si:h.items.toArray()){
			warhead.insert(si);
		}
		return false;
	}
};
