package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class Balloon extends StoneBall{
	game.item.Balloon item;
	public Balloon(game.item.Balloon item){
		hp=40;
		this.item=item;
	}
	@Override
	void touchEnt(Entity ent){
		double k=(intersection(ent)+1)*(0.2+v2rel(ent)*2);
		ent.onAttacked(k,this);
		hp-=k*10;
		super.touchEnt(ent);
	}
	
	double _fc(){return 0.03;}
	@Override
	void upd_a(){
		aa+=-(ax*xv+ay*yv)*0.03-av*0.1;
		ya+=0.001;
	}
	
	public double mass(){return 0.08;}
	public double gA(){return 0;}
	
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


