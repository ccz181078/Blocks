package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.Block;
import game.entity.*;

public class RPG_Item extends RPG_Guided implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RPG_Item");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 1;}
	
	public NonOverlapSpecialItem<Item> item=new NonOverlapSpecialItem<>(Item.class);
	public ShowableItemContainer getItems(){return item;}
	
	@Override
	protected Entity toEnt(){
		return new game.entity.RPG_Item(this);
	}
};
