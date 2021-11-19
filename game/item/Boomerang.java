package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class Boomerang extends StoneBall{
	public static BmpRes bmp[]=BmpRes.load("Item/Boomerang_",2);
	public BmpRes getBmp(){return bmp[rev?1:0];}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	public boolean rev;
	@Override
	protected Entity toEnt(){
		return new game.entity.Boomerang(this);
	}
	@Override
	public BmpRes getUseBmp(){return rotate_btn;}
	@Override
	int energyCost(){return 5;}
	public void onUse(Human a){
		rev=!rev;
		super.onUse(a);
	}
};
