package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import static util.MathUtil.*;

public class JetPack extends EnergyArmor{
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
}

