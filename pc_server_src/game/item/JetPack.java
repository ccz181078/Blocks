package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import static util.MathUtil.*;

public class JetPack extends IronArmor implements EnergyReceiver{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/JetPack");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/JetPack_",2);
	transient boolean using;
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor[using?1:0];}

	public void onUpdate(Human w){
		using=false;
		if(!hasEnergy(1))return;
		double c=0;
		if(w.xdir!=0){
			w.xa+=w.xdir*0.02;
			c+=0.02;
		}
		if(w.ydir!=0){
			w.ya+=w.ydir*0.04;
			c+=0.04;
		}
		if(c>0){
			using=true;
			if(rnd()<c){
				loseEnergy(1);
				if(rnd()<0.3)++damage;
			}
		}
	}
	

	/*extends EnergyTool*/
	public BmpRes getUseBmp(){return use_btn;}
	SpecialItem<EnergyCell> ec=new SpecialItem<EnergyCell>(EnergyCell.class);
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
		new Iron().drop(x,y,util.MathUtil.rndi(4,6));
		DroppedItem.dropItems(getItems(),x,y);
	}
}

