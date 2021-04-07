package game.item;

import game.block.Block;
import game.entity.Agent;
import game.world.World;
import static util.MathUtil.*;

public class BlockRepairer extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 500;}
	public void onDesBlock(Block b){}
	public Item clickAt(double x,double y,Agent a){
		if(ec.isEmpty())return this;
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		int c=Math.min(50,Math.min(b.getDamage(),ec.get().getEnergy()));
		b.repair(px,py,-c);
		ec.get().loseEnergy(c);
		++damage;
		return this;
	}
}
