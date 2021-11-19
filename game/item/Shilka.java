package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class Shilka extends Vehicle{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/Shilka");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/Shilka_",8);
	static BmpRes Jetbmps[]=BmpRes.load("Entity/JetFire_",4);
	
	SpecialItem<Pipeline_5> gun=new SpecialItem<>(Pipeline_5.class);
	
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,gun);
	}

	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){
		if(damage<1000)return bmp_armor[0];
		if(damage<2000)return bmp_armor[1];
		if(damage<3000)return bmp_armor[2];
		return bmp_armor[3];
	}
	public BmpRes getTankBmp(){
		int v = 0;
		if( walk != 0 ) v = 4;
		if(damage<1000)return bmp_armor[0+v];
		if(damage<2000)return bmp_armor[1+v];
		if(damage<3000)return bmp_armor[2+v];
		return bmp_armor[3+v];
	}
	public BmpRes getJetBmp(){
		return Jetbmps[rndi(0,3)];
	}
	public BmpRes getGunBmp(){
		Pipeline_5 w=gun.get();
		if(w==null)return null;
		return w.getBmp();
	}
	public double a=0;
	public boolean flag = false;
	public int maxDamage(){return 4000;}
	
	public double mass(){return walk==0?3:5;}
	public double width(){return 0.95;}
	public double height(){return 0.82;}
	int walk = 0;
	public double maxvr(){return walk==0?1:0;}
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		if(!hasEnergy(5))return true;
		double xd=tx-hu.x,yd=ty-(hu.y+0.23);
		if(rnd()<0.01){loseEnergy(1);damage+=1;}
		if(abs(yd)+abs(xd)>0.001){
			double b = atan2(yd,xd),a0=a;
			if(yd<0&&xd<0)b+=PI*2;
			if(abs(b-a)<0.2)a=b;
			else if(b<a)a-=0.2;
			else a+=0.2;
			if(a<0)a=0;
			if(a>PI)a=PI;
			if(World.cur.get(hu.x+1.5*cos(a),hu.y+1.5*sin(a)).rootBlock().isSolid())a=a0;
			if(World.cur.get(hu.x+1.5*cos(a),hu.y+0.5+1.5*sin(a)).rootBlock().isSolid())a=a0;
			if(abs(b-a)<0.2)shoot(hu,a,tan(a));
		}
		return true;
	}
	
	public void shoot(Human hu,double a,double b){
		Pipeline_5 w=gun.get();
		if(w!=null)w.shoot(hu,a,b,this);
	}
	
	public Entity test_shoot(Human hu,double a,Item ammo){
		Entity.beginTest();
		Pipeline_5 w=gun.get();
		if(w!=null)w.test_shoot(hu,a,tan(a),this,ammo);
		return Entity.endTest();
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		if(abs(tx-hu.x)<=width()&&abs(ty-hu.y)<=height())
			if( walk == 0 )
				walk = 40;
			else
				walk = 39;
		else
			shoot(hu,a,tan(a));
		return true;
	}
	
	public float maxReload(){
		return 3.3f;
	}
	public double getShootX(Human hu,double a,int cnt){
		return hu.x+1.6*cos(a)-0.15*(cnt-3)*sin(a);
	}
	public double getShootY(Human hu,double a,int cnt){
		return hu.y+0.15*(cnt-3)*cos(a)+1.6*sin(a)+0.23;
	}
	public void onUpdate(Human w){
		//World.showText("damage="+damage);
		if( walk > 0 && walk < 40 ) walk--;
		{
			Pipeline_5 p=gun.get();
			if(p!=null)p.onUpdate(w,this);
		}
		if(!checkEnergy(5,w))return;
		double c=0;
		if( walk == 0 ){
			if(w.xdir!=0&&w.ydep<0){
				w.xa+=w.xdir*0.024;
				c+=0.03;
				if(rnd()<0.002)++damage;
			}
			if(w.xdir!=0){
				w.xa+=w.xdir*0.0009;
				c+=0.02;
				if(rnd()<0.001)++damage;
			}
			if(w.ydir!=0){
				w.ya+=w.ydir*0.009;
				if(w.ydep>=0&&!flag)
					loseEnergy(3);
				else if(w.ydep<0)flag = false;
				c+=0.05;
				if(rnd()<0.002)++damage;
			}
			if(c>0){
				if(rnd()<c){
					loseEnergy(1);
					if(rnd()<0.01)++damage;
				}
			}
		}else{
			//if(w.ydep!=0)w.xf+=0.6;
			if(w.ydep!=0)w.xf+=50;
			if(w.xdep!=0)w.yf+=50;
			if(w.ydep<0)w.f+=50;
		}
	}
	public double getJumpAcc(Human h,double v){return walk==0?v:0;}

	public Attack transform(Attack a){
		double t = 1;
		if(!hasEnergy(1))t*=3;
		if(walk!=0)t*=0.25;
		int v=rf2i(a.val*t);
		//System.out.println("v2:"+a.getClass()+" "+a.val);
		if(a instanceof FireAttack){
			damage+=v;
			a.val*=0.02*t;
		}else if(a instanceof DarkAttack){
			damage+=v;
			a.val*=0.1*t;
			//game.entity.Text.gen(50,50,"a="+a.val,null);
		}else if(a instanceof EnergyAttack){
			damage+=v;
			a.val=0;
		}else{
			damage+=rf2i(a.val*t*((NormalAttack)a).getWeight(this));
			//if(a.val>0)System.out.println("v2:"+v);
			a.val=0;
		}
		return super.transform(a);
	}
	public double onImpact(Human h,double v){
		v=max(0,v-200);
		//if(v>0)System.out.println("v1:"+v);
		damage+=rf2i(v*5/10/10);
		return v*1.2/10/10;
	}
	public void onBroken(double x,double y,Agent w){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,80,0.1,3,src);
			Spark.explode_adhesive(x,y,0,0,20,0.05,12,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getTankBmp());
		super.onBroken(x,y,w);
	}
	@Override
	public void touchAgent(Human w,Agent a){
		double difx=w.xv-a.xv,dify=w.yv-a.yv;
		a.onAttacked((difx*difx+dify*dify)*70,SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		cv.save();{
			cv.translate(0,0.23f);
			cv.rotate((float)(a*180/PI));
			BmpRes bmp=getGunBmp();
			if(bmp!=null)bmp.draw(cv,0.45f,0,0.9f,0.37f);
		}cv.restore();
		getTankBmp().draw(cv,0,0,1,(float)0.75);
		if(walk==0)
		if(hu.xdir>0)
			getJetBmp().draw(cv,-1.24f,-0.33f,0.4f,0.16f);
		else if(hu.xdir<0){
			cv.save();{
				cv.rotate(180);
				getJetBmp().draw(cv,-1.24f,+0.33f,0.4f,0.16f);
			}cv.restore();
		}
	}
}
