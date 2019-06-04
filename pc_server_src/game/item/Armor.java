package game.item;

import game.block.Block;
import game.entity.*;
import static util.MathUtil.*;
import util.BmpRes;

public abstract class Armor extends DefendTool{
	private static final long serialVersionUID=1844677L;
	public abstract BmpRes getArmorBmp();
	public void onUpdate(Human w){}
}
