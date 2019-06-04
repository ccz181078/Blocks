package game.item;

import util.BmpRes;
import game.entity.Agent;
import game.world.World;
import static util.MathUtil.*;
import game.block.*;

public class IronStick extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronStick");
	public BmpRes getBmp(){return bmp;}
	static long last_click_t=-100;
	static int click_t=0;
	
}
