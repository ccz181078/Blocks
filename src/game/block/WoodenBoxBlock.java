package game.block;

import util.BmpRes;
import game.item.ItemList;
import game.entity.DroppedItem;
import game.ui.*;

public class WoodenBoxBlock extends WoodenType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WoodenBoxBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	private ItemList items=null;
	public void onPlace(int x,int y){
		items=ItemList.emptyList(16);
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(items,x+0.5f,y+0.5f);
		items=null;
		super.onDestroy(x,y);
	}
	public UI getUI(BlockAt ba){
		return new UI_ItemList(-7,0,4,4,items,UI.pl.il).setBlock(ba);
	}
};
