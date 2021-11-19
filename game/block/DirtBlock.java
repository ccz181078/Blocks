package game.block;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;

public class DirtBlock extends DirtType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DirtBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	public static boolean genTree(int x,int y){
		if(World.cur.get(x  ,y+2) instanceof AirBlock
			&& World.cur.get(x-1,y+2) instanceof AirBlock
			&& World.cur.get(x+1,y+2) instanceof AirBlock
			
			&& World.cur.get(x  ,y+3) instanceof AirBlock
			&& World.cur.get(x-1,y+3) instanceof AirBlock
			&& World.cur.get(x+1,y+3) instanceof AirBlock
			&& World.cur.get(x-2,y+3) instanceof AirBlock
			&& World.cur.get(x+2,y+3) instanceof AirBlock
			
			&& World.cur.get(x  ,y+4) instanceof AirBlock
			&& World.cur.get(x-3,y+4) instanceof AirBlock
			&& World.cur.get(x+3,y+4) instanceof AirBlock
		){
			World.cur.place(x,y+1,new TrunkBlock());
			World.cur.place(x  ,y+2,new TrunkBlock());
			World.cur.place(x-1,y+2,new LeafBlock(0));
			World.cur.place(x+1,y+2,new LeafBlock(0));
			World.cur.place(x  ,y+3,new LeafBlock(0));
			World.cur.place(x-1,y+3,new LeafBlock(0));
			World.cur.place(x+1,y+3,new LeafBlock(0));
			return true;
		}
		return false;
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(rnd()<0.008){
			int x1=x+rndi(-1,1),y1=y+rndi(-1,1);
			int x2=x+rndi(-1,1),y2=y+rndi(-1,1);
			if(World.cur.get(x1,y1).rootBlock() instanceof SandBlock)
			if(World.cur.get(x2,y2).rootBlock() instanceof SandBlock){
				World.cur.set(x,y,new SandBlock());
				return true;
			}
		}
		return false;
	}
	public void onLight(int x,int y,double v){
		if(rnd()<0.05*v&&World.cur.get(x,y+1).isCoverable()){
			if(rnd()<0.01&&genTree(x,y));
			else if(World.cur.get(x,y+1) instanceof AirBlock){
				double r=rnd();
				if(r<0.8)World.cur.place(x,y+1,new GrassBlock(0));
				else if(r<0.9)World.cur.place(x,y+1,new BallPlantBlock());
				else if(r<0.95)World.cur.place(x,y+1,new RedFlowerBlock());
				else if(r<0.99)World.cur.place(x,y+1,new PurpleFlowerBlock());
			}
		}
	}
};
