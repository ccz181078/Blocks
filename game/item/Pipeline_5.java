package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public abstract class Pipeline_5 extends Tool implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	public double toolVal(){return 0;}
	public int maxDamage(){return 10000;}
	
	int cnt = 1 , fire = 2;
	protected static int next[]={0,4,5,1,2,3};
	protected void nextGun(){
		++damage;
		fire = 0;
		if(1<=cnt&&cnt<=5)cnt=next[cnt];
	}
	public abstract ShowableItemContainer getItems();
	public abstract void shoot(Human hu,double a,double b,Shilka ec);
	public abstract void test_shoot(Human hu,double a,double b,Shilka ec,Item ammo);
	public abstract void onUpdate(Human w,Shilka ec);
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public boolean autoUse(final Human h,final Agent a){
		Armor ar=h.armor.get();
		if(ar instanceof Shilka){
			((Shilka)ar).insert(h.getCarriedItem());
			return false;
		}
		return false;
	}
}

