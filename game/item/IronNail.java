package game.item;

import util.BmpRes;

public class IronNail extends Bullet{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronNail");
	public BmpRes getBmp(){return bmp;}
	public double attackValue(){return 15;}
};
