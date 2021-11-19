package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

abstract class WheelVehicle extends Vehicle{
	public abstract BmpRes getBmp();
	public abstract BmpRes getArmorBmp();
	public double getJumpAcc(Human h,double v){return flag==0?v:0;}
	
	public abstract double hardness();
	public int maxDamage(){return 5000;}
	
	public abstract double mass();
	public double width(){return 0.95;}
	public double height(){return 0.95;}
	int flag = 0, state_cd=0;
	public boolean onArmorClick(Human a,double tx,double ty){
		if( abs(tx-a.x) < width() && abs(ty-a.y) < height() ){
			if( flag == 0 )
				flag = 1;
			else if(state_cd==0)state_cd=30;
			return true;
		}
		a.dir=tx>a.x?1:-1;
		if(a.getCarriedItem().get() instanceof ShootableTool)flag=1;
		return false;
	}
	public boolean onArmorLongPress(Human a,double tx,double ty){
		a.dir=tx>a.x?1:-1;
		if(a.getCarriedItem().get() instanceof ShootableTool)flag=1;
		return false;
	}
	@Override
	public boolean rotByVelDir(){return flag==0;}
	
	public void onUpdate(Human w){
		EnergyCell e=ec.get();
		if(!checkEnergy(5,w))return;
		double c=0;
		if(state_cd==0&&(w.xdir!=0||w.ydir!=0)&&flag==1)state_cd=30;
		if(state_cd==1)flag=0;
		if(flag==0)state_cd=0;
		state_cd=max(0,min(30,state_cd-1));
		if(flag==0){
			int dx=f2i(w.x+rnd(-1,1)),dy=f2i(w.y+rnd(-1,1));
			World.cur.get(dx,dy).des(dx,dy,(0.3+w.xv*w.xv+w.yv*w.yv+w.av*w.av)*10,w);
		
			int xdir=w.xdir;
			if(xdir==0&&w.xdep!=0)xdir=(w.xdep>0?1:-1);
			
			if(w.ydep!=0||w.xdep!=0)w.xa+=xdir*0.006;
			else w.xa+=xdir*0.003;
			if(xdir!=0)c+=0.04;
			
			
			int ydir=w.ydir;
			if(ydir==0&&w.ydep!=0)ydir=(w.ydep>0?1:-1);
			
			if(w.xdep!=0&&w.ydep>=0&&ydir==0)w.ya+=w.gA();
			else if(w.ydep!=0||w.xdep!=0)w.ya+=ydir*0.04;
			else w.ya+=ydir*0.01;
			//if(ydir!=0)c+=0.04;
			
			//if(w.xdep!=0||w.ydep!=0)w.climbable=true;
			if(w.xdir!=0)w.aa+=(w.ydep>0?1:-1)*w.xdir*0.01;
		
		}
		
		if(c>0){
			if(rnd()<c*10){
				loseEnergy(2);
				if(rnd()<0.01)++damage;
			}
		}
	}

	public Attack transform(Attack a){
		//a.val = 0;
		if(a instanceof NormalAttack){
			a.val*=((NormalAttack)a).getWeight(this);
		}
		if(a!=null){
			damage+=rf2i(a.val);
			a.val = 0;
		}
		return a;
	}
	public double onImpact(Human h,double v){
		//v=max(0,v-100)*0.005;
		v = 0;
		return v;
	}
	public double maxvr(){return flag==1?0:0.2;}
	
	public abstract double touchAttackValue();
	@Override
	public void touchAgent(Human w,Agent a){
		a.onAttacked(w.intersection(a)*(max(0.,w.v2rel_rnd(a)-0.04)*touchAttackValue()),SourceTool.item(w,this),this);
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		float a=hu.getRotation();
		cv.save();
		cv.rotate(a);
		getArmorBmp().draw(cv,0,0,(float)width(),(float)height());
		cv.restore();
	}
}

