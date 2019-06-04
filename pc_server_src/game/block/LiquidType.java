package game.block;

import game.world.World;
import game.item.Item;
import game.item.Bucket;
import game.entity.Entity;
import static util.MathUtil.*;
import game.entity.Boat;


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
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		ent.f+=k*friction();
		ent.inblock+=k*0.8;
		ent.anti_g+=k*0.3;
		if(ent instanceof Boat)ent.ya+=k*0.06;
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
