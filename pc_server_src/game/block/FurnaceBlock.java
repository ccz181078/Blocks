package game.block;

import util.BmpRes;
import game.entity.Agent;
import game.item.*;
import game.world.World;
import game.entity.DroppedItem;
import game.entity.Player;
import game.ui.*;
import static util.MathUtil.*;

public class FurnaceBlock extends StoneBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/FurnaceBlock_",4);
	private int burning;
	private ShowableItemContainer fuel,items;
	public BmpRes getBmp(){return bmp[burning>0?rndi(1,3):0];}
	int maxDamage(){return 80;}
	public boolean isDeep(){return true;}
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){
			{
				addPage(new game.item.Coal(),new UI_ItemList(0,3,4,1,fuel,pl.il));
				addPage(new game.item.IronOre(),new UI_ItemList(0,0.5f,4,1,items,pl.il));
				addPage(new game.item.IronPickax(),new UI_Craft(Craft.getAllEq(CraftInfo._heat)));
			}
		}.setBlock(ba);
	}
	public int getCraftType(){return burning>0?CraftInfo._heat:0;}
	public void onPlace(int x,int y){
		fuel=ItemList.emptyList(4);
		items=ItemList.emptyNonOverlapList(4);
	}
	public void onFireUp(int x,int y){
		if(burning==0){
			addFuel();
			if(burning>0)World.cur.checkBlock(x,y);
		}
	}
	private void addFuel(){
		for(SingleItem si:fuel.toArray())if(!si.isEmpty()){
			int v=si.get().fuelVal();
			if(v>0){
				si.dec();
				burning+=v*2;
				return;
			}
		}
	}
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(burning>0){
			for(SingleItem si:items.toArray())if(!si.isEmpty()){
				Item w=si.get();
				if(w.heatingTime(true)*rnd()<1)si.set(w.heatingProduct(true));
			}
			--burning;
			if(burning==0)addFuel();
			if(burning>0)World.cur.checkBlock(x,y);
		}
		return false;
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(fuel,x+0.5,y+0.2);
		fuel=null;
		DroppedItem.dropItems(items,x+0.5,y+0.7);
		items=null;
		super.onDestroy(x,y);
	}
};
