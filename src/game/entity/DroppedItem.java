package game.entity;

import static util.MathUtil.*;
import game.item.SingleItem;
import graphics.Canvas;
import game.item.Item;
import game.item.ItemContainer;

public final class DroppedItem extends Entity{
	private static final long serialVersionUID=1844677L;
	SingleItem item;
	public DroppedItem(double _x,double _y,SingleItem _item){
		x=_x+rnd(-0.01,0.01);
		y=_y+rnd(-0.01,0.01);
		xv=rnd(-0.05,0.05);
		yv=rnd(-0.05,0.05);
		item=_item;
		hp=10;
	}
	public static void dropItems(SingleItem si[],double _x,double _y){
		if(si!=null);
		for(SingleItem s:si)
			new DroppedItem(_x,_y,s).add();
	}
	public static void dropItems(ItemContainer ic,double _x,double _y){
		if(ic!=null)dropItems(ic.toArray(),_x,_y);
	}
	public double width(){return 0.3f;}
	public double height(){return 0.3f;}
	public DroppedItem(int _x,int _y,SingleItem _item){
		this(_x+rnd(0.3,0.7),_y+rnd(0.3,0.7),_item);
	}
	public void update(){
		super.update();
		hp-=0.01f;
	}
	void touchAgent(Agent a){
		if(a instanceof Human){
			((Human)a).getItems().insert(item);
			if(item.isEmpty())remove();
		}
	}
	public void draw(Canvas cv){
		Item it=item.get();
		if(it!=null)it.getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
};
