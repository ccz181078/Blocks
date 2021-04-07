package game.block;

import util.BmpRes;
import game.item.ItemList;
import game.entity.DroppedItem;
import game.ui.*;
import game.entity.Entity;
import game.item.SingleItem;

public class IronBoxBlock extends IronBasedType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronBoxBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 160;}
	
	double friction(){return 0.5;}
	double frictionIn1(){return 0.5;}
	double frictionIn2(){return 0.05;}
	
	private ItemList items=null;
	public void onPlace(int x,int y){
		items=ItemList.emptyList(16);
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(items,x+0.5f,y+0.5f);
		items=null;
		super.onDestroy(x,y);
	}
	public void insert(SingleItem si){
		items.insert(si);
	}
	public SingleItem[] toArray(){
		return items.toArray();
	}
	
	public UI getUI(BlockAt ba){
		return new UI_ItemList(-7,0,4,4,items,UI.pl.il).setBlock(ba);
	}
	public game.item.SingleItem[] getItems(){return items.toArray();}
	public boolean isDeep(){return true;}
};
