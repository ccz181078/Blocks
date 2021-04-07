package game.block;

import util.BmpRes;
import game.world.World;
import static util.MathUtil.*;
import game.entity.*;

public class SemilavaBlock extends DirtType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/SemilavaBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 60;}
	public void touchEnt(int x,int y,game.entity.Entity ent){
		super.touchEnt(x,y,ent);
		ent.onAttackedByFire(0.5*intersection(x,y,ent),SourceTool.block(x,y,this));
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(rnd()<0.01){
			if(World.cur.get(x+rndi(-1,1),y+rndi(-1,1)).isCoverable()){
				World.cur.set(x,y,new StoneBlock());
				return true;
			}
		}
		for(int d=1;d<=2;++d){
			int x1=x+rndi(-d,d),y1=y+rndi(-d,d);
			Block b=World.cur.get(x1,y1);
			if(d==1&&(b.rootBlock() instanceof WaterBlock)){
				World.cur.set(x,y,new StoneBlock());
				return true;
			}
			b.onFireUp(x1,y1);
		}

		return false;
	}
};
