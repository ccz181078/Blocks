package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public abstract class RPGItem extends LaunchableItem{
private static final long serialVersionUID=1844677L;
	@Override
	public int maxAmount(){return 16;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}