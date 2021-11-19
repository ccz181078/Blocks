package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class DarkRPG extends RPGItem implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkRPG");
	public BmpRes getBmp(){return bmp;}
	public NonOverlapSpecialItem<Warhead> wh=new NonOverlapSpecialItem<>(Warhead.class);
	public ShowableItemContainer getItems(){return wh;}
	@Override
	public int maxAmount(){return 1;}
	@Override
	protected Entity toEnt(){
		return new game.entity.DarkRPG(this);
	}
};
