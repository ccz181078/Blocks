package game.item;

import util.BmpRes;
import game.entity.Agent;

public class IronBall extends StoneBall{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronBall");
	public BmpRes getBmp(){return bmp;}
	public game.entity.StoneBall getBall(){
		return new game.entity.IronBall();
	}
};
