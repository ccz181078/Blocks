package game.block;

import util.BmpRes;
import game.entity.Entity;
import static java.lang.Math.*;

public class WoodenScaffoldBlock extends WoodenType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/WoodenScaffoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 20;}
	public void touchEnt(int x,int y,Entity ent){
		if(min(y+1,ent.top)-max(y,ent.bottom)>0.1)ent.climbable=true;
		super.touchEnt(x,y,ent);
	}
}
