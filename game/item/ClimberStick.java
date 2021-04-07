package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.entity.*;
import static util.MathUtil.*;

public class ClimberStick extends EnergyArmor{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor=new BmpRes("Armor/ClimberStick");
	public game.entity.ClimberStick ent;
	public BmpRes getBmp(){return bmp_armor;}
	public BmpRes getArmorBmp(){return null;}
	ClimberStick(){
		ent=new game.entity.ClimberStick(this);
	}
	public ClimberStick clone(){
		return (ClimberStick)util.SerializeUtil.deepCopy(this);
	}
	public void onUpdate(Human w){
		if(ent.removed){
			ent.w=w;
			ent.initPos(w.x,w.y,w.xv,w.yv,w);
			ent.add();
		}
	}
	public double getJumpAcc(Human h,double v){return 0;}
}

