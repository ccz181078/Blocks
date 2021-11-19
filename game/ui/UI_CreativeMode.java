package game.ui;

import game.entity.Player;
import graphics.Canvas;
import game.item.Item;
import game.item.ShowableItemContainer;
import static game.item.Craft.*;
import game.item.ItemList_CreativeMode;
import java.util.function.*;

class UI_SubPage extends UI_MultiPage{
	UI_SubPage(Item[][]is,Function<Item[],ShowableItemContainer> f){
		x=1;
		for(Item[]items:is)addPage(items[0],new UI_ItemList(f.apply(items),pl.il,3));
	}
	@Override
	protected boolean onTouch(float tx,float ty,int tp){
		if(tx>3)return false;
		return super.onTouch(tx,ty,tp);
	}
	protected void onDraw(Canvas cv){
		cv.drawRect(0,0,3,8,0xffcccccc);
		super.onDraw(cv);
	}
}

class UI_AllItem extends UI_MultiPage{
	public UI_AllItem(Function<Item[],ShowableItemContainer> f){
		super();
		x=-7;y=0;
		addPage(new game.item.Iron(),new UI_SubPage(_normal_items,f));
		addPage(new game.item.StoneBall(),new UI_SubPage(_throwable_items,f));
		addPage(new game.item.AgentMaker(game.entity.GreenMonster.class),new UI_ItemList(f.apply(_agent),pl.il));
		addPage(new game.item.StoneHammer(),new UI_SubPage(_normal_tools,f));
		addPage(new game.item.EnergyDrill(),new UI_SubPage(_energy_tools,f));
		addPage(new game.block.EnergyStoneBlock(),new UI_ItemList(f.apply(_circuit),pl.il));
		addPage(new game.block.StoneBlock(),new UI_SubPage(_normal_blocks,f));
		addPage(new game.block.WoodenBoxBlock(),new UI_SubPage(_functional_blocks,f));
	}
	protected void onDraw(Canvas cv){
		cv.drawRect(0,0,4,8,0xffaaaaaa);
		super.onDraw(cv);
	}
}

public class UI_CreativeMode extends UI_AllItem{
	public UI_CreativeMode(){
		super(x->new ItemList_CreativeMode(x));
	}
}