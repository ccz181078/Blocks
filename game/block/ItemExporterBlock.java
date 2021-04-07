package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import game.entity.DroppedItem;
import game.world.World;

public class ItemExporterBlock extends IronBasedType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 40;}
	public boolean isSolid(){return false;}
	NonOverlapItem it;
	public void onPlace(int x,int y){
		it=new NonOverlapItem();
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		Block b=World.cur.get(x,y+1).rootBlock();
		SingleItem si[]=b.getItems();
		Item i0=it.get();
		for(SingleItem s:si){
			Item i1=s.get();
			if(i1!=null&&(i0==null||i0.cmpType(i1))){
				s.popItem().drop(x+0.5,y);
				return false;
			}
		}
		return false;
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(it,x+0.5,y+0.5);
		it=null;
		super.onDestroy(x,y);
	}
	public UI getUI(BlockAt ba){
		return new UI_ItemList(-7,0,4,4,it,UI.pl.il).setBlock(ba);
	}
	public boolean isDeep(){return true;}
};
