package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public abstract class AirshipFlank extends Item implements AttackFilter{
private static final long serialVersionUID=1844677L;
	public int maxAmount(){return 1;}
	public AirshipFlank(){
		ent=new Airship_Flank(this);
	}
	public Airship_Flank ent;
	public Item clickAt(double x,double y,Agent a){
		if(max(abs(x-a.x),abs(y-a.y))>4)return this;
		ent.dir=a.dir;
		ent.initPos(x,y,0,0,a);
		if(ent.cadd())return null;
		return this;
	}
	public boolean useInArmor(){return true;}
	public AirshipFlank clone(){
		return (AirshipFlank)util.SerializeUtil.deepCopy(this);
	}
	public void drawInfo(graphics.Canvas cv){
		if(ent.hp/ent.maxHp()<1-1e-4)game.ui.UI.drawProgressBar(cv,0xff00ff00,0xff007f00,(float)(ent.hp/maxHp()),-0.4f,-0.4f,0.4f,-0.33f);
	}
	public abstract double maxHp();
	public abstract double mass();
	public abstract Attack transform(Attack a);
	public void onBroken(){}
	public void update(Human hu){}
	public double onImpact(double v){return max(0,v-100)*0.01;}
	public boolean autoUse(final Human h,final Agent a){
		Armor ar=h.armor.get();
		if(ar instanceof Airship){
			((Airship)ar).insert(h.getCarriedItem());
			return false;
		}
		return false;
	}
}

class IronAirshipFlank extends AirshipFlank{
private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/IronAirshipFlank_",4);
	public BmpRes getBmp(){
		int tp=max(0,min(3,(int)((maxHp()-ent.hp)/maxHp()*4)));
		return bmp_armor[tp];
	}
	public double maxHp(){return 60000;}
	public double mass(){return 5;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(a instanceof EnergyAttack){
			a.val*=1;
		}else if(a instanceof FireAttack){
			a.val*=2;
		}
		return a;
	}
}

class GlassAirshipFlank extends AirshipFlank{
private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/GlassAirshipFlank_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double maxHp(){return 15000;}
	public double mass(){return 2f;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(a instanceof EnergyAttack){
			return null;
		}else if(a instanceof FireAttack){
			a.val*=0.1;
		}else if(a instanceof NormalAttack){
			a.val*=10;
		}
		return a;
	}
	@Override
	public double onImpact(double v){return v*10;}
}
class WoodenAirshipFlank extends AirshipFlank{
private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/WoodenAirshipFlank_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public double maxHp(){return 15000;}
	public double mass(){return 2f;}
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(a instanceof EnergyAttack){
			a.val*=0.5;
		}else if(a instanceof FireAttack){
			a.val*=10;
		}else if(a instanceof NormalAttack){
			a.val*=2;
		}
		return a;
	}
}
class EnergyStoneAirshipFlank extends AirshipFlank{
private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/EnergyStoneAirshipFlank_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public double maxHp(){return 15000;}
	public double mass(){return 3;}
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(a instanceof EnergyAttack)return null;
		a.val*=0.2;
		return a;
	}
	public void onBroken(){
		ShockWave.explode(ent.x,ent.y,ent.xv,ent.yv,90,0.1,8,ent);
		super.onBroken();
	}
}