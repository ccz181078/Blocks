package game.item;

import util.BmpRes;
import game.entity.*;

public class Bow extends Tool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Bow");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public int maxDamage(){return 100;}
	public void onBroken(double x,double y){}
	SpecialItem<Arrow> arrow=new SpecialItem<Arrow>(Arrow.class);
	public Item clickAt(double x,double y,Agent a){
		if(!(a instanceof Human))return this;
		Human w=(Human)a;
		y-=w.y;
		x-=w.x;
		if(x*w.dir<=w.width()||w.xp<4||arrow.isEmpty())return this;
		w.xp-=4;
		++damage;
		w.throwEnt(new game.entity.Arrow(arrow.popItem(),this),y/x,shootSpeed());
		return this;
	}
	public BmpRes getUseBmp(){return use_btn;}
	public void onUse(Human a){
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,arrow));
		}
	}
	public double shootSpeed(){return 0.22;}
};
