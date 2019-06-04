package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class FireBlock extends AirType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/FireBlock_",3);
	public BmpRes getBmp(){return bmp[rndi(0,2)];}
	public void onPress(int x,int y,game.item.Item item){}
	public void onDestroy(int x,int y){}
	public void touchEnt(int x,int y,game.entity.Entity ent){
		ent.onAttackedByFire(0.2*intersection(x,y,ent),null);
	}
	public boolean onCheck(int x,int y){
		for(BlockAt ba:World._.get4(x,y))
			if(ba.block.fuelVal()>0)return false;
		World._.setAir(x,y);
		return true;
	}
	public boolean onUpdate(int x,int y){
		if(onCheck(x,y))return true;
		for(int d=1;d<=2;++d)
		for(int t=0;t<4;++t){
			int x1=x+rndi(-d,d),y1=y+rndi(-d,d);
			World._.get(x1,y1).onFireUp(x1,y1);
		}
		for(BlockAt ba:World._.get4(x,y))ba.block.onBurn(ba.x,ba.y);
		return false;
	}
};
