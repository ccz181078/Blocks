package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class StoneBall extends Entity implements AttackFilter{
	private static final long serialVersionUID=1844677L;
	
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	public double mass(){return 1;}
	public StoneBall(){
		hp=40;
	}
	public boolean pickable(){return true;}
	public void pickedBy(Agent a){
		if(!removed){
			drop();
			removed=true;
		}
	}
	protected void drop(){
		kill();
	}
	public void update(){
		super.update();
		if(xdep!=0||ydep!=0||in_wall)hp-=0.1;
		hp-=0.03;
	}
	@Override
	double friction(){return 0.5;}
	@Override
	public boolean rotByVelDir(){return true;}
	
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(0.1+v2rel(ent)*40)*10;
		ent.onAttacked(k,this);
		hp-=k;
		super.touchEnt(ent);
	}
	@Override
	public AttackFilter getAttackFilter(){return this;}//攻击过滤器
	public Attack transform(Attack a){
		if(a!=null)a.val*=0.2;
		return a;
	}
	
	void onKill(){
		new Stone().drop(x,y,rndi(4,6));
		Fragment.gen(x,y,width(),height(),4,4,4,getBmp());
	}
	public BmpRes getBmp(){return game.item.StoneBall.bmp;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
}
