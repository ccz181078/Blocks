package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class DarkSword extends EnergySwordType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkSword");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	public void onAttack(Entity e,Source src){
		e.onAttackedByDark(10,src);
		super.onAttack(e,src);
	}
}

