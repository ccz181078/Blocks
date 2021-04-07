package game.item;

import util.BmpRes;
import game.entity.Entity;

public class BigGuidedBullet extends GuidedBullet implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BigGuidedBullet");
	public BmpRes getBmp(){return bmp;}
	public int maxAmount(){return 1;}
	
	@Override
	protected Entity toEnt(){return new game.entity.BigGuidedBullet(this);}

	public ItemList items=ItemList.emptyNonOverlapList(2);
	public ShowableItemContainer getItems(){return items;}
};
