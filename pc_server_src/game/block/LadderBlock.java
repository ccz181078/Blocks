package game.block;

import static java.lang.Math.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class LadderBlock extends WoodenType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/LadderBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 20;}
	public boolean isSolid(){return false;}
	int h;
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		ent.f+=k*0.4;
		ent.inblock+=k;
		ent.anti_g+=k*8;
	}
};
