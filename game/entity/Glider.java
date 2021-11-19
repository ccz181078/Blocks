package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class Glider extends StoneBall{
	game.item.Glider item;
	public Glider(game.item.Glider item){
		hp=40;
		this.item=item;
	}
	@Override
	void touchEnt(Entity ent){
		double k=(intersection(ent)+1)*(0.2+v2rel(ent)*10)*10;
		ent.onAttacked(k,this);
		hp-=k;
		super.touchEnt(ent);
	}
	
	double _fc(){return 0.001;}
	@Override
	void upd_a(){
		double va=ax*xv+ay*yv;
		double ax_=ax,ay_=ay;
		aa+=(ax*yv-ay*xv)*0.02-av*0.1;
		
		double v=sqrt(xv*xv+yv*yv);
		double c=min(v,0.5)*max(va,0)*0.05*(item.rev?-1:1);
		xa+=ay*c;
		ya-=ax*c;
	}
	
	public double mass(){return 0.08;}
	public double gA(){return 0.005;}
	
	void onKill(){
		if(!item.warhead.isEmpty()){
			item.warhead.popItem().explode(x,y,xv+rnd_gaussion()*0.01,yv+rnd_gaussion()*0.01,this,null);
			Fragment.gen(x,y,width(),height(),4,4,4,getBmp());
		}else if(rnd()<0.98){
			item.drop(x,y);
		}
	}
	public BmpRes getBmp(){return item.getBmp();}
	public double hardness(){return game.entity.NormalAttacker.PAPER;}
}


