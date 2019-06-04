package game.block;

import util.BmpRes;
import game.entity.Entity;
import game.entity.Agent;
import game.entity.Human;
import static java.lang.Math.*;

public class ConveyorBeltBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	private static BmpRes bmp[]=BmpRes.load("Block/ConveyorBeltBlock_",8);
	public BmpRes getBmp(){
		int t=(int)(game.world.World.cur.time%4);
		return bmp[tp/2*4+((tp&1)==1?t:3-t)];
	}
	static double xv[]={-1/16.,1/16.,0,0},yv[]={0,0,-1/16.,1/16.};
	public boolean isSolid(){return false;}
	int maxDamage(){return 50;}
	private int tp=0;
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		double xa=(xv[tp]-ent.xv)*k*0.2;
		double ya=(yv[tp]-ent.yv)*k*0.2;
		ent.xa+=xa;
		ent.ya+=ya;
		ent.f+=(abs(xa)+abs(ya))*10;
		ent.inblock+=k;
		ent.anti_g+=k*4;
	}

	public BmpRes getUseBmp(){return rotate_btn;}
	public void onUse(Human a){
		tp=(tp+1)%4;
		super.onUse(a);
	}
}
