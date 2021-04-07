package game.item;

import util.BmpRes;

public class DarkBall extends BasicBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/DarkBall");
	public BmpRes getBmp(){return bmp;}
	
	@Override
	game.entity.Entity getBall(){return new game.entity.DarkBall().setHpScale(35);}
};
