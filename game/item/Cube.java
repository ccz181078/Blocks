package game.item;

import util.BmpRes;
import game.entity.Entity;

public class Cube extends LaunchableItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Cube");
	public BmpRes getBmp(){return bmp;}
	@Override
	protected Entity toEnt(){
		return new game.entity.DarkCube();
	}
	public double launchValue(){return 2000;}
};
