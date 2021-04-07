package game.item;

import util.BmpRes;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class IronBall_HD extends ExplosiveBall{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/IronBall_HD");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public Entity toEnt(){
		return new game.entity.IronBall_HD();
	}
	@Override
	public int maxAmount(){return 4;}
	
	@Override
	public void autoLaunch(Human h,Agent a){
		if(rnd()<0.2)super.autoLaunch(h,a);
	}
};
