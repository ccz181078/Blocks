package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class FastAirshipFlank extends IronAirshipFlank{
private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/FastAirshipFlank_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double maxHp(){return 8000;}
	public void update(Human hu){
		Armor ar=hu.armor.get();
		if(!(ar instanceof Airship))return;
		Airship as=(Airship)ar;
		if(as.hasEnergy(2)&&(hu.xdir!=0||hu.ydir!=0)){
			ent.impulse(hu.xdir,hu.ydir,0.05);
			as.loseEnergy(2);
		}
	}
}