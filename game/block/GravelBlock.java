package game.block;

import util.BmpRes;
import game.world.World;
import static util.MathUtil.*;

public class GravelBlock extends DirtType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/GravelBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 60;}
	public void onLight(int x,int y,double v){
		if(rnd()<0.001*v*v){
			World.cur.set(x,y,new SandBlock());
		}
	}
};
