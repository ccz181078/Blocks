package game.item;

import game.block.Block;
import game.entity.Agent;
import game.world.World;
import game.block.CircuitBlock;
import static util.MathUtil.*;

public class Spanner extends Tool{
	private static final long serialVersionUID=1844677L;
	public int maxDamage(){return 500;}
	protected double toolVal(){return 0;}
	
	public void onDesBlock(Block b){}
	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(b instanceof CircuitBlock){
			if(((CircuitBlock)b).onSpannerClick(px,py))++damage;
		}
		return this;
	}
	public void onBroken(double x,double y){}
}
