package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.*;
import game.world.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class FieldMaintainer extends EnergyTool{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FieldMaintainer");
	public BmpRes getBmp(){return bmp;}
	public double toolVal(){return 0;}
	public void onDesBlock(Block b){}
	public int maxDamage(){return 10000;}
	public void onCarried(Agent a){
		for(Entity e:World.cur.getNearby(a.x,a.y,1,1,false,true,false).ents){
			if(e instanceof FieldBuff){
				if(e.hp<120&&e.hp>40){
					int c=rf2i(min(5,120-e.hp)*10);
					if(hasEnergy(c)){
						loseEnergy(c);
						e.hp=120;
					}
				}
			}
		}
	}
}