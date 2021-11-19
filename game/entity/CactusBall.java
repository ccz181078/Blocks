package game.entity;

import static util.MathUtil.*;
import game.item.Stone;
import util.BmpRes;
import game.block.Block;
import static java.lang.Math.*;

public class CactusBall extends StoneBall{
	public CactusBall(){
		hp=100;
	}
	@Override
	void touchEnt(Entity ent){
		double k=(intersection(ent)+1)*(0.2+v2rel(ent)*10)*10;
		ent.onAttacked(k,this);
		hp-=k*0.3;
		exchangeVel(ent,0.3);
		super.touchEnt(ent);
	}
	@Override
	public AttackFilter getAttackFilter(){return this;}//攻击过滤器
	public Attack transform(Attack a){
		if(a instanceof FireAttack)a.val*=10;
		else if(a!=null)a.val*=0.2;
		return a;
	}
	
	void onKill(){
		Fragment.gen(x,y,width(),height(),4,4,4,getBmp());
	}
	public BmpRes getBmp(){return game.item.CactusBall.bmp;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
}
