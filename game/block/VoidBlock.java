package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.entity.*;

public class VoidBlock extends SmokeBlock{
	public BmpRes getBmp(){return AirBlock.bmp;}
	public boolean isCoverable(){return true;}
	
	double frictionIn1(){return 0.005;}
	double frictionIn2(){return 0.005;}
	int maxDamage(){return 20000;}
	@Override
	public boolean onUpdate(int x,int y){
		int x1=x,y1=y,d=rnd()<0.5?-1:1;
		if(rnd()<0.5)x1+=d;
		else y1+=d;
		Block b=World.cur.get(x1,y1);
		if(b.isCoverable()&&!(b instanceof VoidBlock)){
			World.cur.setAir(x,y);
			return true;
		}
		return false;
	}
	/*@Override
	public void touchEnt(int x,int y,Entity e){
		if(e instanceof DroppedItem)e.hp-=0.03;
		super.touchEnt(x,y,e);
	}*/
	@Override
	public double transparency(){return 0;}
	@Override
	public boolean fallable(){return false;}
}