package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class RPG_Serial extends RPGItem implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Serial");
	public BmpRes getBmp(){return bmp;}
	public NonOverlapSpecialItem<Warhead> wh1=new NonOverlapSpecialItem<>(Warhead.class);
	public NonOverlapSpecialItem<Warhead> wh2=new NonOverlapSpecialItem<>(Warhead.class);
	public ShowableItemContainer getItems(){return ItemList.create(wh1,wh2);}
	@Override
	public int maxAmount(){return 1;}
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Serial(this);
	}
};
