package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;

public class ProtectionZone extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/ProtectionZone");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	public boolean isCreative(){return true;}

	@Override
	public void onUse(Human a){
		game.entity.ProtectionZone.setPlayer(a);
	}
	@Override
	public BmpRes getUseBmp(){return field_btn;}
	public boolean autoUse(Human h,Agent a){
		for(Entity e:World.cur.getNearby(h.x,h.y,2,2,false,true,false).ents){
			if(e instanceof game.entity.ProtectionZone)return true;
		}
		if(a==null){
			h.useCarriedItem();
			return true;
		}
		return false;
	}
}
