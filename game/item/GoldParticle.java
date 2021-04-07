package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.*;
import game.entity.*;

public class GoldParticle extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/GoldParticle");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int maxAmount(){return 999;}
	public double hardness(){return game.entity.NormalAttacker.GOLD;}
	@Override
	public void onVanish(double x,double y,Source src){
		int px=f2i(x+rnd(-0.5,0.5)),py=f2i(y+rnd(-0.5,0.5));
		Block b=World.cur.get(px,py).rootBlock();
		if(b.getClass()==StoneBlock.class){
			if(rnd()*4<1)World.cur.set(px,py,new GoldOreBlock());
			return;
		}
	}
};
