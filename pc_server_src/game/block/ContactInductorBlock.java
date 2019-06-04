package game.block;

import game.entity.Entity;
import game.world.World;
import game.item.ContactInductor;
import graphics.Canvas;

public class ContactInductorBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	int tp=0;
	public ContactInductorBlock(Block b){super(b);}
	public int energyValX(){return tp>0?32:0;}
	public int energyValY(){return tp>0?32:0;}

	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(tp>0){
			--tp;
			if(tp==0)World.cur.check4(x,y);
			else World.cur.checkBlock(x,y);
		}
		return false;
	}

	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		if(ent instanceof game.entity.Agent){
			if(tp==0)World.cur.check4(x,y);
			tp=10;
		}
	}
	
	public void onCircuitDestroy(int x,int y){
		new ContactInductor().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		ContactInductor.bmp[tp>0?1:0].draw(cv,0,0,.5f,.5f);
	}
	
}
