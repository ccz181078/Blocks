package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import static util.MathUtil.*;

public class FloatingPack extends JetPack{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/FloatingPack");
	private static BmpRes bmp_armor=new BmpRes("Armor/FloatingPack");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp_armor;}

	public void onUpdate(Human w){
		using=false;
		if(!hasEnergy(1))return;
		double c=0.02;
		if(w.xdir!=0){
			c+=0.02;
		}
		if(w.ydir!=0){
			c+=0.02;
		}
		if(c>0){
			using=true;
			w.climbable=true;
			w.f+=0.04;
			if(rnd()<c){
				loseEnergy(1);
				if(rnd()<0.3)++damage;
			}
		}
	}

	public void onBroken(double x,double y){
		new EnergyStone().drop(x,y,rndi(1,2));
		new Iron().drop(x,y,rndi(4,6));
		super.onBroken(x,y);
	}
}

