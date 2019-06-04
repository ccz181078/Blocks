package game.item;

import game.block.Block;
import game.entity.Agent;
import game.world.World;

public class BlockRepairer extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 500;}
	public void onDesBlock(Block b){}
	public Item clickAt(double x,double y,Agent a){
		if(ec.isEmpty())return this;
		Block b=World._.get(x,y);
		int c=Math.min(50,Math.min(b.damage,ec.get().energy));
		b.damage-=c;
		ec.get().energy-=c;
		++damage;
		return this;
	}
}
