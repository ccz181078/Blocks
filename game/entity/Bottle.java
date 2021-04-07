package game.entity;

import game.block.Block;
import util.BmpRes;

public class Bottle extends Entity{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public double mass(){return 0.25;}
	public double hardness(){return bottle.hardness();}
	public Bottle(game.item.Bottle _bottle){
		hp=10;
		bottle=_bottle;
	}
	@Override
	public String getName(){
		String s=bottle.getName();
		return s;
	}
	
	@Override
	public boolean harmless(){return true;}
	
	
	game.item.Bottle bottle;
	
	@Override
	public BmpRes getBmp(){return bottle.getBmp();}
	
	@Override
	void touchBlock(int px,int py,Block block){
		bottle.touchBlock(this,px,py,block);
	}
	@Override
	void touchEnt(Entity ent){
		bottle.touchEnt(this,ent);
	}
	public void onBroken(){
		if(removed)return;
		bottle.onBroken(x,y,this);
		Fragment.gen(x,y,width(),height(),2,4,4,getBmp());
		remove();
	}
}
