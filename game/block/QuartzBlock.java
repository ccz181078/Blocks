package game.block;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;

public class QuartzBlock extends PureStoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/QuartzBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 240;}
	public void onDestroy(int x,int y){
		new game.item.Quartz().drop(x,y,rndi(14,16));
	}
	public void onLight(int x,int y,double v){
		if(rnd()<0.0005*v){
			World.cur.set(x,y,new SandBlock());
		}
	}
	@Override
	public double transparency(){return 0.3;}
	@Override
	protected int crackType(){return 1;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
};
