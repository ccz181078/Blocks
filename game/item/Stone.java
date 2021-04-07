package game.item;

import util.BmpRes;
import game.entity.*;
import game.block.GravelBlock;
import static util.MathUtil.*;

public class Stone extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Stone");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	@Override
	public int maxAmount(){return 999;}
	@Override
	public void onVanish(double x,double y,Source src){
		if(rnd()<0.125){
			int px=f2i(x),py=f2i(y);
			new FallingBlock(px,py,new GravelBlock()).initPos(x,y,0,0,src).add();
		}
	}
}

