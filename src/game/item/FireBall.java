package game.item;

import util.BmpRes;
import game.entity.Agent;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;
import game.block.AirBlock;

public class FireBall extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireBall");
	public BmpRes getBmp(){return bmp;}
	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World._.get(px,py);
		if(b instanceof AirBlock)return this;
		for(int t=0;t<4;++t)b.onFireUp(px,py);
		if(rnd()<0.2)return null;
		return this;
	}
};
