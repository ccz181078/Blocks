package game.block;

import game.world.World;
import game.item.Item;
import game.item.Bucket;
import game.entity.Entity;
import static util.MathUtil.*;


public abstract class LiquidType extends Block{
	private static final long serialVersionUID=1844677L;
	public boolean isSolid(){return false;}
	public void onPress(int x,int y,Item it){
		if(it instanceof Bucket){
			it.onDesBlock(this);
			if(rnd()*maxDamage()<1){
				World.cur.setAir(x,y);
				drop(x,y);
			}
		}
	}
	public boolean forceCheckEnt(){return true;}
	public double fallValue(){return 100;}
	double frictionIn1(){return 0.2;}
	public boolean chkNonRigidEnt(){return true;}
	public void onOverlap(int x,int y,Entity ent,double k){
		k*=ent.fluidResistance();
		ent.fs2+=k*frictionIn1();
		ent.fc+=k*frictionIn2()/ent.mass();
	}
	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		double k=intersection(x,y,ent);
		//ent.f+=k*friction()*ent.fluidResistance();
		//ent.inblock+=k*0.8;
		ent.ya+=k*ent.buoyancyForce()*ent.gA();
	}

	public void des(int x,int y,int v){}

	public boolean onCheck(int x,int y){
		return false;
	}
	public boolean onUpdate(int x,int y){
		return onUpdate(x,y,this);
	}

	public void onDestroy(int x,int y){}
	
	public static boolean onUpdate(int x,int y,Block _this){
		if(World.cur.get(x,y-1).isCoverable()){
			World.cur.setAir(x,y);
			World.cur.place(x,y-1,_this);
			return true;
		}
		Block l=World.cur.get(x-1,y);
		Block r=World.cur.get(x+1,y);
		if(l.isCoverable()){
			if(!r.isCoverable()||rnd()<0.5){
				World.cur.setAir(x,y);
				World.cur.place(x-1,y,_this);
				return true;
			}else{
				World.cur.setAir(x,y);
				World.cur.place(x+1,y,_this);
				return true;
			}
		}else if(r.isCoverable()){
			World.cur.setAir(x,y);
			World.cur.place(x+1,y,_this);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCond(int x,int y){
		return
			World.cur.get(x,y-1).isCoverable()||
			World.cur.get(x-1,y).isCoverable()||
			World.cur.get(x+1,y).isCoverable();
	}
	
};
