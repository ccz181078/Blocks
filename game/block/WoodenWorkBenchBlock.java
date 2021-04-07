package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.Craft;
import game.item.CraftInfo;
import static game.item.Craft.*;
import static java.lang.Math.*;

public class WoodenWorkBenchBlock extends WoodenBlock implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WoodenWorkBenchBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 60;}
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){{
				int tp=WoodenWorkBenchBlock.this.getCraftType();
				addPage(new game.item.Stick(),new UI_Craft(Craft.get(craft_normal_item,tp)));
				addPage(new game.item.StoneBall(),new UI_Craft(Craft.get(craft_throwable_item,tp)));
				addPage(new game.block.StoneBlock(),new UI_Craft(Craft.get(craft_normal_block,tp)));
				addPage(new game.block.WoodenBoxBlock(),new UI_Craft(Craft.get(craft_functional_block,tp)));
				addPage(new game.item.StoneHammer(),new UI_Craft(Craft.get(craft_normal_tool,tp)));
			}
		}.setBlock(ba);
	}
	public int getCraftType(){return CraftInfo._cut|CraftInfo._complex;}
}
