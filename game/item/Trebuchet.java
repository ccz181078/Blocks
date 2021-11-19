package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Trebuchet extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Armor/Trebuchet_0");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/Trebuchet_",1);
	private static BmpRes bmp_arm=new BmpRes("Armor/TrebuchetArm");
	private static final double D=1.8;
	Entity ball=null;
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){
		return bmp_armor[0];
	}
	public double a=0;
	public boolean flag = false;
	public int maxDamage(){return 4000;}
	
	public double mass(){return 3;}
	public double width(){return 0.95;}
	public double height(){return 0.75;}
	public int energyCost(){return 120;}
	
	long last_press_time=0;
	
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		if(ball==null&&hasEnergy(5)){
			double a1=-atan((tx-hu.x)/max(1e-16,ty-hu.y-0.5));
			a=max(-PI/2,min(PI/2,a*0.9+a1*0.1));
			loseEnergy(1);
		}
		if(ball!=null&&hasEnergy(50)){
			double mv2=EnergyTool.E0*50;
			double c=1;
			if(tx<hu.x)c=-1;
			double m=sqrt(min(mass(),ball.mass())*mv2);
			ball.impulse(c*cos(a),c*sin(a),m);
			hu.impulse(c*cos(a),c*sin(a),-m);
			loseEnergy(50);
			last_press_time=World.cur.time;
			double a1=-atan((ball.x-hu.x)/max(1e-16,ball.y-hu.y-0.5));
			a=max(-PI/2,min(PI/2,a1));
		}
		return true;
	}
	
	private void checkCtrl(Human w,Entity ball){
		if(this.ball!=null)return;
		double d=hypot(ball.width(),ball.height())+D;
		double x0=w.x-d*sin(a),y0=w.y+0.5+d*cos(a);
		double xd=ball.x-x0,yd=ball.y-y0;
		if(hypot(xd,yd)<d&&hasEnergy(10)){
			this.ball=ball;
			loseEnergy(10);
		}
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		if(ball==null){
			SingleItem si=hu.getCarriedItem();
			if(!si.isEmpty()){
				ball=si.popItem().asEnt();
				double w=ball.width(),h=ball.height();
				double d=hypot(w,h)+D;
				ball.initPos(hu.x-d*sin(a),hu.y+0.5+d*cos(a),hu.xv+rnd_gaussion()*1e-8,hu.yv+rnd_gaussion()*1e-8,SourceTool.launch(hu)).add();
			}else{
				double x=hu.x-(D+1)*sin(a),y=hu.y+0.5+(D+1)*cos(a);
				game.world.NearbyInfo ni=World.cur.getNearby(x,y,1,1,false,true,true);
				for(Entity e:ni.ents){
					checkCtrl(hu,e);
				}
				for(Agent e:ni.agents){
					checkCtrl(hu,e);
				}
			}
		}
		return true;
	}

	public void onUpdate(Human w){
		//World.showText("damage="+damage);
		if(!checkEnergy(5,w))return;
		if(ball!=null){
			
			double d=hypot(ball.width(),ball.height())+D;
			double x0=w.x-d*sin(a),y0=w.y+0.5+d*cos(a);
			double xd=ball.x-x0,yd=ball.y-y0;
			if(last_press_time==0){
				xd+=(ball.xv-w.xv)*0.3;
				yd+=(ball.yv-w.yv)*0.3;
			}
			if(hypot(xd,yd)>d)ball=null;
			else if(ball.isRemoved())ball=null;
			else{
				double m=min(mass(),ball.mass())*hypot(xd,yd);
				if(last_press_time!=0&&last_press_time<World.cur.time){
					ball=null;
				}else{
					ball.impulse(xd,yd,-m);
					w.impulse(xd,yd,m);
				}
			}
		}
		if(ball==null)last_press_time=0;
		double c=0;
		double t = 1f;
		if(w.xdir!=0&&w.ydep<0){
			w.xa+=w.xdir*0.05*t;
			c+=0.06;
			if(rnd()<0.002)++damage;
		}
		if(w.ydir!=0){
			w.ya+=w.ydir*0.012*t;
			c+=0.1;
			if(rnd()<0.002)++damage;
		}
		if(c>0){
			if(rnd()<c){
				loseEnergy(2);
				if(rnd()<0.05)++damage;
			}
		}
	}

	public Attack transform(Attack a){
		int t = 1;
		if(!hasEnergy(1))t*=3;
		int v=rf2i(a.val);
		if(a instanceof FireAttack){
			damage+=a.val*0.8*t;
			a.val*=0.01*t;
		}else if(a instanceof DarkAttack){
			damage+=a.val*t;
			a.val*=0.05*t;
			//game.entity.Text.gen(50,50,"a="+a.val,null);
		}else if(a instanceof EnergyAttack){
			damage+=a.val*0.8*t;
			a.val=0;
		}else{
			damage+=rf2i(a.val*0.6*t*((NormalAttack)a).getWeight(this));
			a.val=0;
		}
		return super.transform(a);
	}
	public double onImpact(Human h,double v){
		v=max(0,v-300);
		damage+=rf2i(v*5/10/10);
		return v*1.2/10/10;
	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			Spark.explode_adhesive(x,y,0,0,20,0.05,18,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getArmorBmp());
		super.onBroken(x,y);
	}
	
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(max(0,w.v2rel(a)-0.05)*50,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		cv.save();
		cv.translate(0,0.5f);
		cv.rotate((float)(a*180/PI));
		bmp_arm.draw(cv,0,0.75f,0.1875f,1f);
		cv.restore();
		getArmorBmp().draw(cv,0,0,1,0.75f);
	}
}
