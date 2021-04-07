package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.Agent;
import game.world.World;

public class EnergyPickax extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyPickax");
	public BmpRes getBmp(){return bmp;}

	@Override
	public int maxAmount(){
		return 1;
	}

	@Override
	public Item clickAt(double x,double y,Agent a){
		World.cur.setAir(f2i(x),f2i(y));
		World.cur.check4(f2i(x),f2i(y));
		return this;
	}

	@Override
	public void onPressBlock(int x,int y,Agent a){
		World.cur.setAir(f2i(x),f2i(y));
		World.cur.check4(f2i(x),f2i(y));
	}
	@Override
	public boolean isCreative(){return true;}
	
}
