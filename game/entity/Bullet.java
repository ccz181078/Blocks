package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import util.BmpRes;

public class Bullet extends Entity{
	private static final long serialVersionUID=1844677L;
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double mass(){return bullet.mass();}
	public boolean chkBlock(){return bullet.chkBlock();}
	public boolean chkRigidBody(){return false;}
	public boolean rotByVelDir(){return true;}
	public double hardness(){return bullet.hardness();}
	game.item.Bullet bullet;
	@Override
	public String getName(){
		String s=bullet.getName();
		return s;
	}
	double _fc(){return bullet._fc();}
	@Override
	public AttackFilter getAttackFilter(){return bullet;}//攻击过滤器
	
	public BmpRes getBmp(){return bullet.getBmp();}
	
	public Bullet(game.item.Bullet b){
		hp=30;
		bullet=b;
	}
	public boolean test_chkBlock(){
		if(bullet.chkBlock()){
			Block b=World.cur.get(x,y);
			if(b.isSolid()||b.chkNonRigidEnt())return true;
		}
		return false;
	}
	public void update(){
		super.update();
		//Line.gen(this);
		//if(c>0.3)new Smoke().initPos(x,y,xv*0.7,yv*0.7,null).add();
		
		if(bullet.chkBlock()){
			int px=f2i(x),py=f2i(y);
			Block b=World.cur.get(px,py);
			chkBlock(px,py,b);
		}
		
		//if(sqrt(xv*xv+yv*yv)<=0.1&&rnd()<0.1)kill();
		hp-=0.05;
	}
	void touchBlock(int px,int py,Block b){
		double v=sqrt(xv*xv+yv*yv);
		if(b.isSolid()){
			b.des(px,py,((xv*xv+yv*yv)*10+0.1),this);
			hp-=5;
			f+=0.2;
		}else if(!b.isCoverable()){
			hp-=0.2;
			b.des(px,py,((xv*xv+yv*yv)*10+0.1),this);
		}
	}
	@Override
	void upd_a(){
		double ax_=ax,ay_=ay;
		if(ax*xv+ay*yv<0){ax_*=-1;ay_*=-1;}
		aa+=(ax_*yv-ay_*xv)*0.02-av*0.1;
	}
	@Override
	void touchEnt(Entity ent,boolean flag){
		if((ent instanceof Bullet)){
			if(xv*ent.xv+yv*ent.yv>0)return;
		}
		if(flag||rnd()*3<ent.RPG_ExplodeProb())touchEnt(ent);
	}
	void touchEnt(Entity ent){
		if(source==ent.source&&(ent instanceof Bullet))return;
		bullet.touchEnt(this,ent);
		//super.touchEnt(ent);
	}
	@Override
	public double RPG_ExplodeProb(){return bullet.RPG_ExplodeProb();}
	public void onKill(){
		/*RPG_HE r=new RPG_HE(new game.item.RPG_HE());
		r.initPos(x,y,xv,yv,this);
		r.explode();*/
		bullet.onKill(x,y,this);
	}
	public double gA(){return 0.03/((xv*xv+yv*yv)*100+1);}
}
