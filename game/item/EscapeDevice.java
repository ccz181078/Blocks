package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class EscapeDevice extends SpringShoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp[]=BmpRes.load("Item/EscapeDevice_",2);
	public BmpRes getBmp(){return bmp[state==-1?0:1];}
	public int maxDamage(){return 2000;}
	
	public int state=-1;
	
	@Override
	public double onImpact(Human h,double v){
		damage+=rf2i(v);
		return max(0,(v-10)*0.02);
	}
	
	@Override
	public Shoes update(Human h){
		if(state==-1){
			Armor ar=h.armor.get();
			if(ar!=null&&(ar.damage*1.0/ar.maxDamage()>0.9||h.hp<h.maxHp()*0.25)){
				double h0=h.height();
				ar=h.armor.popItem();
				ar.drop(h.x,h.y);
				h0=h.height()-h0;
				new SetRelPos(h,h,0,h0);
				state=10;
			}
		}else if(state>0){
			h.xa+=0.03*h.xdir;
			h.ya+=0.06;
			--state;
		}else{
			h.f+=0.03;
		}
		return super.update(h);
	}
	
	@Override
	protected double getJumpAcc(){
		return (super.getJumpAcc()-0.4)*0.5+0.4;
	}

};
