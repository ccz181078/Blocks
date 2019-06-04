package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import static util.MathUtil.*;
import game.block.*;

public class Stick extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Stick");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal()
	{return 7;}
	public Item clickAt(double x, double y, Agent a){
		if(EnergyPowerBlock.class.isAssignableFrom(World.cur.get(f2i(x),f2i(y)).getClass())){
			EnergyPowerBlock epl=(EnergyPowerBlock)World.cur.get(f2i(x),f2i(y));
			World.showText("能量"+new Integer(epl.energy).toString());
		}
		return super.clickAt(x,y,a);
	}
	
}
