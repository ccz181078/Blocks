package game.item;

import graphics.Canvas;
import game.entity.Human;
import game.entity.Player;
import util.BmpRes;
import game.entity.DroppedItem;

public abstract class EnergyTool extends Tool implements EnergyReceiver{
	private static final long serialVersionUID=1844677L;
	SpecialItem<EnergyCell> ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	public double toolVal(){return 0;}
	public BmpRes getUseBmp(){return use_btn;}
	public void onUse(Human a){
		a.items.getSelected().insert(this);
		if((a instanceof Player)){
			((Player)a).openDialog(new game.ui.UI_Item(this,getItems()));
		}
	}
	public ShowableItemContainer getItems(){return ec;}

	public void gainEnergy(int v){
		EnergyCell e=ec.get();
		if(e!=null)e.gainEnergy(v);
	}
	public int resCap(){
		EnergyCell e=ec.get();
		if(e!=null)return e.resCap();
		return 0;
	}
	
	boolean hasEnergy(int x){
		EnergyCell e=ec.get();
		if(e==null)return false;
		return e.energy>=x;
	}
	void loseEnergy(int x){
		ec.get().energy-=x;
	}
	public void drawInfo(Canvas cv){
		super.drawInfo(cv);
		EnergyCell e=ec.get();
		if(e!=null)game.ui.UI.drawProgressBar(cv,0xff00ffff,0xff007f7f,e.energy*1f/e.maxEnergy(),-0.4f,-0.3f,0.4f,-0.23f);
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(1,3));
		DroppedItem.dropItems(getItems(),x,y);
	}
}
