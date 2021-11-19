package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class Boomerang extends StoneBall{
	game.item.Boomerang item;
	boolean upd_flag=true;
	public Boomerang(game.item.Boomerang item){
		hp=40;
		this.item=item;
	}
	@Override
	void touchEnt(Entity ent){
		double k=(intersection(ent)+1)*(0.2+v2rel(ent)*10)*10;
		ent.onAttacked(k,this);
		hp-=k*0.1;
		super.touchEnt(ent);
	}
	
	void upd_a(){
		if(upd_flag){
			av=item.rev?0.5:-0.5;
			upd_flag=false;
		}
		double c=(item.rev?1:-1)*av;
		if(c<0)aa-=av*0.1;
		else{
			c=av*0.2;
			xa+=yv*c;
			ya-=xv*c;
			aa-=av*0.01;
		}
	}
	public double mass(){return 0.2;}
	public double gA(){return 0.005;}
	
	void onKill(){
		if(rnd()<0.98)item.drop(x,y);
		else Fragment.gen(x,y,width(),height(),4,4,4,getBmp());
	}
	public BmpRes getBmp(){return item.getBmp();}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
