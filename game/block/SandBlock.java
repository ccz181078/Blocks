package game.block;

import util.BmpRes;
import game.item.Item;
import static util.MathUtil.*;
import game.world.World;

public class SandBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/SandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 30;}
	public int heatingTime(boolean in_furnace){return 200;}
	public Item heatingProduct(boolean in_furnace){return new GlassBlock();}
	public void onLight(int x,int y,double v){
		if(rnd()<0.003*v
		&& World.cur.get(x,y+1) instanceof AirBlock
		&& World.cur.get(x-1,y+2) instanceof AirBlock
		&& World.cur.get(x+1,y+2) instanceof AirBlock
		&& World.cur.get(x-2,y+3) instanceof AirBlock
		&& World.cur.get(x+2,y+3) instanceof AirBlock
		){
			World.cur.place(x,y+1,new CactusBlock());
		}
	}
};
