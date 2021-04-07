package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;
import game.block.*;
import game.entity.*;

public class BlueCrystal extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BlueCrystal");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	@Override
	public void onVanish(double x,double y,Source src){
		int px=f2i(x+rnd(-0.5,0.5)),py=f2i(y+rnd(-0.5,0.5));
		Block b=World.cur.get(px,py).rootBlock();
		if(b.getClass()==StoneBlock.class){
			if(rnd()*3<1)World.cur.set(px,py,new BlueCrystalOreBlock());
			return;
		}
		if(rnd()*16<1){
			new FallingBlock(px,py,new BlueCrystalBlock()).initPos(x,y,0,0,src).add();
		}
	}
};
