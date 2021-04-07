package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.*;
import game.world.World;
import game.entity.*;

public class GlueBottle extends Bottle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/GlueBottle");
	public BmpRes getBmp(){return bmp;}
	public void onBroken(double x,double y,game.entity.Bottle ent){
		if(rnd()<0.75)return;
		for(int i=0;i<5;++i){
			int px=f2i(x+rnd_gaussion()*0.3),py=f2i(y+rnd_gaussion()*0.3);
			if(World.cur.get(px,py).isCoverable()){
				World.cur.place(px,py,new game.block.GlueBlock());
				return;
			}
		}
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		return ShootTool.auto(h,a,this);
	}
}
