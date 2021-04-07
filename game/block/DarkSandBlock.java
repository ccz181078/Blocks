package game.block;

import util.BmpRes;
import game.item.Item;
import static util.MathUtil.*;
import game.world.World;

public class DarkSandBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DarkSandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	public void onLight(int x,int y,double v){
		if(rnd()<0.003*v
		&& World.cur.get(x,y+1) instanceof AirBlock
		&& World.cur.get(x-1,y+1) instanceof AirBlock
		&& World.cur.get(x+1,y+1) instanceof AirBlock
		&& World.cur.get(x-2,y+2) instanceof AirBlock
		&& World.cur.get(x+2,y+2) instanceof AirBlock
		){
			World.cur.place(x,y+1,new DarkVineBlock(1));
		}
	}
};
