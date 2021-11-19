package game.block;

import static java.lang.Math.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class IronLadderBlock extends IronBasedType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronLadderBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public double transparency(){return 0.1;}
	public boolean isSolid(){return false;}
	public void touchEnt(int x,int y,Entity ent){
		onLadderTouchEnt(x,y,ent);
		super.touchEnt(x,y,ent);
	}
	double friction(){return 0.5;}
	double frictionIn1(){return 0.5;}
	double frictionIn2(){return 0.05;}
};
