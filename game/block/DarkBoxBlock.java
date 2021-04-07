package game.block;

import util.BmpRes;
import game.item.ItemList;
import game.entity.DroppedItem;
import game.ui.*;
import game.item.Item;
import static util.MathUtil.*;

public class DarkBoxBlock extends StoneType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DarkBoxBlock");
	public BmpRes getBmp(){return bmp;}
	int cnt=0;
	int maxDamage(){return 1;}
	protected void des(int x,int y,int v){if(rnd()*20<v)super.des(x,y,1);}
	private ItemList items=null;
	public DarkBoxBlock(){}
	public void onPlace(int x,int y){
		super.onPlace(x,y);
		if(cnt++==0){
			items=ItemList.emptyList(16);
		}
	}
	public void onDestroy(int x,int y){
		super.onDestroy(x,y);
		if(0==--cnt){
			DroppedItem.dropItems(items,x+0.5,y+0.5);
			items=null;
		}
	}
	
	public int maxAmount(){return 2;}
	public boolean cmpType(Item b){return this==b;}
	public Block clone(){return this;}
	
	
	public UI getUI(BlockAt ba){
		return new UI_ItemList(-7,0,4,4,items,UI.pl.il).setBlock(ba);
	}
	public game.item.SingleItem[] getItems(){return items.toArray();}
	public boolean isDeep(){return true;}
};
