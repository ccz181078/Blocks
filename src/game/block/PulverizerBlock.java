package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import game.entity.DroppedItem;

public class PulverizerBlock extends CraftHelperBlock implements BlockWithUI,EnergyProvider{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/PulverizerBlock_",2);
	public BmpRes getBmp(){
		if(ch==null||ch.free())return bmp[0];
		return bmp[(int)game.world.World._.time%2];
	}
	int maxDamage(){return 200;}
	
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){
			{
				addPage(new EnergyCell(),new UI_ItemList(ec,pl.il));
				addPage(new game.item.CarbonPowder(),new UI_Craft(Craft.getAllEq(CraftInfo._pulverize)));
			}
			public int getCraftType(){return ch.free()?CraftInfo._pulverize:0;}
		}.setBlock(ba).setCraftHelper(ch);
	}
};
