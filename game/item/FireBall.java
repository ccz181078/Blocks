package game.item;

import util.BmpRes;
import game.entity.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.*;

public class FireBall extends BasicBall{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireBall");
	public BmpRes getBmp(){return bmp;}
	public Item clickAt(double x,double y,Agent a){
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py);
		if(b instanceof AirBlock)return this;
		for(int t=0;t<4;++t)b.onFireUp(px,py);
		if(rnd()<0.2)return null;
		return this;
	}

	@Override
	game.entity.Entity getBall(){return new game.entity.HT_FireBall().setHpScale(52.5);}
	@Override
	public boolean autoUse(Human h,Agent a){
		double D=h.distLinf(a);
		if(D>2&&D<5){
			for(int t=0;t<10;++t)
			if(h.selectItem(ExplosiveBlock.class,true)){
				double x=a.x+rnd(-1,1),y=a.y+rnd(-1,1);
				if(World.cur.get(x,y).isCoverable()){
					h.clickAt(x,y);
					if(!World.cur.get(x,y).isCoverable()){
						h.selectItem(FireBall.class,false);
						h.clickAt(x,y);
						World.showText(x+","+y);
						return true;
					}
				}
			}
		}
		return super.autoUse(h,a);
	}
};
