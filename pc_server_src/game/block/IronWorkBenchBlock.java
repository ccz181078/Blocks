package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.Craft;
import game.item.CraftInfo;
import static game.item.Craft.*;

public class IronWorkBenchBlock extends StoneType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronWorkBenchBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 240;}
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){
			{
				int tp=IronWorkBenchBlock.this.getCraftType();
				addPage(new game.item.DiamondDrill(),new UI_Craft(Craft.get(craft_normal_item,tp)));
				addPage(new game.item.DiamondHammer(),new UI_Craft(Craft.get(craft_normal_tool,tp)));
				addPage(new game.item.EnergyMotor(),new UI_Craft(Craft.get(craft_energy_tool,tp)));
				addPage(new game.block.PulverizerBlock(),new UI_Craft(Craft.get(craft_functional_block,tp)));
				addPage(new game.item.EnergyBlocker(),new UI_Craft(Craft.get(craft_circuit,tp)));
			}
		}.setBlock(ba);
	}
	public int getCraftType(){return CraftInfo._cut|CraftInfo._complex|CraftInfo._diamond|CraftInfo._energy;}
}
