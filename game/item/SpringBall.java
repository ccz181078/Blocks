package game.item;

import util.BmpRes;
import game.entity.*;

public class SpringBall extends BoundaryBall{
	public static BmpRes bmp=new BmpRes("Item/SpringBall");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.SpringBall(warhead);
	}
}