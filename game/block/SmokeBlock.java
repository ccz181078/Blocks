package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.entity.*;

public class SmokeBlock extends Block{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Block/SmokeBlock_",3);
	public BmpRes getBmp(){return bmp[(int)(World.cur.time/4)%3];}
	int maxDamage(){return 2000;}
	public boolean circuitCanBePlaced(){return false;}
	public boolean isSolid(){return false;}
	public boolean isCoverable(){return false;}
	public double hardness(){return game.entity.NormalAttacker.POWDER;}
	
	public double impactValue(){return 0;}
	
	double frictionIn1(){return 0.05;}
	double frictionIn2(){return 0.05;}
		
	@Override
	public void onDestroy(int x,int y){}
	@Override
	public void onVanish(double x,double y,Source src){}
	@Override
	public void touchEnt(int x,int y,Entity e){
		if(e instanceof FallingBlock)return;
		if(e instanceof PureEnergyBall)e.onAttacked(2,SourceTool.block(x,y,this),this);
		super.touchEnt(x,y,e);
	}
	@Override
	public boolean onUpdate(int x,int y){
		if(rnd()<0.2){
			int x1=x,y1=y,d=rnd()<0.5?-1:1;
			if(rnd()<0.5)x1+=d;
			else y1+=d;
			if(World.cur.get(x1,y1).isCoverable()){
				World.cur.setAir(x,y);
				return true;
			}
		}
		return super.onUpdate(x,y);
	}
	

	public boolean chkNonRigidEnt(){return true;}
	protected int crackType(){return -1;}
	
}