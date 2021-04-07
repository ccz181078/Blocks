package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.CraftInfo;
import game.item.Craft;

public class BlockMakerBlock extends IronBasedType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/BlockMakerBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 120;}
	public UI getUI(BlockAt ba){
		return new UI_Craft(Craft.get(Craft.craft_normal_block,CraftInfo._block),-7).setBlock(ba);
	}
	public int getCraftType(){return CraftInfo._block;}
};
