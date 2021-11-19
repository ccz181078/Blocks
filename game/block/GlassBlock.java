package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.Entity;

public class GlassBlock extends StoneType{
	private static final long serialVersionUID=1844677L;	
	static BmpRes bmp=new BmpRes("Block/GlassBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	public double transparency(){return 0;}
	double friction(){return 0.02;}
	double frictionIn1(){return 0.1;}
	double frictionIn2(){return 0.1;}
	public void onDestroy(int x,int y){}
	/*@Override
	public void onOverlap(int x,int y,Entity ent,double k){
		super.onOverlap(x,y,ent,k);
		int c=rf2i(k*10);
		if(c>0)game.world.World.cur.get(x,y).des(x,y,c);
	}*/
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
};
