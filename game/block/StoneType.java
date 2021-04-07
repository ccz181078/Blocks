package game.block;

import game.item.Item;
import static util.MathUtil.*;
import game.entity.Entity;

abstract public class StoneType extends Block{
	private static final long serialVersionUID=1844677L;
	public void onPress(int x,int y,Item item){
		des(x,y,item.pickaxVal(),item);
		item.onDesBlock(this);
	}
	public double hardness(){return game.entity.NormalAttacker.STONE;}
	@Override
	public int shockWaveResistance(){return super.shockWaveResistance()*3;}
	@Override
	double friction(){return 0.14;}
	@Override
	double frictionIn1(){return 0.5;}
	@Override
	double minEnterVel(){return isSolid()?5:0;}
	/*@Override
	public void onOverlap(int x,int y,Entity ent,double k){
		super.onOverlap(x,y,ent,k);
		int c=rf2i(k*2);
		if(c>0)des(x,y,c);
	}*/
	
	@Override
	protected int crackType(){return 1;}
};
class PureStoneType extends StoneType{
	private static final long serialVersionUID=1844677L;
	
	int maxDamage(){return 600;}
	@Override
	protected int crackType(){return 0;}
};
class IronBasedType extends StoneType{
	private static final long serialVersionUID=1844677L;
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	@Override
	protected int crackType(){return 0;}
};
