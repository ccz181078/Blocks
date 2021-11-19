package game.ui;

import game.item.ShowableItemContainer;
import game.item.Item;
import graphics.Canvas;
import game.item.SingleItem;

public class UI_Item extends UI_ItemList{
	private static final long serialVersionUID=1844677L;
	//默认的依赖于随身物品的物品列表界面
	
	Item it;
	public UI_Item(Item _it,ShowableItemContainer ic){
		super(-7,0,4,4,ic,pl.il);
		it=_it;
	}
	public boolean exist(){
		for(SingleItem s:pl.items.toArray()){
			if(s.get()==it)return true;
		}
		for(SingleItem s:pl.bag_items.toArray()){
			if(s.get()==it)return true;
		}
		if(pl.armor.get()==it)return true;
		if(pl.shoes.get()==it)return true;
		return false;
	}
	protected void onDraw(Canvas cv){
		SingleItem ic=it.setAmount(1);
		cv.save();
		cv.scale(4f,4f);
		cv.translate(.5f,.5f);
		ic.draw(cv,ic,0);
		cv.restore();
		super.onDraw(cv);
	}
}
