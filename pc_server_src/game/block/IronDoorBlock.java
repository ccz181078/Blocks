package game.block;

import util.BmpRes;
import game.world.World;
import game.entity.Agent;
import static java.lang.Math.*;

public class IronDoorBlock extends IronBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/IronDoorBlock_",2);
	public BmpRes getBmp(){return bmp[tp];}
	public int maxDamage(){return 200;}
	public boolean isSolid(){return tp==0;}
	public int tp=0;
	double friction(){return 0;}
	
	public boolean onClick(int x,int y,Agent agent){
		if(changeState(x,y)){
			for(BlockAt ba:World.cur.get4(x,y)){
				Block bb=ba.block.rootBlock();
				if(bb.getClass()==getClass()){
					IronDoorBlock b=(IronDoorBlock)bb;
					if(b.tp!=tp)b.changeState(ba.x,ba.y);
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean changeState(int x,int y){
		if(tp==0){
			tp=1;
			return true;
		}
		if(World.cur.getNearby(x+0.5,y+0.5,0.45,0.45,false,false,true).agents.size()==0){
			tp=0;
			return true;
		}
		return false;
	}
	
	public void onDestroy(int x,int y){
		tp=0;
		super.onDestroy(x,y);
	}
}
