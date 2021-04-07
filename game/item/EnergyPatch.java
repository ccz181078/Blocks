package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;

public class EnergyPatch extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyPatch");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public void onUse(Human hu){
		Armor ar=hu.armor.get();
		if(ar!=null&&(ar instanceof EnergyArmor)){
			EnergyArmor ea=(EnergyArmor)ar;
			if(ea.damage>0&&ea.last_fix_time<World.cur.time){
				UsingItem.gen(0.3,0.3,40,this,hu);
				ea.last_fix_time=World.cur.time+50;
				return;
			}
		}
		hu.items.getSelected().insert(this);
	}
	public BmpRes getUseBmp(){
		return eat_btn;
	}
	public void using(UsingItem ent){
		Human hu=(Human)ent.ent;
		Armor ar=hu.armor.get();
		if(ar!=null&&(ar instanceof EnergyArmor)){
			EnergyArmor ea=(EnergyArmor)ar;
			if(ea.damage>=4)ea.damage-=4;
		}
	}
	
	@Override
	public boolean autoUse(Human h,Agent a){
		h.items.getSelected().popItem().onUse(h);
		return false;
	}
}
