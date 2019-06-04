package game.item;

import util.BmpRes;
import game.entity.*;

public class Stick extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Stick");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 7;}
}
