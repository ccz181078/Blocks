package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import game.entity.DroppedItem;

public class ItemAbsorberBlock extends EnergyMachineBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 200;}
	public UI getUI(BlockAt ba){
		return new UI_ItemList(-7,0,4,4,ec,UI.pl.il).setBlock(ba);
	}
};
