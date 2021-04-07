package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;

public class EnergySword extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergySword");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	public int maxAmount(){
		return 1;
	}
	
	@Override
	public void onAttack(Entity e,Source src){
		if(e instanceof VehiclePlaceHolder)return;
		e.hp=0;
	}
	
	@Override
	public boolean isCreative(){return true;}

}
