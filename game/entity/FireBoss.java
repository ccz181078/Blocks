package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import java.util.Collections;
import java.util.Arrays;
import game.world.World;
import game.block.Block;
import game.block.WaterBlock;
import game.world.*;

public class FireBoss extends NormalAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FireBoss");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.9;}
	public double height(){return 0.9;}
	public double maxHp(){return 1000;}
	@Override
	Group group(){return Group.FIRE;}
	@Override
	public double light(){return 1;}
	public FireBoss(double _x,double _y){
		super(_x,_y);
	}
	public Entity getBall(){return new HE_FireBall();}
	public boolean chkRemove(long t){return false;}
	public AttackFilter getAttackFilter(){return fire_filter;}

	int attackMode=0,attackTime=0;
	float angle=0;
	transient float av=0;

	@Override
	protected float getRotation(){return angle;}
	
	
	@Override
	public void update(){
		super.update();
		if(hp>0)hp=min(maxHp(),hp+0.005);
		
		angle+=av;
		av=(float)max(-10,min(10,av+rnd(-2,2)));
	}
		
	@Override
	public void ai(){
		if(6*hp<maxHp()&&rnd()<0.005){//爆炸
			for(int i=0;i<360;i+=12){
				double r=Math.toRadians(i);
				hp=max(0,hp-10);
				throwEntFromCenter(new FireBall(),x+cos(r),y+sin(r),0.2);
			}
		}
		if(rnd()<0.8)return;
		double md=1e10;
		Agent tg=null;
		Enemy e=es[rndi(0,es.length-1)];
		if(e!=null)tg=e.w;
		else{
			for(Player p:World.cur.getPlayers()){
				double d=abs(x-p.x)+abs(y-p.y);
				if(d<md){
					md=d;
					tg=p;
				}
			}
			if(md>20)return;
		}
		
		switch (attackMode){
			case 0:
				double n=rnd(-hp/4-200,14);
				attackMode=n<0?0: n<5?1: n<9?2: 3;
				return;
			case 1://落火球
				if(World.cur.getGroundY(f2i(tg.x))<tg.y+15){
					for(int i=0;i<4;++i){
						FireBall f=new FireBall();
						f.x=tg.x-2+rnd(5);
						f.y=min(128,tg.y+15)-rnd(5);
						f.yv=-0.2;
						f.add();
					}
				}
				attackMode=0;
				return;
			case 2:{//扫射
				double r=Math.toRadians(attackTime*12-45);
				throwEntFromCenter(new FireBall(),x+cos(r)*dir,y+sin(r),0.2);
				throwEntFromCenter(new FireBall(),x-cos(r)*dir,y-sin(r),0.2);}
				++attackTime;
				if(attackTime>15){
					attackTime=0;
					attackMode=0;
				}
				return;
			case 3://散布火球
				for(int i=0;i<=5;++i){
					FireBall f=new FireBall();
					f.x=tg.x+rnd(-10,10);
					f.y=tg.y+rnd(-5,5);
					f.hp=rnd(4,8);
					if(!World.cur.get(f.x,f.y).isSolid())f.add();
				}
				attackMode=0;
				return;
		}
			
		
	}
	void touchBlock(int px,int py,Block b){
		if(!removed&&(b.rootBlock() instanceof WaterBlock)){
			/*double x1=x+rnd(-3,3),y1=y+rnd(1,3);
			while(!World.cur.noBlock(x1,y1,width(),height())){
				x1=x+rnd(-3,3);
				y1=y+rnd(1,3);
			}
			x=x1;
			y=y1;*/
			hp-=20;
			World.cur.setAir(px,py);
			explode(hp/100+5);
		}else super.touchBlock(px,py,b);
		
	}
	void touchAgent(Agent e){
		if(e instanceof Player&&attackTime==0)attackMode=2;
	}
	void onKill(){
		explode(50);
		new game.item.FireBall().drop(x,y,rndi(15,30));
		super.onKill();
	}
	@Override
	public void onKilled(Source src){
		World.showText(">>> "+getName()+"死于"+src);
		super.onKilled(src);
	}
	
}


