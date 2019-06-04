package game.block;

import util.BmpRes;
import game.world.World;
import static util.MathUtil.*;

public class LavaBlock extends LiquidType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/LavaBlock");
	public BmpRes getBmp(){return bmp;}
	double friction(){return 0.9;}
	int maxDamage(){return 30;}
	public void touchEnt(int x,int y,game.entity.Entity ent){
		super.touchEnt(x,y,ent);
		ent.onAttackedByFire(0.5*intersection(x,y,ent),null);
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		for(int d=1;d<=2;++d)
			for(int t=0;t<4;++t){
				int x1=x+rndi(-d,d),y1=y+rndi(-d,d);
				World._.get(x1,y1).onFireUp(x1,y1);
			}
		return false;
	}
};
