package game.block;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.Entity;
import game.world.World;
import game.entity.Agent;
import game.entity.Zombie;

public class AntiReactionBlock extends Block{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/AntiReactionBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 10000;}
	public boolean circuitCanBePlaced(){return false;}
	public boolean isSolid(){return false;}
	public boolean isCoverable(){return false;}
	public void des(int x,int y,int v){}

	@Override
	public double transparency(){return 0;}
	
	
	@Override
	public double frictionIn1(){return 0.05;}
	@Override
	public double frictionIn2(){return 1000;}

	@Override
	public boolean onUpdate(int x,int y){
		if(rnd()<0.2){
			int x1=x,y1=y,d=rnd()<0.5?-1:1;
			if(rnd()<0.5)x1+=d;
			else y1+=d;
			Block b=World.cur.get(x1,y1);
			if(!(b instanceof ReactionBlock||b instanceof AntiReactionBlock)){
				World.cur.place(x1,y1,new AntiReactionBlock());
			}
		}
		return super.onUpdate(x,y);
	}

	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	@Override
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		while(rnd()<0.6)k*=2;
		ent.onAttackedByDark(k,game.entity.SourceTool.OUT);
		super.touchEnt(x,y,ent);
	}
	
}
