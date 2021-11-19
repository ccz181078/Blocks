package game.entity;

import util.BmpRes;
import game.item.Airship;
import game.item.AirshipFlank;
import game.item.Armor;
import game.item.FlankArmor;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;

public class Airship_Flank extends Agent{
private static final long serialVersionUID=1844677L;
	public boolean active(){
		if(super.active()){
			if(airship!=null)return airship.active();
			return true;
		}
		return false;
	}
	public AirshipFlank af;
	public Human airship=null;
	public Airship_Flank(AirshipFlank af){
		super(0,0);
		this.af=af;
		af.ent=this;
		hp=maxHp();
		removed=true;
	}
	
	public boolean pickable(){return airship==null;}
	public void pickedBy(Agent a){
		if(!removed){
			removed=true;
			af.drop(a.x,a.y);
		}
	}
	
	public double hardness(){return af.hardness();}
	public double buoyancyForce(){return 2;}
	public boolean useInArmor(){return true;}
	@Override
	public double touchVal(){return 2;}
	@Override
	public Agent getSrc(){return airship==null?this:airship;}
	@Override
	public double maxHp(){return af==null?1:af.maxHp();}
	@Override
	Group group(){return Group.STONE;}
	
	@Override
	public String getName(){
		if(airship==null)return "飞行箱";
		return airship.getName();
	}
	
	public void update(Human w){
		//super.update();
		//System.out.println(this+" "+af+"  "+af.ent+" update:"+dir+","+hp+","+removed);
		Armor ar=w.armor.get();
		if(ar instanceof Airship)
		{
			Airship as=(Airship)ar;
			airship=w;
			if(af==as.af1.get())dir=-1;else dir=1;
			if(hp>0&&removed){
				new SetRelPos(this,w,(w.width()+width())*dir,0);
				add();
			}else{
				double xd=w.x+(w.width()+width()*0.96)*dir-x;
				double yd=w.y-y;
				if(abs(xd)>width()*0.7||abs(yd)>height()){
					if(af==as.af1.get())as.af1.pop();
					else as.af2.pop();
					airship=null;
					return;
				}
				double m=min(mass(),w.mass())*0.6;
				impulse(xd,yd,m);
				w.impulse(xd,yd,-m);
			}
			//System.out.println(as.af1.get()+"  "+as.af2.get());
			w.anti_g+=0.98;
			exchangeVel(w,0.3);
			//new SyncVel(this,w);
			af.update(w);
		}
		else if(ar instanceof FlankArmor)
		{
			airship = w;
			if(hp>0&&removed){
				new SetRelPos(this,w,(w.width()+width())*dir,0);
				add();
			}else{
				double xd=w.x+(w.width()+width()*0.96)*dir-x;
				double yd=w.y-y+0.4;
				/*if(abs(xd)>width()||abs(yd)>height()){
					if(af==as.af1.get())as.af1.pop();
					airship=null;
					return;
				}*/
				double m=min(mass(),w.mass())*0.1;
				impulse(xd,yd,m);
				w.impulse(xd,yd,-m);
			}
			exchangeVel(w,0.3);
			//new SyncVel(this,w);
			af.update(w);
		}
	}
	
	public void dock(Human w){
		if(airship!=null)return;
		Armor ar=w.armor.get();
		if(ar instanceof Airship)
		{
			Airship a=(Airship)ar;
			if(dir==-1&&a.af1.isEmpty()&&abs(y-w.y)<0.3&&x<w.left){
				a.af1.insert(af);
				airship=w;
			}
			if(dir==1&&a.af2.isEmpty()&&abs(y-w.y)<0.3&&x>w.right){
				a.af2.insert(af);
				airship=w;
			}
		}
		//else if( ar instanceof FlankArmor )
		//{
			
	}

	public double width(){
		return 1;
	}
	public double height(){
		return 1;
	}
	public double mass(){
		return af.mass();
	}
	@Override
	public AttackFilter getAttackFilter(){return af;}
	public double gA(){return 0;}
	@Override
	public double onImpact(double v){
		return af.onImpact(v);
	}
	public boolean chkRemove(long t){
		return false;
	}
	public boolean hasBlood(){return false;}
	
	@Override
	public void update(){
		super.update();
		//System.out.println(x+","+y);
		if(rnd()*30<1)addHp(10);
		boolean exist=false;
		if(airship!=null){
			Armor ar=airship.armor.get();
			if(ar instanceof Airship){
				Airship a=(Airship)ar;
				if(a.af1.get()==af||a.af2.get()==af){
					exist=true;
					exchangeVel(airship,0.3);
				}
			}
			else if( ar instanceof FlankArmor )
			{
				FlankArmor a=(FlankArmor)ar;
				if(a.af1.get()==af)
				{
					exist=true;
					exchangeVel(airship,0.3);
				}
			}//else exist=true;
		}else exist=true;
		if(hp<=0&&!removed){
			kill();
		}
		if(!exist){
			//System.out.println(dir+":removed");
			removed=true;
		}
	}
	@Override
	public void action(){}
	@Override
	public boolean chkBlock(){return true;}
	@Override
	public boolean chkRigidBody(){return true;}
	@Override
	public BmpRes getBmp(){return af.getBmp();}
	@Override
	public boolean drawRev(){return dir==1;}
	@Override
	void onKill(){
		BmpRes bmp=af.getBmp();//getBmp();
		if(bmp!=null){
			new Fragment.Config().setEnt(this).setGrid(4,4,16).setTime(50).apply();
		}
		af.onBroken();
		super.onKill();
	}
}
