package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import game.entity.Agent;
import game.world.World;

public class WoodenDoorBlock extends WoodenType{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/WoodenDoorBlock_",2);
	public BmpRes getBmp(){return bmp[tp];}
	public int maxDamage(){return 50;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	@Override
	public double transparency(){return tp==0?1:0;}
	public boolean isSolid(){return tp==0;}
	double friction(){return tp==0?0.2:0;}
	double frictionIn1(){return tp==0?0.5:0;}
	double frictionIn2(){return 0;}
	public int tp=0;
	
	public boolean onClick(int x,int y,Agent agent){
		if(changeState(x,y)){
			for(BlockAt ba:World.cur.get8(x,y)){
				Block bb=ba.block.rootBlock();
				if(bb.getClass()==getClass()){
					WoodenDoorBlock b=(WoodenDoorBlock)bb;
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
