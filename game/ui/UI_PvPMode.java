package game.ui;

import game.entity.Player;
import graphics.Canvas;
import static game.item.Craft.*;
import game.item.ItemList_PvPMode;

public class UI_PvPMode extends UI_MultiPage{
	private static final long serialVersionUID=1844677L;
	public UI_PvPMode(){
		super();
		x=-7;y=0;
		addPage(new game.item.Iron(),new UI_ItemList(new ItemList_PvPMode(_normal_item),pl.il));
		addPage(new game.item.StoneBall(),new UI_ItemList(new ItemList_PvPMode(_throwable_item),pl.il));
		addPage(new game.item.AgentMaker(game.entity.GreenMonster.class),new UI_ItemList(new ItemList_PvPMode(_agent),pl.il));
		addPage(new game.item.StoneHammer(),new UI_ItemList(new ItemList_PvPMode(_normal_tool),pl.il));
		addPage(new game.item.EnergyDrill(),new UI_ItemList(new ItemList_PvPMode(_energy_tool),pl.il));
		addPage(new game.block.EnergyStoneBlock(),new UI_ItemList(new ItemList_PvPMode(_circuit),pl.il));
		addPage(new game.block.StoneBlock(),new UI_ItemList(new ItemList_PvPMode(_normal_block),pl.il));
		addPage(new game.block.WoodenBoxBlock(),new UI_ItemList(new ItemList_PvPMode(_functional_block),pl.il));
	}
	protected void onDraw(Canvas cv){
		cv.drawRect(0,0,4,8,0xffaaaaaa);
		super.onDraw(cv);
	}
}

