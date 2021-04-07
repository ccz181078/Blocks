package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;
import util.MathUtil;

public class Arrow extends Entity{
private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return 0.1;}
	public boolean chkBlock(){return type!=Type.DARK;}
	public boolean chkRigidBody(){return false;}
	game.item.Arrow arrow;
	public double hardness(){return arrow.hardness();}
	@Override
	public String getName(){
		String s=arrow.getName();
		if(type==Type.BLOOD)s="血·"+s;
		if(type==Type.FIRE)s="火·"+s;
		if(type==Type.DARK)s="影·"+s;
		if(type==Type.ENERGY)s="光·"+s;
		return s;
	}
	public enum Type{
		NORMAL,FIRE,DARK,ENERGY,BLOOD
	};
	Type type;
	public BmpRes getBmp(){return arrow.getBmp(type);}
	public Arrow(game.item.Arrow a,game.item.Bow b){
		hp=10;
		arrow=a;
		type=Type.NORMAL;
		if(b!=null){
			if(b instanceof game.item.FireCrossBow)type=Type.FIRE;
			if(b instanceof game.item.DarkCrossBow)type=Type.DARK;
			if(b instanceof game.item.EnergyCrossBow)type=Type.ENERGY;
			if(b instanceof game.item.BloodCrossBow)type=Type.BLOOD;
		}
	}
	public void update(){
		super.update();
		if(!World.cur.get(x,y).isCoverable())hp-=0.2;
		hp-=0.05;
		if(type==Type.FIRE)hp-=0.05;
	}
	@Override
	void upd_a(){
		double ax_=ax,ay_=ay;
		if(ax*xv+ay*yv<0){ax_*=-1;ay_*=-1;}
		aa+=(ax_*yv-ay_*xv)*0.2-av*0.4;
	}
	
	void touchBlock(int px,int py,Block b){
		if(type==Type.ENERGY&&rnd()>b.transparency())return;
		if(type==Type.FIRE)b.onFireUp(px,py);
		if(b.isSolid())kill();
	}
	@Override
	void touchEnt(Entity ent){
		double v2=v2rel(ent);
		if(type==Type.BLOOD && ent instanceof Agent){
			BloodBall.drop((Agent)ent,9,this);
			kill();
		}
		if(type==Type.FIRE){
			hp-=3;
			ent.onAttackedByFire(3,this);
		}
		if(type==Type.DARK){
			hp-=3;
			ent.onAttackedByDark(3,this);
		}
		if(type==Type.ENERGY){
			hp-=3;
			ent.onAttackedByEnergy(3,this);
		}
		if(v2>0.01){
			ent.impulseMerge(this);
			ent.onAttacked((v2-0.01)*arrow.attackValue(),this);
			kill();
		}
		super.touchEnt(ent);
	}

	public void onAttacked(Attack att){
		super.onAttacked(att);
		if(att instanceof FireAttack){type=Type.FIRE;}
	}
	
	public void onKill(){
		if(type!=Type.FIRE||arrow.getClass()!=game.item.Arrow.class)arrow.drop(x,y);
		else new game.item.IronNail().drop(x,y);
	}
	public double gA(){return 0.003;}
	public boolean rotByVelDir(){return true;}
}
