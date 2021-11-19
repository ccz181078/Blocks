package game.block;

import util.BmpRes;
import game.entity.Entity;
import static java.lang.Math.*;

public class IronScaffoldBlock extends IronBasedType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronScaffoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 100;}
	public double transparency(){return 0.1;}
	double friction(){return 0.5;}
	double frictionIn1(){return 0.5;}
	
	public void touchEnt(int x,int y,Entity ent){
		onScaffoldTouchEnt(x,y,ent);
		super.touchEnt(x,y,ent);
	}
}
