package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.Entity;


public final class DarkVineBlock extends PlantType{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/DarkVineBlock_",2);
	int tp=0;
	public DarkVineBlock(int _tp){tp=_tp;}
	public BmpRes getBmp(){return bmp[tp];}
	public double hardness(){return game.entity.NormalAttacker.LEAF;}
	public void onPress(int x,int y,game.item.Item it){
		if(damage==0&&(it instanceof game.item.Scissors)){
			it.onDesBlock(this);
			if(rnd(maxDamage())<1){
				World.cur.setAir(x,y);
				new DarkVineBlock(tp).drop(x,y);
			}
			return;
		}
		super.onPress(x,y,it);
	}
	public boolean cmpType(game.item.Item b){
		if(b.getClass()==DarkVineBlock.class)return tp==((DarkVineBlock)b).tp;
		return false;
	}
	int maxDamage(){return 30;}
	public void onDestroy(int x,int y){tp=0;}
	/*public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent)*(tp==1?0.4:0.2);
		ent.f+=k;
		ent.inblock+=k;
		ent.anti_g+=k;
	}*/
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		dirt_v=max(0,dirt_v-0.005f);
		repair(0.1,0);
		if(World.cur.get(x,y-1).rootBlock().getClass()==DarkSandBlock.class)dirt_v=min(5,dirt_v+(tp==1?1f:0.1f));
		int x1=x,y1=y;
		if(rnd()<0.7)x1+=(rnd()<0.5?-1:1);
		else y1+=(rnd()<0.5?-1:1);
		Block b=World.cur.get(x1,y1);
		if(b.getClass()==DarkVineBlock.class){
			spread((PlantType)b,0.1f);
		}else if(dirt_v>2&&b.isCoverable()){
			DarkVineBlock w=new DarkVineBlock(0);
			spread(w,0.3f);
			World.cur.place(x1,y1,w);
		}
		return false;
	}
}
