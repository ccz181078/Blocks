package game.block;

import util.BmpRes;
import static util.MathUtil.*;
import game.world.World;

public class ZombieSandBlock extends DirtBlock{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/ZombieSandBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 80;}
	public void onLight(int x,int y,double v){
		if(rnd()<0.3)World.cur.set(x,y,new DirtBlock());
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(rnd()<0.01){
			int x1=x+rndi(-1,1),y1=y+rndi(-1,1);
			if(World.cur.get(x1,y1).isCoverable()){
				World.cur.place(x1,y1,new ZombieSandBlock());
			}
		}
		return false;
	}
};
