package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;
import game.block.Block;
import game.block.BlockAt;
import java.io.*;
import graphics.Canvas;
import android.util.*;
import util.BmpRes;

public abstract class Entity implements Serializable{
	private static final long serialVersionUID=1844677L;
	public double x,y,xv,yv,hp;//postion,velocity,health
	public boolean removed=false;
	public Agent src=null;
	public transient double xdep,ydep,xa,ya;//acceleration
	public transient double xf,yf,f,left,top,right,bottom,V;
	public transient double inblock,anti_g;
	public transient boolean in_wall,climbable;
	public BmpRes getBmp(){return null;}
	
	public final void move(){
		final double max_v=0.3;
		updBlock();
		xv+=xa;yv+=ya;
		double v=sqrt(xv*xv+yv*yv);
		if(v>max_v){
			xv=xv/v*max_v;
			yv=yv/v*max_v;
		}
		if(src!=null){
			if(src.removed)src=null;
		}
	}
	
	//移除实体
	final void remove(){
		hp=0;
		removed=true;
	}
	
	//移除实体，并调用onKill
	final void kill(){
		if(!removed){
			onKill();
			remove();
		}
	}
	
	//产生火球的爆炸
	public void explode(double v){
		int c=min(30,rf2i(v*2));
		for(int i=0;i<c;++i){
			double xv=rnd(-1,1),yv=rnd(-1,1),v2=xv*xv+yv*yv;
			if(v2>0.1&&v2<1){
				FireBall f=new FireBall();
				f.x=x+rnd(-0.1,0.1);
				f.y=y+rnd(-0.1,0.1);
				f.xv=xv;
				f.yv=yv;
				f.add();
			}
		}
	}
	
	//前更新
	//每次世界更新时，在所有实体更新前执行
	public void update0(){
		xa=ya=0;
		inblock=0;
		climbable=false;
		xf=yf=f=anti_g=0;
		double W=width(),H=height();
		left=x-W;
		right=x+W;
		top=y+H;
		bottom=y-H;
		V=W*H*4;
	}
	public boolean chkBlock(){return true;}//是否与方块接触
	public boolean chkRigidBody(){return true;}//是否与方块进行刚体碰撞检测
	
	
	//更新
	public void update(){
		boolean chk_block=chkBlock();
		game.world.NearbyInfo ni=World._.getNearby(x,y,width(),height(),chk_block,false,true);
		for(Agent a:ni.agents)if(a!=this)if(hitTest(a))touchAgent(a);
		if(chk_block)
		for(int y=0;y<ni.blocks.length;++y){
			Block[] b=ni.blocks[y];
			for(int x=0;x<b.length;++x){
				b[x].touchEnt(ni.xl+x,ni.yl+y,this);
				touchBlock(ni.xl+x,ni.yl+y,b[x]);
				if(removed)return;
			}
		}
	}
	
	private double mov(double vd_0){
		double vd=1;
		double xt=2,yt=2;
		if(xv!=0)vd=min(vd,xt=(xv-xdep)/xv);
		if(yv!=0)vd=min(vd,yt=(yv-ydep)/yv);
		double vd0=vd;
		vd=min(vd,vd_0);
		double k=(abs(xv)+abs(yv))*vd;
		if(k>1e-4)vd*=(k-1e-4)/k;
		else vd=0;
		if(!in_wall){
			x+=xv*vd;
			y+=yv*vd;
		}
		if(vd0==xt&&xdep!=0)xv=0;else if(xv!=0)xdep=0;
		if(vd0==yt&&ydep!=0)yv=0;else if(yv!=0)ydep=0;
		return vd;
	}
	
