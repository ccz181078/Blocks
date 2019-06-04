package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;

public class TeleportationStick extends Tool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/TeleportationStick");
	public BmpRes getBmp(){return bmp;}
	protected double toolVal(){return 0;}
	public Item clickAt(double x,double y,Agent a){
		a.x=x;
		a.y=y;
		++damage;
		return this;
	}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 100;}
	public void onBroken(double x,double y){
		new IronStick().drop(x,y);
	}
	public double repairRate(){return 0.1;}
}
