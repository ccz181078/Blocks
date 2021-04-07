package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class TeleportationSquare extends Tool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Item/TeleportationSquare_",2);
	public BmpRes getBmp(){return bmp[damage==0?0:1];}
	protected double toolVal(){return 0;}
	public int maxDamage(){return 150;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	@Override
	public BmpRes getUseBmp(){return field_btn;}
	@Override
	public void onUse(Human a){
		a.getCarriedItem().insert(this);
		if(!a.teleporting&&damage==0){
			a.teleporting=true;
			new TeleportationEvent(a,this);
		}
	}
	public boolean autoUse(Human h,Agent a){
		if(a==null){
			h.useCarriedItem();
			return true;
		}
		return false;
	}
	public void onDesBlock(Block b){}
	public void onAttack(Entity a,Source src){}
}

class TeleportationEvent extends Event{
	private static final long serialVersionUID=1844677L;
	Agent target;
	TeleportationSquare item;
	int time;
	TeleportationEvent(Agent a,TeleportationSquare item){
		target=a;
		this.item=item;
		item.damage=1;
		time=150;
		add(1);
	}
	public void run(){
		new SetRelPos(target,target,target.xdir*0.2,target.ydir*0.2);
		--time;
		++item.damage;
		if(time<0||!target.teleporting){
			item.damage+=152;
			target.teleporting=false;
			target.xv=target.yv=0;
			target.xa=target.ya=0;
			target.xv0=target.yv0=0;
		}else add(1);
	}
}