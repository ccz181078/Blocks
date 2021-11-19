package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Tank extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Tank");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/Tank_",4);
	private static BmpRes bmp_gun[]=BmpRes.load("Armor/Gun_",2);
	private static BmpRes bmp_speed[]=BmpRes.load("Armor/Speed_",4);
	static BmpRes Jetbmps[]=BmpRes.load("Entity/JetFire_",4);
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){
		if(damage<2500)return bmp_armor[0];
		if(damage<5000)return bmp_armor[1];
		if(damage<7500)return bmp_armor[2];
		return bmp_armor[3];
	}
	public BmpRes getTankBmp(){
		if(damage<2500)return bmp_armor[0];
		if(damage<5000)return bmp_armor[1];
		if(damage<7500)return bmp_armor[2];
		return bmp_armor[3];
	}
	public BmpRes getSpeedBmp(){
		if(max_vr>3.5f)return bmp_speed[3];
		if(max_vr>2.5f)return bmp_speed[2];
		if(max_vr>1.5f)return bmp_speed[1];
		return bmp_speed[0];
	}
	public BmpRes getJetBmp(){
		return Jetbmps[rndi(0,3)];
	}
	public BmpRes getGunBmp(){return bmp_gun[reload>=fireReloadCost()?0:1];}
	public double a=0.225;
	public boolean flag = false;
	public int maxDamage(){return 10000;}
	public double fireReloadCost(){
		return mode == 1 ? 1.25f : 2f;
	}
	
	public double mass(){return 5;}
	public double width(){return 0.95;}
	public double height(){return 0.6;}
	int mode = 0 , gun_cd = 0;
	public void shoot(Human hu,double a,double b,boolean have_cost){
		//if(have_cost)
		{
			if(!hasEnergy(energyCost()))return;
			if(reload<fireReloadCost())return;
		}
		Item ammo = hu.items.getSelected().popItem();
		if(ammo==null)return;
		if(have_cost) damage+=2;
		double x=hu.x+1.6*cos(a),y=hu.y+0.23+1.6*sin(a);
		if(have_cost) reload -= fireReloadCost();
		if( !( ammo instanceof Bullet ) )
		{
			Spark s=new Spark(0,0);
			s.initPos(x+0.03*cos(a),y+0.03*sin(a),cos(a)*0.5,sin(a)*0.5,hu);
			s.hp*=0.2;
			s.add();
		}
		
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,2*mv2());
		loseEnergy(energyCost());
	}
	public int energyCost(){return 48;}
	public Entity test_shoot(Human hu,double a,Item ammo){
		Entity.beginTest();
		double x=hu.x+1.6*cos(a),y=hu.y+0.23+1.6*sin(a);
		double b=tan(a);
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,2*mv2());
		return Entity.endTest();
	}
	
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		if(damage>7500)return true;
		if(!hasEnergy(5))return true;
		rot_time=5;
		double xd=tx-hu.x,yd=ty-(hu.y+0.23),t=1;
		if( mode == 1 ) t = 4;
		if(rnd()<0.01){loseEnergy(1);damage+=1;}
		if(abs(yd)+abs(xd)>0.001){
			double b = atan2(yd,xd),a0=a;
			if(yd<0&&xd<0)b+=PI*2;
			if(abs(b-a)<0.05*t)
			{
				a=b;
				if(a<-0.15)a=-0.15;
				if(a>PI+0.15)a=PI+0.15;
				if(hu.getCarriedItem().getAmount()>1){
					Item ammo = hu.items.getSelected().get();
					if(ammo!=null)
						if(ammo instanceof Bullet && gun_cd == 0)
						{
							shoot(hu,a,tan(a),false);
							gun_cd = 8;
						}
				}
			}
			else if(b<a)a-=0.05*t;
			else a+=0.05*t;
			if(a<-0.15)a=-0.15;
			if(a>PI+0.15)a=PI+0.15;
			if(World.cur.get(hu.x+1.5*cos(a),hu.y+0.23+1.5*sin(a)).rootBlock().isSolid())a=a0;
		}
		return true;
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		if( abs(hu.x-tx)<width() && abs(hu.y-ty)<height() )
			mode ^= 1;
		else
			shoot(hu,a,tan(a),true);
		return true;
	}
	
	public float maxReload(){
		return 5;
	}
	double max_vr=0;
	int dir0=0;
	boolean same_dir=false;
	public double maxvr(){return same_dir?max_vr:0;}
	public double frictionXr(){return 1;}

	int rot_time=0;

	public void onUpdate(Human w){
		//World.showText("damage="+damage);
		max_vr*=0.997;
		same_dir=false;
		if(gun_cd!=0)gun_cd--;
		reload+=0.033f;
		if(reload>maxReload()-(float)abs(w.xv)*7.5f)reload=maxReload()-(float)abs(w.xv)*7.5f;
		if(reload<0)reload=0;
		if(!checkEnergy(5,w))return;
		
		
		{
			if(rot_time>0){
				--rot_time;
				if(w.getCarriedItem().getAmount()!=1)rot_time=0;
			}
			if(rot_time>0){
				double c=cos(a),s=sin(a),r=1.6;
				double x=c*r,y=s*r+0.23;
				updateRotatingItem(w,w.getCarriedItem(),x,y);
			}
		}
		
		if(w.xdir!=0&&dir0==w.xdir){
			if(mode==1)max_vr+=0.004;
			else max_vr+=0.02;
			same_dir=true;
		}else{
			max_vr*=0.97;
			if(max_vr<0.05)dir0=w.xdir;
		}
		double c=0;
		double t = 1f;
		if( mode == 1 ) t *= 0.1;
		if(w.xdir!=0&&w.ydep<0){
			//w.xa+=w.xdir*0.075*t/w.mass();
			c+=0.06;
			if(rnd()<0.002)++damage;
		}
		if(w.xdir!=0){
			w.xa+=w.xdir*0.0015*t/w.mass();
			c+=0.03;
			if(rnd()<0.001)++damage;
		}
		if(w.ydir!=0){
			w.ya+=w.ydir*0.016*t;
			if(w.ydep>=0&&!flag&&mode==0){
				flag = true;
				for(int i=0;i<5;i++){
					Spark s=new Spark(0,0);
					s.initPos(w.x+rnd_gaussion()*0.6,w.y-0.65,rnd_gaussion()*0.0008,-0.001,w);
					s.add();
				}
				loseEnergy(3);
			}else if(w.ydep<0)flag = false;
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
			damage+=rf2i(a.val*0.7*t);
			a.val*=0.006*t;
		}else if(a instanceof DarkAttack){
			damage+=rf2i(a.val*t);
			a.val*=0.03*t;
			//game.entity.Text.gen(50,50,"a="+a.val,null);
		}else if(a instanceof EnergyAttack){
			damage+=rf2i(a.val*0.9*t);
			a.val=0;
		}else{
			damage+=rf2i(a.val*0.2*t*((NormalAttack)a).getWeight(this));
			a.val=0;
		}
		return super.transform(a);
	}
	public double onImpact(Human h,double v){
		v=max(0,v-300);
		damage+=rf2i(v*5/10/10);
		return v*1.2/10/10;
	}
	public void onBroken(double x,double y,Agent w){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			Spark.explode_adhesive(x,y,0,0,20,0.05,18,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getTankBmp());
		super.onBroken(x,y,w);
	}
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(max(0,w.v2rel(a)-0.05)*200,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		if(rot_time>0){
			double c=cos(a),s=sin(a),r=1.6;
			double x=c*r,y=s*r+0.23;
			drawRotatingItem(cv,hu.getCarriedItem(),x,y);
		}
		cv.save();{
			cv.translate(0,0.23f);
			cv.rotate((float)(a*180/PI));
			getGunBmp().draw(cv,0.45f,0,0.9f,0.11f);
		}cv.restore();
		getTankBmp().draw(cv,0,0,1,(float)0.6);
		if(hu.xdir>0){
			getJetBmp().draw(cv,-1.24f,-0.2f,0.4f,0.14f);
		}
		else if(hu.xdir<0){
			cv.save();{
				cv.rotate(180);
				getJetBmp().draw(cv,-1.24f,+0.2f,0.4f,0.14f);
			}cv.restore();
		}
		if(max_vr>0.5f)
		{
			if(dir0<0){
				getSpeedBmp().draw(cv,0,0,0.95f,0.6f);
			}
			else{
				cv.save();{
					cv.rotate(180);
					getSpeedBmp().draw(cv,0,0.384f,0.95f,0.6f);
				}cv.restore();
			}
		}
	}
}
