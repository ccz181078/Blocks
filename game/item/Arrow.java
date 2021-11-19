package game.item;

import util.BmpRes;
import game.entity.Entity;
import static util.MathUtil.*;

public class Arrow extends ThrowableItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Arrow"),bmps[]=BmpRes.load("Item/Arrow_",6);
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public BmpRes getBmp(game.entity.Arrow.Type type){
		if(type==game.entity.Arrow.Type.FIRE)return bmps[rndi(0,2)];
		if(type==game.entity.Arrow.Type.DARK)return bmps[3];
		if(type==game.entity.Arrow.Type.ENERGY)return bmps[4];
		if(type==game.entity.Arrow.Type.BLOOD)return bmps[5];
		return bmp;
	}
	
	int energyCost(){return 1;}
	@Override
	protected Entity toEnt(){
		return new game.entity.Arrow(this,null);
	}
	
	public int attackValue(){return 33;}
	
};

class IronArrow extends Arrow{
	static BmpRes bmps[]=BmpRes.load("Item/IronArrow_",6);
	public BmpRes getBmp(){return bmps[0];}
	public BmpRes getBmp(game.entity.Arrow.Type type){
		if(type==game.entity.Arrow.Type.FIRE)return bmps[1];
		if(type==game.entity.Arrow.Type.DARK)return bmps[2];
		if(type==game.entity.Arrow.Type.ENERGY)return bmps[3];
		if(type==game.entity.Arrow.Type.BLOOD)return bmps[4];
		return bmps[0];
	}
	public int attackValue(){return 66;}
}