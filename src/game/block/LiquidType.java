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
				World._.setAir(x,y);
				asItem().drop(x,y);
			}
		}
	}
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		ent.f+=k*friction();
		ent.inblock+=k*0.8;
		ent.anti_g+=k*0.3;
	}

	public void des(int x,int y,int v){}

	public boolean onCheck(int x,int y){
		return false;
	}
	public boolean onUpdate(int x,int y){
		if(World._.get(x,y-1).isCoverable()){
			World._.setAir(x,y);
			World._.place(x,y-1,this);
			return true;
		}
		Block l=World._.get(x-1,y);
		Block r=World._.get(x+1,y);
		if(l.isCoverable()){
			if(!r.isCoverable()||rnd()<0.5){
				World._.setAir(x,y);
				World._.place(x-1,y,this);
				return true;
			}else{
				World._.setAir(x,y);
				World._.place(x+1,y,this);
				return true;
			}
		}else if(r.isCoverable()){
			World._.setAir(x,y);
			World._.place(x+1,y,this);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCond(int x,int y){
		return
			World._.get(x,y-1).isCoverable()||
			World._.get(x-1,y).isCoverable()||
			World._.get(x+1,y).isCoverable();
	}
	
};
