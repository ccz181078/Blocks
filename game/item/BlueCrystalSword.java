package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class BlueCrystalSword extends EnergySwordType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/BlueCrystalSword");
	public BmpRes getBmp(){return bmp;}
	@Override
	public void onAttack(Entity e,Source src){
		e.onAttackedByEnergy(10,src);
		if(rnd()<0.1){
			new FocusedEnergy().initPos(e.x,e.y,e.xv,e.yv,src).setHpScale(0.3).add();
		}
		super.onAttack(e,src);
	}
}

