package game.item;

import util.BmpRes;
import game.entity.*;

public class EnergyStoneRing extends ExplosiveBall{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/EnergyStoneRing");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.EnergyStoneRing();
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(a instanceof Player){
			if(((Player)a).armor.get()==null)return false;
		}
		return super.autoUse(h,a);
	}
};
