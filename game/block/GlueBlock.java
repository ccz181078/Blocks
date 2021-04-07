package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;
import game.item.Item;

public class GlueBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/GlueBlock_",3);
	public BmpRes getBmp(){return bmp[state];}
	int maxDamage(){return 300;}
	@Override
	public boolean cmpType(Item item){
		return super.cmpType(item)&&((GlueBlock)item).state==state;
	}
	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	@Override
	public int shockWaveResistance(){return 10000;}
	public boolean isSolid(){return state==2;}
	protected void des(int x,int y,int v){
		if(state==0)return;
		super.des(x,y,v);
	}
	double friction(){return 0.4;}
	public double transparency(){return state==0?0.5:1;}
	public double getJumpAcc(){return 0;}
	@Override
	public void onFireUp(int x,int y){
		if(state>0&&rnd()<0.1){
			--state;
		}
	}
	@Override
	public boolean onUpdate(int x,int y){
		checkDry(x,y);
		if(state==0){
			damage=0;
			return false;
		}
		if(state==1){
			if(damage>100){
				damage=0;
				state=0;
			}else damage=max(0,damage-10);
			return false;
		}
		return super.onUpdate(x,y);
	}
	private void checkDry(int x,int y){
		if(state<2&&World.cur.get(x+rndi(-1,1),y+rndi(-1,1)).rootBlock().isCoverable()){
			++state;
		}
	}
	int state=0;
	@Override
	public double frictionIn1(){return 0.05;}
	@Override
	public double frictionIn2(){return 1000;}
	/*public void touchEnt(int x,int y,Entity ent){
		if(state==2){
			super.touchEnt(x,y,ent);
			return;
		}
		double k=intersection(x,y,ent);
		ent.f+=k*0.95;
		ent.inblock+=k*0.03;
		ent.anti_g+=k*8;
	}*/
	
	@Override
	protected int crackType(){return 0;}
};
