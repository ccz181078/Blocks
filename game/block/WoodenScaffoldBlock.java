package game.block;

import util.BmpRes;
import game.entity.Entity;
import static java.lang.Math.*;

public class WoodenScaffoldBlock extends WoodenType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WoodenScaffoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 20;}
	double friction(){return 0.5;}
	double frictionIn1(){return 0.5;}
	public void touchEnt(int x,int y,Entity ent){
		onScaffoldTouchEnt(x,y,ent);
		super.touchEnt(x,y,ent);
	}
}
