package game.item;

import util.BmpRes;
import graphics.Canvas;
import game.block.Block;
import game.entity.*;
import game.world.World;
import game.item.Item;
import static java.lang.Math.*;
import static util.MathUtil.*;

public abstract class Airship extends Vehicle{
	private static final long serialVersionUID=1844677L;
	public ShowableItemContainer getItems(){return ItemList.create(ec,af1,af2);}
	
	public SpecialItem<AirshipFlank> af1=new SpecialItem<>(AirshipFlank.class),af2=new SpecialItem<>(AirshipFlank.class);
	public float Breload=1;
	
	public double width(){return 0.875;}
	public double height(){return 0.875;}
	public void shoot(Human hu,double tx,double ty){
		if(!hasEnergy(energyCost()))return;
		AirshipFlank af=(tx>hu.x?af1:af2).get();
		if(af==null)return;
		if(reload<fireReloadCost())return;
		Item ammo = hu.items.getSelected().popItem();
		if(ammo==null)return;
		loseEnergy(energyCost());
		reload -= fireReloadCost();
		final double y = hu.y+(ty>hu.y?1.5:-1.5);
		final double x = hu.x+(tx<hu.x?2:-2);
		ammo.onLaunchAtPos(af.ent,(tx>x?1:-1),x,y,(ty-y)/(tx-x),mv2());
		return;
	}
	
	public Entity test_shoot(Human hu,double tx,double ty,Item ammo,boolean is_long){
		Entity.beginTest();
		if(is_long){
			if(Breload>=1){
				int dir = ty>hu.y?1:-1;
				ammo.onLaunchAtPos(hu,dir,hu.x,hu.y+1.8*dir,1e100,mv2());
			}
		}else{
			if(reload>=fireReloadCost()){
				AirshipFlank af=(tx>hu.x?af1:af2).get();
				if(af!=null){
					final double y = hu.y+(ty>hu.y?1.5:-1.5);
					final double x = hu.x+(tx<hu.x?2:-2);
					ammo.onLaunchAtPos(af.ent,(tx>x?1:-1),x,y,(ty-y)/(tx-x),mv2());
				}
			}
		}
		return Entity.endTest();
	}
	
	public boolean onArmorLongPress(Human hu,double tx,double ty){
		if(!hasEnergy(energyCost()))return true;
		if(Breload<1)return true;
		Item ammo = hu.items.getSelected().popItem();
		if(ammo==null)return true;
		loseEnergy(energyCost());
		Breload = 0;
		int dir = ty>hu.y?1:-1;
		ammo.onLaunchAtPos(hu,dir,hu.x,hu.y+1.8*dir,1e100,mv2());
		return true;
	}
	
	public boolean onArmorClick(Human hu,double tx,double ty){
		shoot(hu,tx,ty);
		return true;
	}

	public void onUpdate(Human w){
		AirshipFlank a1=af1.get(),a2=af2.get();
		if(a1!=null){
			a1.ent.update(w);
			if(a1.ent.hp<=0)af1.pop();
		}
		if(a2!=null){
			a2.ent.update(w);
			if(a2.ent.hp<=0)af2.pop();
		}
		int t;
		reload+=0.03f;
		Breload += 0.025f;
		if(reload<0)reload=0;
		if(reload>maxReload())reload=maxReload();
		if(!hasEnergy(5))return;
		double c=0;
		
		if(w.xdir!=0){
			w.xa+=w.xdir*0.00025;
			c+=0.03;
			if(rnd()<0.001)++damage;
		}
		if(w.ydir!=0){
			if(w.ydir<0)
				w.ya+=w.ydir*0.006;
			else
				w.ya+=w.ydir*0.003;
			if(rnd()<0.002)++damage;
		}
		
		if(c>0){
			if(rnd()<c){
				loseEnergy(2);
				if(rnd()<0.05)++damage;
			}
		}
	}

