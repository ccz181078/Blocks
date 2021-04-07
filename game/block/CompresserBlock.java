package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import game.entity.DroppedItem;

public class CompresserBlock extends CraftHelperBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/CompresserBlock_",4);
	public BmpRes getBmp(){
		if(ch==null||ch.free())return bmp[0];
		int t=(int)(game.world.World.cur.time/2%8);
		if(t>=4)t=7-t;
		return bmp[t];
	}
	int maxDamage(){return 200;}
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){{
				addPage(new EnergyCell(),new UI_ItemList(ec,pl.il));
				addPage(new game.item.Diamond(),new UI_Craft(Craft.getAllEq(CraftInfo._compress)));
			}
		}.setBlock(ba).setCraftHelper(ch);
	}
	public int getCraftType(){return ch.free()?CraftInfo._compress:0;}
};
