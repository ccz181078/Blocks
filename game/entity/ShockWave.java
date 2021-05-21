package game.entity;

import util.BmpRes;
import game.block.GlassBlock;
import game.world.World;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class ShockWave extends Ball{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return null;}
	public ShockWave(){
		hp=10;
	}
	public boolean isPureEnergy(){return true;}
	public double mass(){return 0.1;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	double _fc(){return 3e-3;}
	@Override
	public double RPG_ExplodeProb(){return 0;}
	boolean chk_ent=false;
	@Override
	public boolean chkEnt(){return chk_ent;}
	void touchEnt(Entity a){
		if(a instanceof ShockWave){
			if(rnd()<0.3)chk_ent=false;
			return;
		}
		if(hp<=0||a.hp<=0||(a instanceof Spark))return;
		a.onAttacked(hp*hp/50.,this,this);
		a.onAttackedByEnergy(hp*hp/50.,this);
		a.xa+=(xv*1.0-a.xv)*0.5/max(1,a.mass());
		a.ya+=(yv*1.0-a.yv)*0.5/max(1,a.mass());
		hp-=0.5;
	}
	@Override
	public AttackFilter getAttackFilter(){return special_filter;}//攻击过滤器
	@Override
	protected boolean useRandomWalk(){return false;}
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		if(!b.isCoverable()){
			double s=1+b.shockWaveResistance()/50.;
			if(rnd()*s>hp){
				b.des(px,py,3,this);
				remove();
				return;
			}
			hp-=s;
			if(b.fallable()){
				World.cur.setAir(px,py);
				Entity e=new FallingBlock(px,py,b);
				e.xv=xv;
				e.yv=yv;
				e.add();				
			}else{
				b.des(px,py,3,this);
			}
		}
	}
	@Override
	public void update(){
		super.update();
		if(rnd()<0.1)chk_ent=true;
		double v=min(1,hypot(xv,yv)*(sqrt(hp/10)))*0.2+hp*hp*1e-5;
		hp-=0.04/(hypot(xv,yv)+0.01);
		if(rnd()<v)new Smoke().initPos(x,y,xv,yv,null).add();
		if(rnd()<0.2){
			double v1=hypot(xv,yv)+1e-8;
			game.world.GroupFall.apply(f2i(x+xv/v1),f2i(y+yv/v1));
		}
	}
	public static void explode(double x0,double y0,double xv0,double yv0,int num,double v,double hp_scale,Source src){
		src=SourceTool.explode(src);
		for(int i=0;i<num;++i){
			ShockWave s=new ShockWave();
			double a=i*(2*PI/num);
			double xd=cos(a),yd=sin(a);
			s.initPos(x0+xd*0.3,y0+yd*0.3,xv0+xd*v,yv0+yd*v,src);
			s.hp*=hp_scale;
			s.add();
		}
	}
}
