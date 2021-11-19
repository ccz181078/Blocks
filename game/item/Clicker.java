package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class Clicker extends Item{
	static BmpRes bmp=new BmpRes("Item/Clicker");
	public BmpRes getBmp(){return bmp;}
	@Override
	public int maxAmount(){return 1;}
	public boolean useInArmor(){return true;}
}
