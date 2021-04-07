package game.block;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class SoftIronScaffoldBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/SoftIronScaffoldBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 1;}
	public double transparency(){return 0.1;}
	public void touchEnt(int x,int y,Entity ent){
		if(min(y+1,ent.top)-max(y,ent.bottom)>0.1)ent.climbable=true;
		if(rnd()<0.001||ent.in_wall){
			des(x,y,1);
		}
		super.touchEnt(x,y,ent);
	}
};
