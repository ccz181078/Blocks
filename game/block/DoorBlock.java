package game.block;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static java.lang.Math.*;

public class DoorBlock extends CircuitBlock{
	static BmpRes bmp=new BmpRes("Block/DoorBlock");
	public BmpRes getBmp(){return bmp;}
	public boolean isSolid(){return tp==0?super.isSolid():false;}
	public double transparency(){return tp==0?super.transparency():0;}
	double friction(){return tp==0?super.friction():0;}
	double frictionIn1(){return tp==0?super.frictionIn1():0;}
	double frictionIn2(){return tp==0?super.frictionIn2():0;}
	public int tp=0;
	
	@Override
	public Block clone(){
		DoorBlock w=new DoorBlock(block.clone());
		w.tp=tp;
		return w;
	}
	
	@Override
	public void onOverlap(int x,int y,Entity ent,double k){
		if(tp==0)super.onOverlap(x,y,ent,k);
	}
	@Override
	public void touchEnt(int x,int y,Entity ent){if(tp==0)super.touchEnt(x,y,ent);}
	
	public DoorBlock(Block b){
		super(b);
	}
	
	public boolean onClick(int x,int y,Agent agent){
		if(changeState(x,y)){
			for(BlockAt ba:World.cur.get8(x,y)){
				Block bb=ba.block;
				if(bb.getClass()==getClass()){
					DoorBlock b=(DoorBlock)bb;
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
	
	public boolean onSpannerClick(int x,int y){
		return false;
	}
	
	public void onCircuitDestroy(int x,int y){
		new game.item.Door().drop(x,y);
	}
	
	public void draw(graphics.Canvas cv){
		if(tp==0)super.draw(cv);
		getBmp().draw(cv,0,0,0.5f,0.5f);
	}
}
