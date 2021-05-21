package game.item;

import util.BmpRes;
import game.entity.*;

public class DarkIronBall extends IronBall{
private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/DarkIronBall");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.DarkIronBall();
	}
};
