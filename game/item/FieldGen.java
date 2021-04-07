package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public abstract class FieldGen extends Item{
	private static final long serialVersionUID=1844677L;
	protected double getAreaIn(FieldBuff f,Entity e){
		double xd=min(e.x+e.width(),f.x+f.radius)-max(e.x-e.width(),f.x-f.radius);
		double yd=min(e.y+e.height(),f.y+f.radius)-max(e.y-e.height(),f.y-f.radius);
		return max(0,xd)*max(0,yd);
	}
	protected double getAtkVal(FieldBuff f,Entity e){
		double a=getAreaIn(f,e);
		return a*(1+f.v2rel(e)*100);
	}
	public double light(FieldBuff f){return 0;}
	@Override
	public void onUse(Human a){
		new FieldBuff(a,this,4).add();
	}
	@Override
	public BmpRes getUseBmp(){return field_btn;}
	public void draw(FieldBuff f,graphics.Canvas cv){
		float r=(float)f.radius;
		cv.drawRect(-r,-r,r,r,getColor());
	}
	public void update(FieldBuff f){
		Agent tg=f.target;
		f.hp-=f.v2rel(tg)*20;
		NearbyInfo ni=World.cur.getNearby(f.x,f.y,f.radius,f.radius,false,true,true);
		for(Entity e:ni.ents){
			if(e!=f)touchEnt(f,e);
		}
		for(Agent e:ni.agents){
			if(e!=f.target)touchAgent(f,e);
		}
		if(f.distL2(tg)>1.5)f.kill();
	}
	
	
	public Item clickAt(double x,double y,Agent a){
		for(Agent e:World.cur.getNearby(x,y,0.1,0.1,false,false,true).agents){
			if(e==a)continue;
			new FieldBuff(e,this,2.4).add();
			return null;
		}
		return super.clickAt(x,y,a);
	}
	
	public void touchEnt(FieldBuff f,Entity e){}
	public void touchAgent(FieldBuff f,Agent e){touchEnt(f,e);}
	protected abstract int getColor();
	public boolean autoUse(Human h,Agent a){
		for(Entity e:World.cur.getNearby(h.x,h.y,2,2,false,true,false).ents){
			if(e instanceof FieldBuff&&((FieldBuff)e).target==h)return true;
		}
		if(a==null){
			h.useCarriedItem();
			return true;
		}
		if(max(abs(h.x-a.x),abs(h.y-a.y))<3){
			h.useCarriedItem();
			return true;
		}
		return false;
	}
}

class EnergyField extends FieldGen{
	static BmpRes bmp=new BmpRes("Item/EnergyField");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected int getColor(){return 0xa00080ff;}
	@Override
	public double light(FieldBuff f){return 2*f.radius/f.r0;}
	@Override
	public void touchEnt(FieldBuff f,Entity e){
		e.onAttackedByEnergy(getAtkVal(f,e),f);
		double rxv=e.xv-f.xv,ryv=e.yv-f.yv;
		double xd=e.x-f.x,yd=e.y-f.y;
		double xa=0,ya=0;
		if(xd*rxv<0&&abs(e.y-f.y)<f.radius)xa=-2*rxv;
		if(yd*ryv<0&&abs(e.x-f.x)<f.radius)ya=-2*ryv;
		double m=e.mass();
		f.target.impulse(xa,ya,-m);
		e.impulse(xa,ya,m);
	}
}
class FireField extends FieldGen{
	static BmpRes bmp=new BmpRes("Item/FireField");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected int getColor(){return 0xa0ff8000;}
	@Override
	public void update(FieldBuff f){
		super.update(f);
		int x=f2i(f.x+rnd(-1,1)*f.radius);
		int y=f2i(f.y+rnd(-1,1)*f.radius);
		World.cur.get(x,y).onFireUp(x,y);
	}
	@Override
	public void touchEnt(FieldBuff f,Entity e){
		if(e instanceof Spark){
			double xd=f.x-e.x,yd=f.y-e.y,d=hypot(xd,yd)+1e-8;
			e.xa-=xd/d*0.01;
			e.ya-=yd/d*0.01;
		}else{
			e.onAttackedByFire(getAtkVal(f,e),f);
			if(rnd()<0.1)Spark.explode(e.x,e.y,f.xv,f.yv,1,0.03,1,f);
		}
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(a==null)return false;
		return super.autoUse(h,a);
	}
}
class DarkField extends FieldGen{
	static BmpRes bmp=new BmpRes("Item/DarkField");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected int getColor(){return 0xa0ff00ff;}
	@Override
	public void touchEnt(FieldBuff f,Entity e){
		e.onAttackedByDark(getAtkVal(f,e),f);
		double rxv=e.xv-f.xv,ryv=e.yv-f.yv;
		double xd=e.x-f.x,yd=e.y-f.y;
		double xa=0,ya=0;
		if(xd*rxv<0&&abs(e.y-f.y)<f.radius)xa=(xd<0?2:-2)*f.radius;
		if(yd*ryv<0&&abs(e.x-f.x)<f.radius)ya=(yd<0?2:-2)*f.radius;
		if(xa!=0||ya!=0)new SetRelPos(e,e,xa,ya);
	}
}
class BloodField extends FieldGen{
	static BmpRes bmp=new BmpRes("Item/BloodField");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected int getColor(){return 0x80ff0000;}
	
	@Override
	public void touchEnt(FieldBuff f,Entity e){
		if(e instanceof BloodBall){
			double xd=f.x-e.x,yd=f.y-e.y,d=hypot(xd,yd)+1e-8;
			e.xa+=xd/d*0.1;
			e.ya+=yd/d*0.1;
			e.f+=0.2;
		}
	}
	@Override
	public void touchAgent(FieldBuff f,Agent e){
		if(rnd()<0.2)BloodBall.drop(e,5,f);
	}
	@Override
	public boolean autoUse(Human h,Agent a){
		if(a==null)return false;
		return super.autoUse(h,a);
	}
}
class GreenField extends FieldGen{
	static BmpRes bmp=new BmpRes("Item/GreenField");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected int getColor(){return 0xa000ff00;}
	@Override
	public void update(FieldBuff f){
		super.update(f);
		f.target.addHp(1);
	}
	@Override
	public void touchEnt(FieldBuff f,Entity e){
		e.onAttacked(getAtkVal(f,e),f);
		double rxv=e.xv-f.xv,ryv=e.yv-f.yv;
		double xd=e.x-f.x,yd=e.y-f.y;
		double xa=0,ya=0;
		if(xd*rxv<0&&abs(e.y-f.y)<f.radius)xa=-0.2*rxv;
		if(yd*ryv<0&&abs(e.x-f.x)<f.radius)ya=-0.2*ryv;
		double m=e.mass();
		f.target.impulse(xa,ya,-m);
		e.impulse(xa,ya,m);
	}
}