	public double onImpact(Human h,double v){
		if(v<1000)return 0;
		v -= 1000;
		damage+=rf2i(v*0.01);
		return v*0.01;
	}
	@Override
	public void touchAgent(Human w,Agent a){
		if(a instanceof Airship_Flank)((Airship_Flank)a).dock(w);
		else{
			double difx=w.xv-a.xv,dify=w.yv-a.yv;
			a.onAttacked((difx*difx+dify*dify)*10,SourceTool.item(w,this),this);
		}
	}
	
	@Override
	public void draw(graphics.Canvas cv,Human hu){
		getBmp().draw(cv,0,0,(float)width(),(float)height());
	}
	
	public int energyCost(){return 5;}
	
	public abstract BmpRes getBmp();
	public abstract double mass();
	public abstract int maxDamage();
	public abstract double fireReloadCost();
	public abstract double TorpedoReloadCost();
	public abstract float maxReload();
	public abstract Attack transform(Attack a);
	public void onBroken(double x,double y,Agent w){
		AirshipFlank a1=af1.get(),a2=af2.get();
		if(a1!=null)a1.ent.airship=null;
		if(a2!=null)a2.ent.airship=null;
		af1.pop();af2.pop();
		DroppedItem.dropItems(toArray(),x,y);
		
		/*Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,150,0.12,3,src);
			Spark.explode_adhesive(x,y,0,0,100,0.2,90,src);
			ShockWave.explode(x,y,0,0,50,0.1,0.3,src);
			ShockWave.explode(x,y,0,0,50,0.1,0.6,src);
		}*/
		super.onBroken(x,y,w);
		
	}
}

class GlassAirship extends Airship{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp=new BmpRes("Item/GlassAirship");
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/GlassAirship_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double mass(){return 6;}
	public int maxDamage(){return 20000;}
	public double fireReloadCost(){return 4f;}
	public double TorpedoReloadCost(){return 6f;}
	public float maxReload(){return 8f;}
	public double hardness(){return game.entity.NormalAttacker.QUARTZ;}
	
	public Attack transform(Attack a){
		int t = 1;
		if(!hasEnergy(1))t*=3;
		if(a instanceof EnergyAttack){
			return null;
		}else if(a instanceof FireAttack){
			damage+=rf2i(a.val*0.1)*t;
			a.val*=0.002*t;
		}else if(a instanceof DarkAttack){
			damage+=rf2i(a.val)*t;
			a.val*=0.04*t;
		}else if(a instanceof NormalAttack){
			damage+=rf2i(a.val*10*t*((NormalAttack)a).getWeight(this));
			a.val=0;
		}
		return a;
	}
	
	public void onBroken(double x,double y,Agent w){
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,100,0.12,3,src);
			Spark.explode_adhesive(x,y,0,0,60,0.2,90,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
			ShockWave.explode(x,y,0,0,36,0.1,0.6,src);
		}
		super.onBroken(x,y,w);
	}
}

class WoodenAirship extends Airship{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp_armor[]=BmpRes.load("Armor/WoodenAirship_",1);
	public BmpRes getBmp(){
		return bmp_armor[0];
	}
	public double mass(){return 6;}
	public int maxDamage(){return 10000;}
	public double fireReloadCost(){return 4f;}
	public double TorpedoReloadCost(){return 6f;}
	public float maxReload(){return 8f;}
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	
	public Attack transform(Attack a){
		int v=rf2i(a.val);
		if(a instanceof FireAttack){
			damage+=v*10;
			a.val*=0.2;
		}else if(a instanceof DarkAttack){
			damage+=v;
			a.val*=0.2;
		}else if(a instanceof EnergyAttack){
			damage+=rf2i(a.val*0.5);
		}else{
			damage+=rf2i(a.val*2*((NormalAttack)a).getWeight(this));
			a.val=0;
		}
		return a;
	}
	
	public void onBroken(double x,double y,Agent w){
		Source src=SourceTool.item(w,this);
		if(rnd(0,1)<0.8){
			Spark.explode(x,y,0,0,100,0.12,3,src);
			Spark.explode_adhesive(x,y,0,0,60,0.2,90,src);
			ShockWave.explode(x,y,0,0,40,0.1,0.3,src);
			ShockWave.explode(x,y,0,0,36,0.1,0.6,src);
		}
		super.onBroken(x,y,w);
	}
}