	public final void updBlock(){
		if(chkRigidBody()){
			game.world.NearbyInfo ni=World._.getNearby(x+xv/2,y+yv/2,width()+abs(xv/2),height()+abs(yv/2),true,false,false);

			in_wall=false;
			xdep=ydep=0;
			
			for(int y=0;y<ni.blocks.length;++y){
				Block[] b=ni.blocks[y];
				for(int x=0;x<b.length;++x){
					b[x].rigidBodyHitTest(ni.xl+x,ni.yl+y,this);
				}
			}
			
			double vd=1-mov(1);

			for(int y=0;y<ni.blocks.length;++y){
				Block[] b=ni.blocks[y];
				for(int x=0;x<b.length;++x){
					b[x].rigidBodyHitTest(ni.xl+x,ni.yl+y,this);
				}
			}
			mov(vd);
			upd_v();
			
		}else{
			x+=xv;
			y+=yv;
			upd_v();
		}

		ya-=gA()*max(0,1-anti_g);
		if(in_wall)xv=yv=0;
	}
	private void upd_v(){
		double f1=friction();
		xf*=f1;yf*=f1;f*=f1;
		xv*=(1-min(1,xf))*(1-min(1,f));
		yv*=(1-min(1,yf))*(1-min(1,f));		
	}
	double friction(){return 1;}//摩擦力
	public AttackFilter getAttackFilter(){return null;}//攻击过滤器
	public void onAttacked(Attack att){//被攻击
		hp-=att.val;
	}
	private void _onAttacked(Attack att){
		AttackFilter af=getAttackFilter();
		if(af!=null)att=af.transform(att);
		if(att!=null)onAttacked(att);
	}
	public final void onAttacked(double v,Agent w){//普通攻击
		_onAttacked(new NormalAttack(w,v));
	}
	public final void onAttackedByEnergy(double v,Agent w){//能量石攻击
		_onAttacked(new EnergyAttack(w,v));
	}
	public final void onAttackedByFire(double v,Agent w){//火/热攻击
		_onAttacked(new FireAttack(w,v));
	}
	public final void onAttackedByDark(double v,Agent w){//影/空间攻击
		_onAttacked(new DarkAttack(w,v));
	}
	void touchBlock(int x,int y,Block b){}//事件：接触方块
	boolean hitTest(Entity ent){//碰撞检测
		return !removed&&!ent.removed&&
			abs(x-ent.x)<=width()+ent.width()&&
			abs(y-ent.y)<=height()+ent.height();
	}
	void touchAgent(Agent ent){//事件：接触实体
		double xd=x-ent.x,yd=y-ent.y,d=0.035/(abs(xd)+abs(yd)+1e-8);
		double d1=d/mass(),d2=d/ent.mass();
		xa+=xd*d1;
		ya+=yd*d1;
		ent.xa-=xd*d2;
		ent.ya-=yd*d2;
	}
	public boolean isDead(){return hp<=0||removed;}
	public final void onDestroy(){
		if(!removed){
			onKill();
			remove();
		}
	}
	void onKill(){}//事件：被移除
	public double width(){return 0.1;}//half of width
	public double height(){return 0.1;}//half of height
	double mass(){return 1;}//mass
	double gA(){return 0.03;}//gravitational acceleration
	public void draw(Canvas cv){//绘制
		float w=(float)width();
		float h=(float)height();
		BmpRes bmp=getBmp();
		if(bmp!=null){
			if(rotByVelDir()){
				cv.save();
				if(yv!=0||xv!=0)cv.rotate((float)(atan2(yv,xv)*180/PI));
				bmp.draw(cv,0,0,w,h);
				cv.restore();
			}else bmp.draw(cv,0,0,w,h);
		}
	}
	public boolean rotByVelDir(){return false;}//绘制时是否根据速度方向旋转
	public void add(){World._.newEnt(this);}//add to world
	protected static AttackFilter
	energy_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof EnergyAttack)?null:a;
	}},fire_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof FireAttack)?null:a;
	}},dark_filter=new AttackFilter(){public Attack transform(Attack a){
		return (a instanceof DarkAttack)?null:a;
	}};
}
