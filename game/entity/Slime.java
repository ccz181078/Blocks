package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.world.World;

public class Slime extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/GlueBlock_0");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.9;}
	public double height(){return 0.9;}
	public double mass(){return 4;}
	public double maxHp(){return 1e5;}
	double _fc(){return 0.01;}
	public Slime(double _x,double _y){super(_x,_y);}
	Group group(){return Group.ENERGY;}
	
	@Override
	public double buoyancyForce(){return 1;}
	@Override
	public double fluidResistance(){return 0.3;}
	
	@Override
	void touchAgent(Agent ent){
		if(ent.group()!=group()){
			target=ent;
		}
		super.touchAgent(ent);
	}
	
	@Override
	void touchEnt(Entity ent){
		ent.onAttacked(0.2,this);
		ent.onAttackedByEnergy(1,this);
		addHp(1);
		exchangeVel(ent,0.2);
		
		double m=min(mass(),ent.mass())*0.07;
		double xd=ent.x-x,yd=ent.y-y;
		impulse(xd,yd,m);
		ent.impulse(xd,yd,-m);
		
		super.touchEnt(ent);
	}
	@Override
	public double RPG_ExplodeProb(){return 0.02;}
	
	public double onImpact(double v){
		return max(0,v-1000);
	}
	
	public AttackFilter getAttackFilter(){return energy_filter;}
	
	void onKill(){
		new game.block.GlueBlock().drop(x,y,4);
		super.onKill();
	}
	@Override
	public void onKilled(Source src){
		String s=getName();
		if(src==null){
			World.showText(">>> "+getName()+"死亡了");
		}else{
			World.showText(">>> "+src+"击杀了"+getName());
		}
		super.onKilled(src);
	}
}
