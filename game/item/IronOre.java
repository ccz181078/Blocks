package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.*;
import game.entity.*;

public class IronOre extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronOre");
	public BmpRes getBmp(){return bmp;}
	public int heatingTime(boolean in_furnace){return in_furnace?200:1000000000;}
	public Item heatingProduct(boolean in_furnace){return in_furnace?new Iron():null;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	@Override
	public void onVanish(double x,double y,Source src){
		int px=f2i(x+rnd(-0.5,0.5)),py=f2i(y+rnd(-0.5,0.5));
		Block b=World.cur.get(px,py).rootBlock();
		if(b.getClass()==StoneBlock.class){
			World.cur.set(px,py,new IronOreBlock(0));
			return;
		}
		if(b instanceof IronOreBlock){
			int tp=((IronOreBlock)b).tp;
			if(tp<=1){
				if(rnd()<0.5)World.cur.set(px,py,new IronOreBlock(tp+1));
				return;
			}
			if(tp==2){
				if(rnd()*11<1)World.cur.set(px,py,new IronOreBlock(tp+1));
				return;
			}
		}
		if(rnd()*16<1){
			new FallingBlock(px,py,new IronOreBlock(3)).initPos(x,y,0,0,src).add();
		}
	}
};
