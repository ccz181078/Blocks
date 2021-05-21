package game.item;

import util.BmpRes;
import static util.MathUtil.rnd;
import game.entity.*;

public class CactusHammer extends HammerType{
	static BmpRes bmp=new BmpRes("Item/CactusHammer");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 1;}
	public int swordVal(){return 3;}
	public int maxDamage(){return 500;}
	public int fuelVal(){return 30;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
	public void onBroken(double x,double y){
		if(rnd()<0.8)new Stick().drop(x,y);
		super.onBroken(x,y);
	}
	
	public int foodVal(){return 12;}
	public int eatTime(){return 150;}
	public void using(UsingItem ent){
		if(ent.hp>120){
			Human hu=(Human)ent.ent;
			hu.loseHp(0.3,SourceTool.item(hu,this));
		}
		super.using(ent);
	}

}

