package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class GuidedBulletManager extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/GuidedBulletManager");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp;}
	protected float maxReload(){return 1.6f;}
	public int maxDamage(){return 6000;}
	@Override
	public double mv2(){return super.mv2()*3;}
	
	public double mass(){return 4.5f;}
	public double width(){return 0.8;}
	public double height(){return 0.8;}
	int flag = 0;
	double a = 0, a0 = 0,av0=0;
	int mode=0;
	public Entity test_shoot(Human hu,double a,Item ammo){
		Entity.beginTest();
		double x=hu.x+2*cos(a),y=hu.y+2*sin(a);
		double b=tan(a);
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,mv2());
		return Entity.endTest();
	}
	public void shoot(Human hu,double a,double b){
		//if(have_cost)
		if(hu.items.getSelected()==null)return;
		if(cd>0||hu.items.getSelected().isEmpty())return;
		double reload_cost=fireReloadCost( hu.items.getSelected().get() );
		if(reload<reload_cost)return;
		reload -= reload_cost;
		
		Item ammo = hu.items.getSelected().popItem();
		cd=getCd(hu.xv*hu.xv+hu.yv*hu.yv,ammo);
		double x=hu.x+1.6*cos(a),y=hu.y+1.6*sin(a);
		//Spark s=new Spark(0,0);
		//s.initPos(x,y,hu.xv,hu.yv,hu);
		//s.hp*=0.5;
		//s.add();
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,mv2());
		loseEnergy(energyCost());
	}
	int cd = 0;

	public double fireReloadCost( Item ammo ){
		if( ammo instanceof game.item.Warhead ) return 0.55f;
		if( ammo instanceof game.block.Block ) return 0.4f;
		if( ammo instanceof RPGItem ) return 0.55f;
		return 0.064f;
	}
	
	public int getCd( double v , Item ammo ){
		int t = 2;
		if( ammo instanceof game.item.Bullet ) return 1 * t;
		if( ammo instanceof game.item.RPGItem ) return 4 * t;
		if( ammo instanceof game.item.Bottle ) return 4 * t;
		if( ammo instanceof game.item.StoneBall ) return 10 * t;
		if( ammo instanceof game.block.Block ) return 4 * t;
		return 2 * t;
	}
	public boolean onArmorClick(Human hu,double tx,double ty){
		//if( abs(tx-a.x) < width() && abs(ty-a.y) < height() )
		//	if( flag == 0 && reload > 0.4 )
		//		flag = 1;
		//a.dir=tx>a.x?1:-1;
		double xd=tx-hu.x,yd=ty-hu.y;
		if(abs(xd)<width()&&abs(yd)<height()){
			mode=(mode+1)%2;
			return true;
		}
		if(abs(yd)+abs(xd)>0.001){
			double b = atan2(yd,xd),a0=a;
			a=b;
			shoot(hu,a,tan(a));
		}
		return true;
	}
	private void fix_a(){
		a=a%(2*PI);
		if(a<-PI/2)a+=2*PI;
		if(a>PI*1.5)a-=2*PI;
	}
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		if(!hasEnergy(5))return true;
		double xd=tx-hu.x,yd=ty-hu.y;
		if(rnd()<0.01){loseEnergy(1);damage+=1;}
		if(abs(yd)+abs(xd)>0.001){
			double b = atan2(yd,xd),a0=a;
			/*double d=(b-a)%(2*PI);
			if(d<-PI)d+=2*PI;
			if(d>PI)d-=2*PI;
			if(abs(d)<0.15)a+=d;
			else if(d>0)a+=0.15;
			else a-=0.15;*/
			a=b;
			
			fix_a();
			//if(World.cur.get(hu.x+1.2*cos(a),hu.y+1.2*sin(a)).rootBlock().isSolid())a=a0;
			//if(abs(d)<0.3)
			shoot(hu,a,tan(a));
		}
		return true;
	}
	
	public void onUpdate(Human w){
		if(!hasEnergy(1))return;
		
		w.fc+=0.02/0.15;
		{
			double ax=cos(a0),ay=sin(a0);
			double c=(ax*w.yv-ay*w.xv);
			w.fc+=(abs(c))/w.mass()+0.06*max(0,hypot(w.xv,w.yv)-0.5);
			av0+=c*0.03-av0*0.1;
			a0=(a0+av0)%(PI*2);
			double d=(a-a0)%(2*PI);
			if(d>PI)d-=PI*2;
			if(d<-PI)d+=PI*2;
			double M=PI/3;
			d=max(-M,min(M,d))-d;
			a+=d;
			a0-=d*0.1;
			w.anti_g+=1;
			
			fix_a();
		}
		double c=0,C=1;
		if(mode==0){
			if(w.xdir!=0){
				w.xa+=w.xdir*0.025*C;
				c+=0.03;
			}
			if(w.ydir!=0){
				w.ya+=w.ydir*0.025*C;
				c+=0.03;
			}
		}else{
			if(w.xdir!=0){
				av0-=w.xdir*0.007*C;
				c+=0.01;
			}
			if(w.ydir!=0){
				w.xa+=w.ydir*cos(a0)*0.025*C;
				w.ya+=w.ydir*sin(a0)*0.025*C;
				c+=0.03;
			}
		}
		if(c>0){
			if(rnd()<c*10){
				loseEnergy(1);
				if(rnd()<0.1)++damage;
			}
		}
		reload += 0.015;
		cd--;
		if( reload > maxReload() ) reload = maxReload();
	}

	public Attack transform(Attack a){
		a=super.transform(a);
		if(a!=null){
			damage+=1.2*rf2i(a.val);
			a.val*=0.02;
		}
		return a;
	}
	public double onImpact(Human h,double v){
		v=max(0,v-20)*0.5;
		damage+=rf2i(v*5);
		return v;
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.v2rel(a)*100,SourceTool.item(w,this),this);
	}
	
	public float getRotation(){
		return 0;
		//return (float)(a0*180/PI-90);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		cv.save();{
			cv.rotate((float)(a0*180/PI));
			bmp.draw(cv,0,0,(float)width(),(float)height());
			if(mode==1)getBmp().draw(cv,0,0,(float)width(),(float)height());
		}cv.restore();
	}
}
