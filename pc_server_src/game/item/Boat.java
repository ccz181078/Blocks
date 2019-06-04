package game.item;

import util.BmpRes;
import game.entity.Agent;
import game.world.World;
import game.entity.Entity;

public class Boat extends Item{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/Boat");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 30;}

	public Item clickAt(double x,double y,Agent a){
		if(World.cur.noBlock(x,y,0.4,0.4)){
			Entity e=new game.entity.Boat();
			e.x=x;
			e.y=y;
			e.add();
			return null;
		}
		return this;
	}
	
};
