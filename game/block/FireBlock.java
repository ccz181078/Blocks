package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.entity.*;

public class FireBlock extends AirType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/FireBlock_",3);
	public BmpRes getBmp(){return bmp[rndi(0,2)];}
	public void onPress(int x,int y,game.item.Item item){}
	public void onDestroy(int x,int y){}
	public double light(){return 1;}

	@Override
	public void des(int x,int y,int v){}
	
	public void touchEnt(int x,int y,Entity ent){
		ent.onAttackedByFire(0.8*intersection(x,y,ent),SourceTool.block(x,y,this));
	}
	public boolean onCheck(int x,int y){
		for(BlockAt ba:World.cur.get4(x,y))
			if(ba.block.fuelVal()>0)return false;
		World.cur.setAir(x,y);
		return true;
	}
	public boolean onUpdate(int x,int y){
		if(onCheck(x,y))return true;
		for(int d=1;d<=2;++d)
		for(int t=0;t<4;++t){
			int x1=x+rndi(-d,d),y1=y+rndi(-d,d);
			World.cur.get(x1,y1).onFireUp(x1,y1);
		}
		for(BlockAt ba:World.cur.get4(x,y))ba.block.onBurn(ba.x,ba.y);
		return false;
	}
};
