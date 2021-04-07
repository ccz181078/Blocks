package game.block;

import util.BmpRes;
import game.world.World;
import static util.MathUtil.*;
import game.entity.*;

public class LavaBlock extends LiquidType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/LavaBlock");
	public BmpRes getBmp(){return bmp;}
	double frictionIn2(){return 200;}
	int maxDamage(){return 30;}
	public double light(){return 1;}
	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		ent.onAttackedByFire(2*intersection(x,y,ent),SourceTool.block(x,y,this));
	}
	public void onLight(int x,int y,double v){
		if(rnd()<0.01*v*v)World.cur.set(x,y,new SemilavaBlock());
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		for(int d=1;d<=2;++d)
			for(int t=0;t<4;++t){
				int x1=x+rndi(-d,d),y1=y+rndi(-d,d);
				Block b=World.cur.get(x1,y1);
				if(d==1&&(b.rootBlock() instanceof WaterBlock)){
					World.cur.set(x,y,new SemilavaBlock());
					return true;
				}
				b.onFireUp(x1,y1);
			}
		return false;
	}
	
	public static LavaBlock getInstance(){return new LavaBlock();}
	protected LavaBlock(){}
};
