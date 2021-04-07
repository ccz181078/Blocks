package game.item;

import util.BmpRes;
import game.entity.*;

public class BlackHole extends StoneBall{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/BlackHole");
	public BmpRes getBmp(){return bmp;}
	public Entity toEnt(){
		return new game.entity.BlackHole();
	}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	
	@Override
	public int maxAmount(){return 1;}
};
