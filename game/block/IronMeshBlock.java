package game.block;

import util.BmpRes;
import game.entity.Entity;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class IronMeshBlock extends IronBasedType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/IronMeshBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 20;}
	public double transparency(){return 0.1;}
	public boolean isSolid(){return false;}
	public boolean forceCheckEnt(){return true;}
	public boolean chkNonRigidEnt(){return true;}
	
	@Override
	public double frictionIn2(){return 10;}
	public void touchEnt(int x,int y,Entity ent){
		if(!ent.harmless()&&rnd()*5/ent.mass()<intersectionLength(x,y,ent))des(x,y,1);
		super.touchEnt(x,y,ent);
		/*double k=intersection(x,y,ent);
		ent.f+=k*0.99;
		ent.inblock+=k;
		ent.anti_g+=k;*/
	}
}
