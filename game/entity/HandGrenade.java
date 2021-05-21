package game.entity;

import util.BmpRes;
import game.item.Warhead;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class HandGrenade extends Entity{
	private static final long serialVersionUID=1844677L;
	
	public double width(){return 0.3;}
	public double height(){return 0.3;}
	public double mass(){return 0.2;}
	
	game.item.HandGrenade item;
	public double hardness(){return item.hardness();}
	int time=60;
	
	@Override
	public String getName(){
		String s=item.getName();
		return s;
	}
	public HandGrenade(game.item.HandGrenade it){
		hp=40;
		item=it;
	}
	public void update(){
		super.update();
		hp-=0.03;
		if(--time<=0)kill();
	}
	
	@Override
	void touchAgent(Agent ent){
		//if(rnd()<ent.RPG_ExplodeProb())kill();
		kill();
		super.touchAgent(ent);
	}
	
	void onKill(){
		Warhead wh=item.warhead.popItem();
		Spark.explode(x,y,xv,yv,4,0.1,4,this);
		if(wh!=null){
			wh.explode(x,y,xv+rnd_gaussion()*0.01,yv+rnd_gaussion()*0.01,this,null);
		}
	}
	public BmpRes getBmp(){return item.getBmp();}
}