public class HDBox extends WheelVehicle{
	private static BmpRes bmp[]=BmpRes.load("Armor/HDBox_",2);
	public BmpRes getBmp(){return bmp[flag];}
	public BmpRes getArmorBmp(){
		return bmp[flag];
	}
	public double mass(){return 40;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public double touchAttackValue(){return 5000;}
	public void onUpdate(Human w){
		super.onUpdate(w);
		
		if(flag==1){
			if(w.ydep!=0)w.xf+=5;
			if(w.xdep!=0)w.yf+=5;
			if(w.ydep<0)w.f+=5;
			double v=max(0,w.xv*w.xv+w.yv*w.yv-0.2);
			int k=rf2i(max(0,min(50,100*v)));
			for(int i=0;i<k;++i){
				int x1=f2i(w.x+rnd_gaussion()*sqrt(1+k*0.1)),y1=f2i(w.y+rnd_gaussion()*sqrt(1+k*0.1));
				game.block.Block b=game.world.World.cur.get(x1,y1);
				if(!b.isCoverable()){
					b.fall(x1,y1,w.xv,w.yv);
					w.impulse(w.xv,w.yv,-1);
					damage+=1;
				}
			}
		}

	}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(this);
		if(true){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);

			Source ex=SourceTool.explode(src);
			for(int i=0;i<20;++i){
				double xv=rnd(-1,1),yv=rnd(-1,1),v=sqrt(xv*xv+yv*yv)+1e-8;
				xv/=v;yv/=v;
				if(v<1){
					new game.entity.Bullet(new game.item.Bullet_HD())
					.initPos(x+xv*0.2,y+yv*0.2,xv*2,yv*2,ex)
					.add();
				}else{
					--i;
					continue;
				}
			}			
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y);
	}
}

class IronWheel extends WheelVehicle{
	private static BmpRes bmp[]=BmpRes.load("Armor/IronWheel_",2);
	public BmpRes getBmp(){return bmp[flag];}
	public BmpRes getArmorBmp(){
		return bmp[flag];
	}
	public double maxvr(){return flag==1?0:0.4;}
	public double mass(){return 8;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public double touchAttackValue(){return 500;}
	public void onBroken(double x,double y){
		new Iron().drop(x,y,util.MathUtil.rndi(10,20));
		DroppedItem.dropItems(toArray(),x,y);
		Source src=SourceTool.item(this);
		if(true){
			Spark.explode(x,y,0,0,60,0.1,3,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);

			Source ex=SourceTool.explode(src);		
		}
		Fragment.gen(x,y,width(),height(),4,4,12,getBmp());
		super.onBroken(x,y);
	}
}

class LauncherWheel extends HDBox{
	private static BmpRes bmp=new BmpRes("Armor/LauncherWheel");
	public BmpRes getBmp(){return bmp;}
	public BmpRes getArmorBmp(){return bmp;}
	
	public int energyCost(){return 1000;}
	
	public void onUpdate(Human w){
		flag=0;
		super.onUpdate(w);
		reload=min(maxReload(),reload+0.02f);
	}
	
	public Entity test_shoot(Human hu,double a,Item ammo){
		Entity.beginTest();
		double x=hu.x+1.6*cos(a),y=hu.y+1.6*sin(a);
		double b=tan(a);
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,mv2());
		return Entity.endTest();
	}
	public void shoot(Human hu,double a,double b){
		if(!hasEnergy(energyCost()))return;
		if(reload<maxReload()*0.9)return;
		if(hu.items.getSelected()==null)return;
		if(hu.items.getSelected().isEmpty())return;
		reload=0;
		
		Item ammo = hu.items.getSelected().popItem();
		double x=hu.x+1.6*cos(a),y=hu.y+1.6*sin(a);
		ammo.onLaunchAtPos(hu,a>PI/2?-1:1,x,y,b,mv2());
		loseEnergy(energyCost());
	}
	
	public boolean onArmorClick(Human a,double tx,double ty){
		a.dir=tx>a.x?1:-1;
		double a0=a.a;
		a0=a0%(2*PI);
		if(a0<-PI/2)a0+=2*PI;
		if(a0>PI*1.5)a0-=2*PI;
		shoot(a,a0,tan(a0));
		return true;
	}
	public boolean onArmorLongPress(Human a,double tx,double ty){
		a.dir=tx>a.x?1:-1;
		double dx=tx-a.x,dy=ty-a.y;
		double da=(atan2(dy,dx)-(a.a+a.av*5))%(2*PI);
		if(da<-PI)da+=2*PI;
		if(da>PI)da-=2*PI;
		a.aa+=0.01*a.av+0.01*da;
		return true;
	}
}
